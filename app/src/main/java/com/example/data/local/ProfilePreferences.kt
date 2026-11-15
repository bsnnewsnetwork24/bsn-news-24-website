package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "bsn_journalist_profile")

data class JournalistProfile(
  val name: String = "",
  val fatherName: String = "",
  val designation: String = "",
  val mobileNumber: String = "",
  val district: String = "",
  val bloodGroup: String = "",
  val photoLocalPath: String = "",
  val isSaved: Boolean = false,
)

class ProfilePreferencesRepository(private val context: Context) {
  private object Keys {
    val NAME = stringPreferencesKey("profile_name")
    val FATHER_NAME = stringPreferencesKey("profile_father_name")
    val DESIGNATION = stringPreferencesKey("profile_designation")
    val MOBILE = stringPreferencesKey("profile_mobile")
    val DISTRICT = stringPreferencesKey("profile_district")
    val BLOOD_GROUP = stringPreferencesKey("profile_blood_group")
    val PHOTO_PATH = stringPreferencesKey("profile_photo_path")
    val IS_SAVED = booleanPreferencesKey("profile_is_saved")
  }

  val profileFlow: Flow<JournalistProfile> =
    context.profileDataStore.data.map { prefs ->
      JournalistProfile(
        name = prefs[Keys.NAME] ?: "",
        fatherName = prefs[Keys.FATHER_NAME] ?: "",
        designation = prefs[Keys.DESIGNATION] ?: "",
        mobileNumber = prefs[Keys.MOBILE] ?: "",
        district = prefs[Keys.DISTRICT] ?: "",
        bloodGroup = prefs[Keys.BLOOD_GROUP] ?: "",
        photoLocalPath = prefs[Keys.PHOTO_PATH] ?: "",
        isSaved = prefs[Keys.IS_SAVED] ?: false,
      )
    }

  suspend fun saveProfile(profile: JournalistProfile) {
    context.profileDataStore.edit { prefs ->
      prefs[Keys.NAME] = profile.name.trim()
      prefs[Keys.FATHER_NAME] = profile.fatherName.trim()
      prefs[Keys.DESIGNATION] = profile.designation.trim()
      prefs[Keys.MOBILE] = profile.mobileNumber.trim()
      prefs[Keys.DISTRICT] = profile.district.trim()
      prefs[Keys.BLOOD_GROUP] = profile.bloodGroup.trim()
      prefs[Keys.PHOTO_PATH] = profile.photoLocalPath
      prefs[Keys.IS_SAVED] = true
    }
  }
}
