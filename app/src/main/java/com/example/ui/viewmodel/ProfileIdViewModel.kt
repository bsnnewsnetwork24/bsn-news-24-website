package com.example.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.JournalistProfile
import com.example.data.local.ProfilePreferencesRepository
import com.example.utils.IdCardGenerator
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ProfileIdUiState(
  val name: String = "",
  val fatherName: String = "",
  val designation: String = "",
  val mobileNumber: String = "",
  val district: String = "",
  val bloodGroup: String = "",
  val photoLocalPath: String = "",
  val savedProfile: JournalistProfile? = null,
  val isSaving: Boolean = false,
  val isProcessingCard: Boolean = false,
  val showEditForm: Boolean = true,
  val statusMessage: String? = null,
  val errorMessage: String? = null,
)

class ProfileIdViewModel(
  private val appContext: Context,
  private val preferencesRepo: ProfilePreferencesRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(ProfileIdUiState())
  val uiState: StateFlow<ProfileIdUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      preferencesRepo.profileFlow.collect { stored ->
        _uiState.update { current ->
          if (stored.isSaved && current.savedProfile == null) {
            current.copy(
              name = stored.name,
              fatherName = stored.fatherName,
              designation = stored.designation,
              mobileNumber = stored.mobileNumber,
              district = stored.district,
              bloodGroup = stored.bloodGroup,
              photoLocalPath = stored.photoLocalPath,
              savedProfile = stored,
              showEditForm = false,
            )
          } else if (stored.isSaved) {
            current.copy(savedProfile = stored)
          } else {
            current
          }
        }
      }
    }
  }

  fun onNameChanged(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
  fun onFatherNameChanged(v: String) = _uiState.update { it.copy(fatherName = v, errorMessage = null) }
  fun onDesignationChanged(v: String) = _uiState.update { it.copy(designation = v, errorMessage = null) }
  fun onMobileChanged(v: String) = _uiState.update { it.copy(mobileNumber = v, errorMessage = null) }
  fun onDistrictChanged(v: String) = _uiState.update { it.copy(district = v, errorMessage = null) }
  fun onBloodGroupChanged(v: String) = _uiState.update { it.copy(bloodGroup = v, errorMessage = null) }

  fun toggleEditForm(show: Boolean) {
    _uiState.update { it.copy(showEditForm = show, statusMessage = null, errorMessage = null) }
  }

  fun clearMessages() {
    _uiState.update { it.copy(statusMessage = null, errorMessage = null) }
  }

  fun importPickedPhoto(uri: Uri) {
    viewModelScope.launch {
      _uiState.update { it.copy(errorMessage = null, statusMessage = null) }
      val copiedPath =
        withContext(Dispatchers.IO) {
          try {
            val destFile = File(appContext.filesDir, "journalist_profile_photo.jpg")
            appContext.contentResolver.openInputStream(uri)?.use { input ->
              FileOutputStream(destFile).use { output -> input.copyTo(output) }
            } ?: return@withContext null
            destFile.absolutePath
          } catch (_: Exception) {
            null
          }
        }
      if (copiedPath != null) {
        _uiState.update {
          it.copy(
            photoLocalPath = copiedPath,
            statusMessage = "ছবি সফলভাবে যুক্ত হয়েছে। এখন \"সংরক্ষণ করুন\" বাটনে চাপ দিন।",
          )
        }
      } else {
        _uiState.update { it.copy(errorMessage = "ছবি লোড করতে সমস্যা হয়েছে। আবার চেষ্টা করুন।") }
      }
    }
  }

  fun saveProfileAndGenerateCard() {
    val state = _uiState.value
    if (state.name.isBlank()) {
      _uiState.update { it.copy(errorMessage = "অনুগ্রহ করে আপনার নাম লিখুন।") }
      return
    }
    if (state.designation.isBlank()) {
      _uiState.update { it.copy(errorMessage = "অনুগ্রহ করে আপনার পদবি লিখুন।") }
      return
    }
    if (state.mobileNumber.isBlank()) {
      _uiState.update { it.copy(errorMessage = "অনুগ্রহ করে মোবাইল নম্বর লিখুন।") }
      return
    }
    if (state.district.isBlank()) {
      _uiState.update { it.copy(errorMessage = "অনুগ্রহ করে জেলার নাম লিখুন।") }
      return
    }

    viewModelScope.launch {
      _uiState.update { it.copy(isSaving = true, errorMessage = null, statusMessage = null) }
      val profile =
        JournalistProfile(
          name = state.name.trim(),
          fatherName = state.fatherName.trim(),
          designation = state.designation.trim(),
          mobileNumber = state.mobileNumber.trim(),
          district = state.district.trim(),
          bloodGroup = state.bloodGroup.trim().ifBlank { "O+" },
          photoLocalPath = state.photoLocalPath,
          isSaved = true,
        )
      preferencesRepo.saveProfile(profile)
      _uiState.update {
        it.copy(
          isSaving = false,
          savedProfile = profile,
          showEditForm = false,
          statusMessage = "আপনার প্রোফাইল ও ডিজিটাল আইডি কার্ড সফলভাবে সংরক্ষিত হয়েছে!",
        )
      }
    }
  }

  fun shareIdCard() {
    val profile = _uiState.value.savedProfile ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessingCard = true, statusMessage = null, errorMessage = null) }
      val res =
        withContext(Dispatchers.IO) {
          val bmp = IdCardGenerator.renderIdCardBitmap(appContext, profile)
          IdCardGenerator.shareIdCard(appContext, bmp)
        }
      res.fold(
        onSuccess = { _uiState.update { it.copy(isProcessingCard = false) } },
        onFailure = { e ->
          _uiState.update {
            it.copy(
              isProcessingCard = false,
              errorMessage = e.message ?: "শেয়ার করতে সমস্যা হয়েছে।",
            )
          }
        },
      )
    }
  }

  fun downloadIdCardImage() {
    val profile = _uiState.value.savedProfile ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessingCard = true, statusMessage = null, errorMessage = null) }
      val res =
        withContext(Dispatchers.IO) {
          val bmp = IdCardGenerator.renderIdCardBitmap(appContext, profile)
          IdCardGenerator.saveIdCardToGallery(appContext, bmp)
        }
      res.fold(
        onSuccess = { msg ->
          _uiState.update { it.copy(isProcessingCard = false, statusMessage = msg) }
        },
        onFailure = { e ->
          _uiState.update {
            it.copy(
              isProcessingCard = false,
              errorMessage = e.message ?: "ডাউনলোড করতে সমস্যা হয়েছে।",
            )
          }
        },
      )
    }
  }

  fun printIdCard(activity: Activity?) {
    val profile = _uiState.value.savedProfile ?: return
    if (activity == null) {
      _uiState.update { it.copy(errorMessage = "প্রিন্ট ডায়ালগ চালু করা যায়নি।") }
      return
    }
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessingCard = true, statusMessage = null, errorMessage = null) }
      val bmp =
        withContext(Dispatchers.IO) { IdCardGenerator.renderIdCardBitmap(appContext, profile) }
      val res = IdCardGenerator.printIdCard(activity, bmp)
      res.fold(
        onSuccess = { _uiState.update { it.copy(isProcessingCard = false) } },
        onFailure = { e ->
          _uiState.update {
            it.copy(
              isProcessingCard = false,
              errorMessage = e.message ?: "প্রিন্ট করতে সমস্যা হয়েছে।",
            )
          }
        },
      )
    }
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val appCtx = context.applicationContext
      return ProfileIdViewModel(appCtx, ProfilePreferencesRepository(appCtx)) as T
    }
  }
}
