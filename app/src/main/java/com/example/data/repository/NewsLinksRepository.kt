package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.data.local.NewsLinkDao
import com.example.data.local.NewsLinkEntity
import com.example.data.remote.CloudNewsLink
import com.example.data.remote.FirestoreNewsLinkRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class NewsLinksRepository(
  private val context: Context,
  private val dao: NewsLinkDao,
  private val firestoreRepo: FirestoreNewsLinkRepository,
) {
  private fun currentUserUidOrNull(): String? =
    runCatching { Firebase.auth.currentUser?.uid }.getOrNull()

  fun observeLocalLinks(): Flow<List<NewsLinkEntity>> = dao.observeActiveLinks()

  fun isOnline(): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
      ?: return false
    val network = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(network) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
  }

  suspend fun addOrUpdateLocalLink(
    headline: String,
    rawUrl: String,
    existingId: String? = null,
  ): Result<NewsLinkEntity> {
    val cleanHeadline = headline.trim()
    val cleanUrl = NewsLinkEntity.normalizeUrl(rawUrl)
    if (cleanHeadline.isBlank() || cleanUrl.isBlank()) {
      return Result.failure(IllegalArgumentException("অনুগ্রহ করে শিরোনাম এবং লিংক উভয়ই লিখুন।"))
    }
    val key = NewsLinkEntity.computeNormalizedKey(cleanHeadline, cleanUrl)
    val now = System.currentTimeMillis()

    // Prevent duplicates when adding a brand new link
    if (existingId == null) {
      val duplicate = dao.findByNormalizedKey(key)
      if (duplicate != null && !duplicate.isDeletedPending) {
        return Result.failure(IllegalStateException("এই নিউজ লিংকটি ইতিমধ্যেই সংরক্ষিত আছে।"))
      }
    }

    val existing = existingId?.let { dao.findById(it) }
    val currentUid = currentUserUidOrNull()
    val docId = existing?.id ?: key

    val entity =
      NewsLinkEntity(
        id = docId,
        headline = cleanHeadline,
        url = cleanUrl,
        normalizedKey = key,
        createdAtMillis = existing?.createdAtMillis ?: now,
        updatedAtMillis = now,
        isSynced = false,
        syncPending = true,
        ownerUid = currentUid ?: existing?.ownerUid,
        isDeletedPending = false,
      )
    dao.upsert(entity)

    if (currentUid != null && isOnline()) {
      val cloudResult =
        firestoreRepo.upsertLink(
          linkId = entity.id,
          headline = entity.headline,
          url = entity.url,
          normalizedKey = entity.normalizedKey,
        )
      if (cloudResult.isSuccess) {
        dao.markSynced(entity.id, currentUid)
        return Result.success(entity.copy(isSynced = true, syncPending = false, ownerUid = currentUid))
      }
    }
    return Result.success(entity)
  }

  suspend fun deleteLink(entity: NewsLinkEntity): Result<Unit> {
    val currentUid = currentUserUidOrNull()
    if (currentUid != null && entity.isSynced) {
      if (isOnline()) {
        val remoteRes = firestoreRepo.deleteLink(entity.id)
        if (remoteRes.isSuccess) {
          dao.deleteHardById(entity.id)
          return Result.success(Unit)
        }
      }
      dao.markDeletedPending(entity.id)
      return Result.success(Unit)
    } else {
      dao.deleteHardById(entity.id)
      return Result.success(Unit)
    }
  }

  suspend fun syncWithCloud(uid: String): Result<SyncSummary> {
    if (!isOnline()) {
      return Result.failure(
        IllegalStateException("ইন্টারনেট সংযোগ নেই। অনলাইনে এলে অটোমেটিক ক্লাউডে সিঙ্ক হবে।")
      )
    }
    return try {
      // 1. Push pending deletes first
      val pendingDeletes = dao.getPendingDeletes()
      for (item in pendingDeletes) {
        val delRes = firestoreRepo.deleteLink(item.id)
        if (delRes.isSuccess) {
          dao.deleteHardById(item.id)
        }
      }

      // 2. Fetch current cloud records for this authenticated user
      val cloudList = firestoreRepo.fetchAllLinksOnce(uid).getOrThrow()
      val cloudByKey = cloudList.associateBy { it.normalizedKey }

      // 3. Push local active items that aren't synced yet or missing in cloud (avoid duplicates)
      val localActive = dao.getActiveLinksOnce()
      var uploadedCount = 0
      for (local in localActive) {
        val matchingCloud = cloudByKey[local.normalizedKey]
        if (matchingCloud == null) {
          val upsertRes =
            firestoreRepo.upsertLink(
              linkId = local.id,
              headline = local.headline,
              url = local.url,
              normalizedKey = local.normalizedKey,
            )
          if (upsertRes.isSuccess) {
            dao.markSynced(local.id, uid)
            uploadedCount++
          } else {
            return Result.failure(
              upsertRes.exceptionOrNull() ?: IllegalStateException("ক্লাউডে আপলোড ব্যর্থ হয়েছে।")
            )
          }
        } else if (local.syncPending) {
          val upsertRes =
            firestoreRepo.upsertLink(
              linkId = matchingCloud.id,
              headline = local.headline,
              url = local.url,
              normalizedKey = local.normalizedKey,
            )
          if (upsertRes.isSuccess) {
            dao.markSynced(local.id, uid)
          }
        } else {
          dao.markSynced(local.id, uid)
        }
      }

      // 4. Restore any cloud items missing locally (e.g., after app reinstall or new phone)
      val latestCloud = firestoreRepo.fetchAllLinksOnce(uid).getOrThrow()
      val restoredCount = mergeCloudLinksIntoLocal(uid, latestCloud)

      Result.success(SyncSummary(uploaded = uploadedCount, restored = restoredCount))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Result.failure(
        IllegalStateException("ক্লাউড সিঙ্ক করতে সমস্যা হয়েছে: ${e.localizedMessage ?: "অজানা ত্রুটি"}")
      )
    }
  }

  suspend fun observeAndMergeCloudRealtime(uid: String) {
    firestoreRepo.observeUserLinks(uid).collectLatest { cloudItems ->
      mergeCloudLinksIntoLocal(uid, cloudItems)
    }
  }

  private suspend fun mergeCloudLinksIntoLocal(
    uid: String,
    cloudItems: List<CloudNewsLink>,
  ): Int {
    val existingLocal = dao.getActiveLinksOnce()
    val localByKey = existingLocal.associateBy { it.normalizedKey }
    val toInsert = mutableListOf<NewsLinkEntity>()
    var restoredCount = 0

    for (cloud in cloudItems) {
      val localMatch = localByKey[cloud.normalizedKey] ?: dao.findById(cloud.id)
      val cloudUpdatedMillis = cloud.updatedAt?.toDate()?.time ?: System.currentTimeMillis()
      val cloudCreatedMillis = cloud.createdAt?.toDate()?.time ?: cloudUpdatedMillis

      if (localMatch == null) {
        toInsert +=
          NewsLinkEntity(
            id = cloud.id,
            headline = cloud.headline,
            url = cloud.url,
            normalizedKey = cloud.normalizedKey,
            createdAtMillis = cloudCreatedMillis,
            updatedAtMillis = cloudUpdatedMillis,
            isSynced = true,
            syncPending = false,
            ownerUid = uid,
            isDeletedPending = false,
          )
        restoredCount++
      } else if (!localMatch.isDeletedPending && !localMatch.syncPending) {
        if (
          localMatch.headline != cloud.headline ||
            localMatch.url != cloud.url ||
            !localMatch.isSynced
        ) {
          toInsert +=
            localMatch.copy(
              headline = cloud.headline,
              url = cloud.url,
              isSynced = true,
              syncPending = false,
              ownerUid = uid,
              updatedAtMillis = maxOf(localMatch.updatedAtMillis, cloudUpdatedMillis),
            )
        }
      }
    }
    if (toInsert.isNotEmpty()) {
      dao.upsertAll(toInsert)
    }
    return restoredCount
  }

  data class SyncSummary(val uploaded: Int, val restored: Int)
}
