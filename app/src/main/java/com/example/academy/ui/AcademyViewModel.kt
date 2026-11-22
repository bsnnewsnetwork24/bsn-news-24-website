package com.example.academy.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.academy.data.AcademyCurriculumRepository
import com.example.academy.data.AcademyProgressState
import com.example.academy.data.AcademyProgressStore
import com.example.academy.model.AcademySearchHit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AcademySectionTab(val labelBn: String) {
  CURRICULUM("পাঠ্যক্রম"),
  PRACTICAL("ব্যবহারিক"),
  BD_INTL("দেশ ও বিশ্ব"),
  ETHICS("নৈতিকতা ও সুরক্ষা"),
  ROADMAP("৪ বছরের রোডম্যাপ"),
  EXAMS("BSN পরীক্ষা"),
  SOURCES("তথ্যসূত্র ও সততা"),
}

sealed interface AcademyRoute {
  data object Hub : AcademyRoute
  data class SubjectDetail(
    val subjectId: String,
    val initialChapterId: String? = null,
    val highlightMcqId: String? = null,
  ) : AcademyRoute
}

class AcademyViewModel(private val progressStore: AcademyProgressStore) : ViewModel() {
  val progressState: StateFlow<AcademyProgressState> =
    progressStore.progressFlow.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = AcademyProgressState(),
    )

  private val _selectedSection = MutableStateFlow(AcademySectionTab.CURRICULUM)
  val selectedSection: StateFlow<AcademySectionTab> = _selectedSection.asStateFlow()

  private val _selectedYear = MutableStateFlow(1)
  val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

  private val _selectedSemester = MutableStateFlow(1)
  val selectedSemester: StateFlow<Int> = _selectedSemester.asStateFlow()

  private val _route = MutableStateFlow<AcademyRoute>(AcademyRoute.Hub)
  val route: StateFlow<AcademyRoute> = _route.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _searchResults = MutableStateFlow<List<AcademySearchHit>>(emptyList())
  val searchResults: StateFlow<List<AcademySearchHit>> = _searchResults.asStateFlow()

  fun selectSection(section: AcademySectionTab) {
    _selectedSection.value = section
    _route.value = AcademyRoute.Hub
  }

  fun selectYear(year: Int) {
    _selectedYear.value = year
    val validSemesters =
      AcademyCurriculumRepository.semesters.filter { it.year == year }.map { it.semesterNumber }
    if (_selectedSemester.value !in validSemesters && validSemesters.isNotEmpty()) {
      _selectedSemester.value = validSemesters.first()
    }
  }

  fun selectSemester(semester: Int) {
    _selectedSemester.value = semester
  }

  fun openSubject(
    subjectId: String,
    chapterId: String? = null,
    highlightMcqId: String? = null,
  ) {
    val subject = AcademyCurriculumRepository.findSubject(subjectId) ?: return
    val resolvedChapter = chapterId ?: subject.chapters.firstOrNull()?.id
    _route.value =
      AcademyRoute.SubjectDetail(
        subjectId = subject.id,
        initialChapterId = resolvedChapter,
        highlightMcqId = highlightMcqId,
      )
    if (resolvedChapter != null) {
      viewModelScope.launch { progressStore.recordLessonOpened(subject.id, resolvedChapter) }
    }
  }

  fun recordChapterActive(subjectId: String, chapterId: String) {
    viewModelScope.launch { progressStore.recordLessonOpened(subjectId, chapterId) }
  }

  fun navigateBackToHub(): Boolean {
    return if (_route.value != AcademyRoute.Hub) {
      _route.value = AcademyRoute.Hub
      true
    } else {
      false
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    _searchResults.value = AcademyCurriculumRepository.search(query)
  }

  fun commitSearchQuery(query: String) {
    val clean = query.trim()
    updateSearchQuery(clean)
    if (clean.length >= 2) {
      viewModelScope.launch { progressStore.addSearchQuery(clean) }
    }
  }

  fun clearSearchHistory() {
    viewModelScope.launch { progressStore.clearSearchHistory() }
  }

  fun toggleChapterCompleted(chapterId: String) {
    viewModelScope.launch { progressStore.toggleChapterCompleted(chapterId) }
  }

  fun toggleBookmark(subjectId: String, chapterId: String) {
    viewModelScope.launch { progressStore.toggleBookmark(subjectId, chapterId) }
  }

  fun togglePracticalCompleted(practicalId: String) {
    viewModelScope.launch { progressStore.togglePracticalCompleted(practicalId) }
  }

  fun saveQuizScore(quizKey: String, score: Int, total: Int) {
    viewModelScope.launch { progressStore.saveQuizResult(quizKey, score, total) }
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return AcademyViewModel(AcademyProgressStore(context.applicationContext)) as T
    }
  }
}
