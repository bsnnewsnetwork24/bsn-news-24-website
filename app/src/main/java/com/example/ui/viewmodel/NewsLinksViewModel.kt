package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BsnDatabase
import com.example.data.local.NewsLinkEntity
import com.example.data.remote.FirestoreNewsLinkRepository
import com.example.data.remote.GoogleAuthHelper
import com.example.data.repository.NewsLinksRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsLinksUiState(
  val searchQuery: String = "",
  val links: List<NewsLinkEntity> = emptyList(),
  val totalLinksCount: Int = 0,
  val isSignedIn: Boolean = false,
  val userEmail: String? = null,
  val userName: String? = null,
  val isSyncing: Boolean = false,
  val isAuthBusy: Boolean = false,
  val statusMessage: String? = null,
  val errorMessage: String? = null,
)

class NewsLinksViewModel(
  private val repository: NewsLinksRepository,
  private val auth: FirebaseAuth? = runCatching { Firebase.auth }.getOrNull(),
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  private val _authUser = MutableStateFlow<FirebaseUser?>(auth?.currentUser)
  private val _syncState = MutableStateFlow(SyncUiFlags())

  private var cloudListenerJob: Job? = null

  private val authStateListener =
    FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      _authUser.value = user
      if (user != null) {
        startCloudSyncAndObserve(user.uid)
      } else {
        stopCloudObserver()
      }
    }

  val uiState: StateFlow<NewsLinksUiState> =
    combine(repository.observeLocalLinks(), _searchQuery, _authUser, _syncState) {
        allLinks,
        query,
        user,
        flags ->
        val trimmedQuery = query.trim()
        val filtered =
          if (trimmedQuery.isEmpty()) {
            allLinks
          } else {
            allLinks.filter {
              it.headline.contains(trimmedQuery, ignoreCase = true) ||
                it.url.contains(trimmedQuery, ignoreCase = true)
            }
          }
        NewsLinksUiState(
          searchQuery = query,
          links = filtered,
          totalLinksCount = allLinks.size,
          isSignedIn = user != null,
          userEmail = user?.email,
          userName = user?.displayName,
          isSyncing = flags.isSyncing,
          isAuthBusy = flags.isAuthBusy,
          statusMessage = flags.statusMessage,
          errorMessage = flags.errorMessage,
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NewsLinksUiState(),
      )

  init {
    auth?.addAuthStateListener(authStateListener)
    auth?.currentUser?.let { startCloudSyncAndObserve(it.uid) }
  }

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun clearMessages() {
    _syncState.update { it.copy(statusMessage = null, errorMessage = null) }
  }

  fun saveNewsLink(headline: String, url: String, existingId: String? = null) {
    viewModelScope.launch {
      _syncState.update { it.copy(statusMessage = null, errorMessage = null) }
      val res = repository.addOrUpdateLocalLink(headline, url, existingId)
      res.fold(
        onSuccess = { saved ->
          val msg =
            if (saved.isSynced) {
              "নিউজ লিংকটি ফোন এবং Gmail ক্লাউডে সংরক্ষিত হয়েছে।"
            } else if (_authUser.value != null) {
              "নিউজ লিংকটি ফোনে সেভ হয়েছে। ইন্টারনেট পেলে ক্লাউডে সিঙ্ক হবে।"
            } else {
              "নিউজ লিংকটি ফোনে নিরাপদে সংরক্ষিত হয়েছে।"
            }
          _syncState.update { it.copy(statusMessage = msg) }
        },
        onFailure = { err ->
          _syncState.update {
            it.copy(errorMessage = err.message ?: "নিউজ লিংক সংরক্ষণ করা যায়নি।")
          }
        },
      )
    }
  }

  fun deleteNewsLink(entity: NewsLinkEntity) {
    viewModelScope.launch {
      repository.deleteLink(entity)
      _syncState.update { it.copy(statusMessage = "নিউজ লিংকটি মুছে ফেলা হয়েছে।") }
    }
  }

  fun signInWithGmailForCloudSave(context: Context) {
    if (!repository.isOnline()) {
      _syncState.update {
        it.copy(
          statusMessage = null,
          errorMessage =
            "ইন্টারনেট সংযোগ নেই। তবে আপনি অফলাইনে ফোনের মধ্যেই সকল নিউজ লিংক সংরক্ষণ, দেখা, এডিট ও মুছতে পারবেন।",
        )
      }
      return
    }
    viewModelScope.launch {
      _syncState.update { it.copy(isAuthBusy = true, statusMessage = null, errorMessage = null) }
      val result = GoogleAuthHelper.signInWithGoogle(context)
      result.fold(
        onSuccess = { user ->
          _authUser.value = user
          _syncState.update {
            it.copy(
              isAuthBusy = false,
              statusMessage = "Gmail (${user.email ?: ""}) যুক্ত হয়েছে। ক্লাউড সিঙ্ক চলছে...",
            )
          }
          syncNow()
        },
        onFailure = { err ->
          val isCancelled = err is GoogleAuthHelper.AuthCancelledException
          _syncState.update {
            it.copy(
              isAuthBusy = false,
              errorMessage = if (isCancelled) err.message else err.message ?: "সাইন-ইন ব্যর্থ হয়েছে।",
            )
          }
        },
      )
    }
  }

  fun syncNow() {
    val uid = _authUser.value?.uid ?: return
    viewModelScope.launch {
      _syncState.update { it.copy(isSyncing = true, errorMessage = null) }
      val res = repository.syncWithCloud(uid)
      res.fold(
        onSuccess = { summary ->
          val msg =
            when {
              summary.restored > 0 && summary.uploaded > 0 ->
                "সিঙ্ক সম্পন্ন: ${summary.uploaded}টি আপলোড ও ${summary.restored}টি আগের লিংক পুনরুদ্ধার হয়েছে।"
              summary.restored > 0 ->
                "ক্লাউড থেকে আপনার আগের ${summary.restored}টি লিংক পুনরুদ্ধার করা হয়েছে।"
              summary.uploaded > 0 ->
                "${summary.uploaded}টি নতুন লিংক Gmail ক্লাউডে সংরক্ষিত হয়েছে।"
              else -> "আপনার সকল নিউজ লিংক Gmail ক্লাউডে আপ-টু-ডেট আছে।"
            }
          _syncState.update { it.copy(isSyncing = false, statusMessage = msg) }
        },
        onFailure = { err ->
          _syncState.update {
            it.copy(
              isSyncing = false,
              errorMessage = err.message ?: "ক্লাউড সিঙ্ক ব্যর্থ হয়েছে।",
            )
          }
        },
      )
    }
  }

  fun signOut(context: Context) {
    viewModelScope.launch {
      stopCloudObserver()
      GoogleAuthHelper.signOut(context)
      _authUser.value = null
      _syncState.update {
        it.copy(
          isSyncing = false,
          statusMessage = "Gmail সাইন-আউট সম্পন্ন হয়েছে। আপনার লোকাল লিংক ফোনেই সুরক্ষিত আছে।",
          errorMessage = null,
        )
      }
    }
  }

  private fun startCloudSyncAndObserve(uid: String) {
    stopCloudObserver()
    cloudListenerJob =
      viewModelScope.launch {
        repository.syncWithCloud(uid)
        try {
          repository.observeAndMergeCloudRealtime(uid)
        } catch (e: CancellationException) {
          throw e
        } catch (e: Exception) {
          if (auth?.currentUser?.uid == uid) {
            _syncState.update {
              it.copy(
                errorMessage =
                  "রিয়েল-টাইম ক্লাউড সিঙ্কে সমস্যা হয়েছে: ${e.localizedMessage ?: "পুনরায় চেষ্টা করুন"}"
              )
            }
          }
        }
      }
  }

  private fun stopCloudObserver() {
    cloudListenerJob?.cancel()
    cloudListenerJob = null
  }

  override fun onCleared() {
    super.onCleared()
    auth?.removeAuthStateListener(authStateListener)
    stopCloudObserver()
  }

  private data class SyncUiFlags(
    val isSyncing: Boolean = false,
    val isAuthBusy: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null,
  )

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val appCtx = context.applicationContext
      val db = BsnDatabase.getInstance(appCtx)
      val firestoreRepo = FirestoreNewsLinkRepository(appCtx)
      val repo = NewsLinksRepository(appCtx, db.newsLinkDao(), firestoreRepo)
      return NewsLinksViewModel(repo) as T
    }
  }
}
