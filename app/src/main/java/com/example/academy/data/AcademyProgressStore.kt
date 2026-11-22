package com.example.academy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.academyDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "bsn_academy_progress")

data class AcademyProgressState(
  val completedChapterIds: Set<String> = emptySet(),
  val bookmarkedLessons: List<String> = emptyList(), // "subjectId|chapterId"
  val recentLessons: List<String> = emptyList(), // "subjectId|chapterId"
  val lastReadLesson: String? = null,
  val completedPracticalIds: Set<String> = emptySet(),
  val quizScores: Map<String, String> = emptyMap(), // key -> "score/total"
  val searchHistory: List<String> = emptyList(),
)

class AcademyProgressStore(private val context: Context) {
  private object Keys {
    val COMPLETED_CHAPTERS = stringSetPreferencesKey("completed_chapters")
    val BOOKMARKS_SERIALIZED = stringPreferencesKey("bookmarked_lessons_list")
    val RECENT_SERIALIZED = stringPreferencesKey("recent_lessons_list")
    val LAST_READ = stringPreferencesKey("last_read_lesson")
    val COMPLETED_PRACTICALS = stringSetPreferencesKey("completed_practicals")
    val QUIZ_SCORES_SERIALIZED = stringPreferencesKey("quiz_scores_map")
    val SEARCH_HISTORY_SERIALIZED = stringPreferencesKey("search_history_list")
  }

  val progressFlow: Flow<AcademyProgressState> =
    context.academyDataStore.data.map { prefs ->
      AcademyProgressState(
        completedChapterIds = prefs[Keys.COMPLETED_CHAPTERS] ?: emptySet(),
        bookmarkedLessons = decodeList(prefs[Keys.BOOKMARKS_SERIALIZED]),
        recentLessons = decodeList(prefs[Keys.RECENT_SERIALIZED]),
        lastReadLesson = prefs[Keys.LAST_READ]?.takeIf { it.isNotBlank() },
        completedPracticalIds = prefs[Keys.COMPLETED_PRACTICALS] ?: emptySet(),
        quizScores = decodeMap(prefs[Keys.QUIZ_SCORES_SERIALIZED]),
        searchHistory = decodeList(prefs[Keys.SEARCH_HISTORY_SERIALIZED]),
      )
    }

  suspend fun toggleChapterCompleted(chapterId: String) {
    context.academyDataStore.edit { prefs ->
      val current = prefs[Keys.COMPLETED_CHAPTERS]?.toMutableSet() ?: mutableSetOf()
      if (!current.add(chapterId)) {
        current.remove(chapterId)
      }
      prefs[Keys.COMPLETED_CHAPTERS] = current
    }
  }

  suspend fun toggleBookmark(subjectId: String, chapterId: String) {
    val token = "$subjectId|$chapterId"
    context.academyDataStore.edit { prefs ->
      val current = decodeList(prefs[Keys.BOOKMARKS_SERIALIZED]).toMutableList()
      if (current.contains(token)) {
        current.remove(token)
      } else {
        current.add(0, token)
      }
      prefs[Keys.BOOKMARKS_SERIALIZED] = encodeList(current.take(30))
    }
  }

  suspend fun recordLessonOpened(subjectId: String, chapterId: String) {
    val token = "$subjectId|$chapterId"
    context.academyDataStore.edit { prefs ->
      prefs[Keys.LAST_READ] = token
      val current = decodeList(prefs[Keys.RECENT_SERIALIZED]).toMutableList()
      current.remove(token)
      current.add(0, token)
      prefs[Keys.RECENT_SERIALIZED] = encodeList(current.take(12))
    }
  }

  suspend fun togglePracticalCompleted(practicalId: String) {
    context.academyDataStore.edit { prefs ->
      val current = prefs[Keys.COMPLETED_PRACTICALS]?.toMutableSet() ?: mutableSetOf()
      if (!current.add(practicalId)) {
        current.remove(practicalId)
      }
      prefs[Keys.COMPLETED_PRACTICALS] = current
    }
  }

  suspend fun saveQuizResult(quizKey: String, score: Int, total: Int) {
    context.academyDataStore.edit { prefs ->
      val map = decodeMap(prefs[Keys.QUIZ_SCORES_SERIALIZED]).toMutableMap()
      map[quizKey] = "$score/$total"
      prefs[Keys.QUIZ_SCORES_SERIALIZED] = encodeMap(map)
    }
  }

  suspend fun addSearchQuery(query: String) {
    val trimmed = query.trim()
    if (trimmed.length < 2) return
    context.academyDataStore.edit { prefs ->
      val list = decodeList(prefs[Keys.SEARCH_HISTORY_SERIALIZED]).toMutableList()
      list.removeAll { it.equals(trimmed, ignoreCase = true) }
      list.add(0, trimmed)
      prefs[Keys.SEARCH_HISTORY_SERIALIZED] = encodeList(list.take(8))
    }
  }

  suspend fun clearSearchHistory() {
    context.academyDataStore.edit { prefs ->
      prefs.remove(Keys.SEARCH_HISTORY_SERIALIZED)
    }
  }

  private fun encodeList(items: List<String>): String =
    items.joinToString("\n")

  private fun decodeList(raw: String?): List<String> =
    raw?.split("\n")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

  private fun encodeMap(map: Map<String, String>): String =
    map.entries.joinToString("\n") { "${it.key}=${it.value}" }

  private fun decodeMap(raw: String?): Map<String, String> =
    raw
      ?.split("\n")
      ?.mapNotNull { line ->
        val idx = line.indexOf('=')
        if (idx > 0) line.substring(0, idx) to line.substring(idx + 1) else null
      }
      ?.toMap() ?: emptyMap()
}
