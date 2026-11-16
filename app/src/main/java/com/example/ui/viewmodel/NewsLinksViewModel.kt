package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.LocalNewsLinksStore
import com.example.data.local.NewsLinkEntity
import com.example.data.repository.NewsLinksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsLinksUiState(
  val searchQuery: String = "",
  val links: List<NewsLinkEntity> = emptyList(),
  val totalLinksCount: Int = 0,
  val statusMessage: String? = null,
  val errorMessage: String? = null,
)

class NewsLinksViewModel(
  private val repository: NewsLinksRepository,
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  private val _feedback = MutableStateFlow(FeedbackState())

  val uiState: StateFlow<NewsLinksUiState> =
    combine(repository.observeLocalLinks(), _searchQuery, _feedback) { allLinks, query, feedback ->
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
          statusMessage = feedback.statusMessage,
          errorMessage = feedback.errorMessage,
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NewsLinksUiState(),
      )

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun clearMessages() {
    _feedback.update { it.copy(statusMessage = null, errorMessage = null) }
  }

  fun saveNewsLink(headline: String, url: String, existingId: String? = null) {
    viewModelScope.launch {
      _feedback.update { it.copy(statusMessage = null, errorMessage = null) }
      val res = repository.addOrUpdateLocalLink(headline, url, existingId)
      res.fold(
        onSuccess = {
          _feedback.update {
            it.copy(statusMessage = "নিউজ লিংকটি অ্যাপের ভেতরে নিরাপদে সংরক্ষিত হয়েছে।")
          }
        },
        onFailure = { err ->
          _feedback.update {
            it.copy(errorMessage = err.message ?: "নিউজ লিংক সংরক্ষণ করা যায়নি।")
          }
        },
      )
    }
  }

  fun deleteNewsLink(entity: NewsLinkEntity) {
    viewModelScope.launch {
      repository.deleteLink(entity)
      _feedback.update { it.copy(statusMessage = "নিউজ লিংকটি মুছে ফেলা হয়েছে।") }
    }
  }

  private data class FeedbackState(
    val statusMessage: String? = null,
    val errorMessage: String? = null,
  )

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val appCtx = context.applicationContext
      val store = LocalNewsLinksStore(appCtx)
      val repo = NewsLinksRepository(store)
      return NewsLinksViewModel(repo) as T
    }
  }
}
