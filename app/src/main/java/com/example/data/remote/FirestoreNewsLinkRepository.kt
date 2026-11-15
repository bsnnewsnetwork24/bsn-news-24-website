package com.example.data.remote

import android.content.Context
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreNewsLinkRepository(
  private val db: FirebaseFirestore,
  private val auth: FirebaseAuth = Firebase.auth,
) {
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    ),
    Firebase.auth,
  )

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("ক্লাউড সংরক্ষণের জন্য অনুগ্রহ করে Gmail দিয়ে সাইন ইন করুন।")
  }

  fun observeUserLinks(userId: String): Flow<List<CloudNewsLink>> = flow {
    val path = "users/$userId/news_links"
    emitAll(
      db.collection("users")
        .document(userId)
        .collection("news_links")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(
            CloudNewsLink::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
        }
        .catch { error ->
          val firestoreError =
            generateSequence(error) { it.cause }
              .filterIsInstance<FirebaseFirestoreException>()
              .firstOrNull()
          if (firestoreError == null && error is CancellationException) throw error
          val exceptionToLog = firestoreError ?: (error as? Exception)
          if (exceptionToLog != null && auth.currentUser?.uid == userId) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
          }
          throw error
        }
    )
  }

  suspend fun fetchAllLinksOnce(userId: String): Result<List<CloudNewsLink>> {
    val path = "users/$userId/news_links"
    return try {
      val snapshot =
        db.collection("users").document(userId).collection("news_links").get().await()
      val items =
        snapshot.toObjects(
          CloudNewsLink::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
        )
      Result.success(items)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.LIST, path)
      Result.failure(e)
    }
  }

  suspend fun getLinkById(linkId: String): Result<CloudNewsLink?> {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("news_links").document(linkId)
    return try {
      val snapshot = docRef.get().await()
      val item =
        snapshot.toObject(
          CloudNewsLink::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
        )
      Result.success(item)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.GET, docRef.path)
      Result.failure(e)
    }
  }

  suspend fun upsertLink(
    linkId: String,
    headline: String,
    url: String,
    normalizedKey: String,
  ): Result<Unit> {
    val uid = requireUserId()
    val safeId = sanitizeDocId(linkId)
    val docRef = db.collection("users").document(uid).collection("news_links").document(safeId)
    return try {
      val existingSnapshot = docRef.get().await()
      if (existingSnapshot.exists()) {
        val updatePayload =
          mapOf(
            "headline" to headline.trim().take(300),
            "url" to url.trim().take(1000),
            "normalizedKey" to normalizedKey.trim().take(200),
            "updatedAt" to FieldValue.serverTimestamp(),
          )
        docRef.update(updatePayload).await()
      } else {
        val createPayload =
          mapOf(
            "userId" to uid,
            "headline" to headline.trim().take(300),
            "url" to url.trim().take(1000),
            "normalizedKey" to normalizedKey.trim().take(200),
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp(),
          )
        docRef.set(createPayload).await()
      }
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      Result.failure(e)
    }
  }

  suspend fun deleteLink(linkId: String): Result<Unit> {
    val uid = requireUserId()
    val safeId = sanitizeDocId(linkId)
    val docRef = db.collection("users").document(uid).collection("news_links").document(safeId)
    return try {
      docRef.delete().await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      Result.failure(e)
    }
  }

  companion object {
    fun sanitizeDocId(raw: String): String {
      val cleaned = raw.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
      return cleaned.ifBlank { "lnk_default" }.take(128)
    }
  }
}
