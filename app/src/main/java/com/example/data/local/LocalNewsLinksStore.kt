package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.newsLinksDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "bsn_offline_news_links")

class LocalNewsLinksStore(private val context: Context) {

  private object Keys {
    val LINKS_JSON = stringPreferencesKey("saved_links_json")
  }

  val linksFlow: Flow<List<NewsLinkEntity>> =
    context.newsLinksDataStore.data.map { prefs ->
      parseJson(prefs[Keys.LINKS_JSON].orEmpty()).sortedByDescending { it.updatedAtMillis }
    }

  suspend fun getLinksOnce(): List<NewsLinkEntity> {
    val prefs = context.newsLinksDataStore.data.first()
    return parseJson(prefs[Keys.LINKS_JSON].orEmpty()).sortedByDescending { it.updatedAtMillis }
  }

  suspend fun findById(id: String): NewsLinkEntity? =
    getLinksOnce().firstOrNull { it.id == id }

  suspend fun findByNormalizedKey(key: String): NewsLinkEntity? =
    getLinksOnce().firstOrNull { it.normalizedKey == key }

  suspend fun upsert(entity: NewsLinkEntity) {
    context.newsLinksDataStore.edit { prefs ->
      val current = parseJson(prefs[Keys.LINKS_JSON].orEmpty()).toMutableList()
      val idx = current.indexOfFirst { it.id == entity.id }
      if (idx >= 0) {
        current[idx] = entity
      } else {
        current.add(entity)
      }
      prefs[Keys.LINKS_JSON] = serializeJson(current)
    }
  }

  suspend fun deleteById(id: String) {
    context.newsLinksDataStore.edit { prefs ->
      val updated = parseJson(prefs[Keys.LINKS_JSON].orEmpty()).filterNot { it.id == id }
      prefs[Keys.LINKS_JSON] = serializeJson(updated)
    }
  }

  private fun parseJson(raw: String): List<NewsLinkEntity> {
    if (raw.isBlank()) return emptyList()
    return runCatching {
      val arr = JSONArray(raw)
      buildList(arr.length()) {
        for (i in 0 until arr.length()) {
          val obj = arr.optJSONObject(i) ?: continue
          val id = obj.optString("id")
          val headline = obj.optString("headline")
          val url = obj.optString("url")
          val normalizedKey = obj.optString("normalizedKey")
          val createdAtMillis = obj.optLong("createdAtMillis", 0L)
          val updatedAtMillis = obj.optLong("updatedAtMillis", createdAtMillis)
          if (id.isNotBlank() && headline.isNotBlank() && url.isNotBlank()) {
            add(
              NewsLinkEntity(
                id = id,
                headline = headline,
                url = url,
                normalizedKey = normalizedKey.ifBlank {
                  NewsLinkEntity.computeNormalizedKey(headline, url)
                },
                createdAtMillis = createdAtMillis,
                updatedAtMillis = updatedAtMillis,
              )
            )
          }
        }
      }
    }.getOrDefault(emptyList())
  }

  private fun serializeJson(items: List<NewsLinkEntity>): String {
    val arr = JSONArray()
    for (item in items) {
      val obj =
        JSONObject()
          .put("id", item.id)
          .put("headline", item.headline)
          .put("url", item.url)
          .put("normalizedKey", item.normalizedKey)
          .put("createdAtMillis", item.createdAtMillis)
          .put("updatedAtMillis", item.updatedAtMillis)
      arr.put(obj)
    }
    return arr.toString()
  }
}
