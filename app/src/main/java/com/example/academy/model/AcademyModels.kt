package com.example.academy.model

enum class ContentStatus(val badgeBn: String) {
  COMPLETED("সম্পূর্ণ"),
  IN_PROGRESS("উন্নয়নাধীন"),
  PLANNED("পরিকল্পিত"),
}

data class EnglishTerm(
  val termEn: String,
  val meaningBn: String,
  val usageExampleBn: String,
)

data class LessonQAndA(
  val id: String,
  val questionBn: String,
  val answerBn: String,
  val isDescriptive: Boolean = false,
)

data class AcademyMcq(
  val id: String,
  val questionBn: String,
  val optionsBn: List<String>,
  val correctIndex: Int,
  val explanationBn: String,
)

data class ChapterLesson(
  val id: String,
  val chapterNumber: Int,
  val titleBn: String,
  val titleEn: String,
  val banglaExplanation: String,
  val definitions: List<Pair<String, String>>,
  val englishTerms: List<EnglishTerm>,
  val practicalExamples: List<String>,
  val caseStudyBn: String,
  val importantNotes: List<String>,
  val shortQAs: List<LessonQAndA>,
  val descriptiveQAs: List<LessonQAndA>,
  val mcqs: List<AcademyMcq>,
  val practicalExerciseBn: String,
  val assignmentBn: String,
  val revisionPointsBn: List<String>,
)

data class AcademySubject(
  val id: String,
  val code: String,
  val year: Int,
  val semester: Int,
  val titleEn: String,
  val titleBn: String,
  val introBn: String,
  val learningObjectivesBn: List<String>,
  val chapters: List<ChapterLesson>,
  val status: ContentStatus,
  val academicReferenceNote: String,
)

data class AcademySemester(
  val year: Int,
  val semesterNumber: Int,
  val titleEn: String,
  val titleBn: String,
  val goalBn: String,
)

data class AcademyYearInfo(
  val year: Int,
  val titleEn: String,
  val titleBn: String,
  val overviewBn: String,
)

data class PracticalExerciseModule(
  val id: String,
  val categoryBn: String,
  val difficultyBn: String,
  val titleBn: String,
  val isFictionalPracticeScenario: Boolean = true,
  val scenarioBn: String,
  val stepsBn: List<String>,
  val sampleOutputGuideBn: String,
  val ethicsCheckBn: String,
)

data class BdIntlJournalismModule(
  val id: String,
  val badgeBn: String,
  val titleBn: String,
  val overviewBn: String,
  val legalJurisdictionDateNote: String? = null,
  val keyFactsBn: List<String>,
  val reportingGuideBn: List<String>,
)

data class EthicsSafetyModule(
  val id: String,
  val badgeBn: String,
  val titleBn: String,
  val principleBn: String,
  val mustDoBn: List<String>,
  val avoidStrictlyBn: List<String>,
)

data class YearRoadmapPlan(
  val year: Int,
  val titleBn: String,
  val annualObjectiveBn: String,
  val semester1GoalBn: String,
  val semester2GoalBn: String,
  val weeklyStudyGuidanceBn: String,
  val readingScheduleBn: String,
  val practicalScheduleBn: String,
  val revisionScheduleBn: String,
  val completionCompetenciesBn: List<String>,
)

data class AcademicReferenceEntry(
  val categoryBn: String,
  val title: String,
  val detailsBn: String,
)

enum class SearchHitType(val labelBn: String) {
  LESSON("অধ্যায় পাঠ"),
  TERMINOLOGY("ইংরেজি পরিভাষা"),
  DEFINITION("সংজ্ঞা ও ধারণা"),
  QUESTION_ANSWER("প্রশ্নোত্তর"),
  MCQ("BSN নমুনা MCQ"),
  EXERCISE("ব্যবহারিক অনুশীলন"),
  ASSIGNMENT("অ্যাসাইনমেন্ট"),
  NOTE("গুরুত্বপূর্ণ নোট"),
}

data class AcademySearchHit(
  val id: String,
  val subjectId: String,
  val chapterId: String,
  val year: Int,
  val semester: Int,
  val hitType: SearchHitType,
  val titleBn: String,
  val subtitleBn: String,
  val snippetBn: String,
  val targetMcqId: String? = null,
)
