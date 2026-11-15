package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest

@Entity(tableName = "news_links")
data class NewsLinkEntity(
  @PrimaryKey val id: String,
  val headline: String,
  val url: String,
  val normalizedKey: String,
  val createdAtMillis: Long,
  val updatedAtMillis: Long,
  val isSynced: Boolean = false,
  val syncPending: Boolean = true,
  val ownerUid: String? = null,
  val isDeletedPending: Boolean = false,
) {
  companion object {
    fun normalizeUrl(rawUrl: String): String {
      val trimmed = rawUrl.trim()
      if (trimmed.isEmpty()) return ""
      return if (
        trimmed.startsWith("http://", ignoreCase = true) ||
          trimmed.startsWith("https://", ignoreCase = true)
      ) {
        trimmed
      } else {
        "https://$trimmed"
      }
    }

    fun computeNormalizedKey(headline: String, url: String): String {
      val canonical =
        "${normalizeUrl(url).lowercase().trimEnd('/')}|${headline.trim().lowercase()}"
      val digest = MessageDigest.getInstance("SHA-256").digest(canonical.toByteArray(Charsets.UTF_8))
      return "lnk_" + digest.take(16).joinToString("") { "%02x".format(it) }
    }
  }
}
