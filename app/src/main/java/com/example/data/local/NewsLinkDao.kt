package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsLinkDao {
  @Query("SELECT * FROM news_links WHERE isDeletedPending = 0 ORDER BY updatedAtMillis DESC")
  fun observeActiveLinks(): Flow<List<NewsLinkEntity>>

  @Query("SELECT * FROM news_links WHERE isDeletedPending = 0 ORDER BY updatedAtMillis DESC")
  suspend fun getActiveLinksOnce(): List<NewsLinkEntity>

  @Query("SELECT * FROM news_links WHERE syncPending = 1 AND isDeletedPending = 0")
  suspend fun getPendingUpserts(): List<NewsLinkEntity>

  @Query("SELECT * FROM news_links WHERE isDeletedPending = 1")
  suspend fun getPendingDeletes(): List<NewsLinkEntity>

  @Query("SELECT * FROM news_links WHERE normalizedKey = :key LIMIT 1")
  suspend fun findByNormalizedKey(key: String): NewsLinkEntity?

  @Query("SELECT * FROM news_links WHERE id = :id LIMIT 1")
  suspend fun findById(id: String): NewsLinkEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(entity: NewsLinkEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertAll(entities: List<NewsLinkEntity>)

  @Query("DELETE FROM news_links WHERE id = :id")
  suspend fun deleteHardById(id: String)

  @Query("UPDATE news_links SET isDeletedPending = 1, syncPending = 1, updatedAtMillis = :now WHERE id = :id")
  suspend fun markDeletedPending(id: String, now: Long = System.currentTimeMillis())

  @Query("UPDATE news_links SET isSynced = 1, syncPending = 0, ownerUid = :uid WHERE id = :id")
  suspend fun markSynced(id: String, uid: String)
}
