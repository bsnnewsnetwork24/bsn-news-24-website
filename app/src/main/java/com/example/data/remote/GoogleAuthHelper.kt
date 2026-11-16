package com.example.data.remote

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object GoogleAuthHelper {
  private const val TAG = "GoogleAuthHelper"

  fun currentUser(): FirebaseUser? = runCatching { Firebase.auth.currentUser }.getOrNull()

  suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
    val clientId =
      try {
        context.getString(R.string.default_web_client_id)
      } catch (e: Exception) {
        return Result.failure(
          IllegalStateException("Google Sign-In কনফিগারেশন পাওয়া যায়নি (default_web_client_id)।")
        )
      }

    val activity = context.findActivity()
    val credentialManager = CredentialManager.create(context)
    val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
    val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

    return try {
      val result = credentialManager.getCredential(activity ?: context, request)
      val credential = result.credential
      if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        val authResult = Firebase.auth.signInWithCredential(authCredential).await()
        val user =
          authResult.user
            ?: return Result.failure(IllegalStateException("Gmail সাইন-ইন সম্পন্ন হয়নি।"))
        Result.success(user)
      } else {
        Result.failure(IllegalStateException("অপ্রত্যাশিত ক্রেডেনশিয়াল পাওয়া গেছে।"))
      }
    } catch (e: GetCredentialCancellationException) {
      Log.w(TAG, "Google Sign-In cancelled or dismissed: ${e.message}", e)
      Result.failure(
        AuthCancelledException("Gmail সাইন-ইন বাতিল করা হয়েছে। সংরক্ষণের জন্য একটি Gmail অ্যাকাউন্ট নির্বাচন করুন।")
      )
    } catch (e: NoCredentialException) {
      Log.w(TAG, "No Google credentials found on device: ${e.message}", e)
      Result.failure(
        IllegalStateException("এই ডিভাইসে কোনো Google অ্যাকাউন্ট যুক্ত নেই বা প্লে সার্ভিস পাওয়া যায়নি।")
      )
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e(TAG, "Google Sign-In failed", e)
      Result.failure(
        IllegalStateException(
          e.localizedMessage ?: "Gmail দিয়ে সাইন-ইন করতে সমস্যা হয়েছে। ইন্টারনেট সংযোগ পরীক্ষা করুন।"
        )
      )
    }
  }

  suspend fun attemptSilentSignIn(context: Context): FirebaseUser? {
    runCatching { Firebase.auth.currentUser }.getOrNull()?.let {
      return it
    }
    val clientId =
      try {
        context.getString(R.string.default_web_client_id)
      } catch (_: Exception) {
        return null
      }
    val credentialManager = CredentialManager.create(context)
    val googleIdOption =
      GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(clientId)
        .setAutoSelectEnabled(true)
        .build()
    val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

    return try {
      val result = credentialManager.getCredential(context, request)
      val credential = result.credential
      if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        Firebase.auth.signInWithCredential(authCredential).await().user
      } else {
        null
      }
    } catch (_: Exception) {
      null
    }
  }

  suspend fun signOut(context: Context) {
    val credentialManager = CredentialManager.create(context)
    withContext(NonCancellable) {
      try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
      } catch (e: Exception) {
        Log.w(TAG, "Failed to clear credential state: ${e.message}")
      } finally {
        runCatching { Firebase.auth.signOut() }
      }
    }
  }

  class AuthCancelledException(message: String) : Exception(message)

  private tailrec fun Context.findActivity(): Activity? =
    when (this) {
      is Activity -> this
      is ContextWrapper -> baseContext.findActivity()
      else -> null
    }
}
