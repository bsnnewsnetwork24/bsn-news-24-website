package com.example.data.repository

import com.example.data.local.LocalNewsLinksStore
import com.example.data.local.NewsLinkEntity
import kotlinx.coroutines.flow.Flow

class NewsLinksRepository(
  private val store: LocalNewsLinksStore,
) {
  fun observeLocalLinks(): Flow<List<NewsLinkEntity>> = store.linksFlow

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

    if (existingId == null) {
      val duplicate = store.findByNormalizedKey(key)
      if (duplicate != null) {
        return Result.failure(IllegalStateException("এই নিউজ লিংকটি ইতিমধ্যেই সংরক্ষিত আছে।"))
      }
    }

    val existing = existingId?.let { store.findById(it) }
    val docId = existing?.id ?: "${key}_${now}"

    val entity =
      NewsLinkEntity(
        id = docId,
        headline = cleanHeadline,
        url = cleanUrl,
        normalizedKey = key,
        createdAtMillis = existing?.createdAtMillis ?: now,
        updatedAtMillis = now,
      )
    store.upsert(entity)
    return Result.success(entity)
  }

  suspend fun deleteLink(entity: NewsLinkEntity): Result<Unit> {
    store.deleteById(entity.id)
    return Result.success(Unit)
  }
}
