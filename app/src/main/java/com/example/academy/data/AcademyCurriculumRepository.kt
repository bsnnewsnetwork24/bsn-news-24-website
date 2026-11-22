package com.example.academy.data

import com.example.academy.model.AcademySearchHit
import com.example.academy.model.AcademySemester
import com.example.academy.model.AcademySubject
import com.example.academy.model.AcademyYearInfo
import com.example.academy.model.ContentStatus
import com.example.academy.model.SearchHitType

object AcademyCurriculumRepository {
  val years: List<AcademyYearInfo> = AcademyOverviewData.years
  val semesters: List<AcademySemester> = AcademyOverviewData.semesters

  val allSubjects: List<AcademySubject> by lazy {
    Year1Semester1Data.subjects +
      Year1Semester2Data.subjects +
      Year2Semester3Data.subjects +
      Year2Semester4Data.subjects +
      Year3Semester5Data.subjects +
      Year3ExtendedSubjects.semester5AudioVideoSeparate +
      Year3Semester6Data.subjects +
      Year3ExtendedSubjects.semester6DetailedSplitSubjects +
      Year4Semester7Data.subjects +
      Year4Semester8Data.subjects
  }

  fun subjectsForYear(year: Int): List<AcademySubject> =
    allSubjects.filter { it.year == year }

  fun subjectsForSemester(year: Int, semester: Int): List<AcademySubject> =
    allSubjects.filter { it.year == year && it.semester == semester }

  fun findSubject(subjectId: String): AcademySubject? =
    allSubjects.firstOrNull { it.id == subjectId }

  fun totalChapterCount(): Int = allSubjects.sumOf { it.chapters.size }

  fun completedSubjectCount(): Int =
    allSubjects.count { it.status == ContentStatus.COMPLETED }

  fun totalMcqCount(): Int =
    allSubjects.sumOf { s -> s.chapters.sumOf { c -> c.mcqs.size } }

  /**
   * Normalizes English and common Bangla spelling variations so beginner students can search
   * across lessons, English terms, definitions, Q&A, MCQs, exercises, and assignments offline.
   */
  fun normalizeSearchText(raw: String): String {
    val lowered =
      raw
        .trim()
        .lowercase()
        .replace("\u09BC", "") // Normalizes composite য়/ড়/ঢ় -> য/ড/ঢ cleanly
        .replace("য়", "য")
        .replace("ড়", "ড")
        .replace("ঢ়", "ঢ")
    if (lowered.isEmpty()) return ""
    val sb = StringBuilder(lowered.length)
    for (ch in lowered) {
      when (ch) {
        'ী' -> sb.append('ি')
        'ূ' -> sb.append('ু')
        'ৌ' -> sb.append('ো')
        'ৈ' -> sb.append('ে')
        'শ', 'ষ' -> sb.append('স')
        'ণ', 'ঞ' -> sb.append('ন')
        'য' -> sb.append('জ')
        'ড', 'ঢ', 'র' -> sb.append('র')
        'ং' -> sb.append('ঙ')
        'ঃ' -> Unit
        else -> sb.append(ch)
      }
    }
    return sb.toString()
      .replace(Regex("\\s+"), " ")
      .trim()
  }

  fun search(query: String): List<AcademySearchHit> {
    val normQuery = normalizeSearchText(query)
    if (normQuery.length < 2) return emptyList()
    val tokens = normQuery.split(" ").filter { it.isNotBlank() }
    val results = mutableListOf<AcademySearchHit>()

    fun matchesAll(vararg fields: String): Boolean {
      val combined = normalizeSearchText(fields.joinToString(" "))
      return tokens.all { combined.contains(it) }
    }

    for (subject in allSubjects) {
      for (chapter in subject.chapters) {
        // 1. Lesson content & overview
        if (
          matchesAll(
            subject.titleBn,
            subject.titleEn,
            subject.introBn,
            chapter.titleBn,
            chapter.titleEn,
            chapter.banglaExplanation,
            chapter.caseStudyBn,
          )
        ) {
          results +=
            AcademySearchHit(
              id = "hit_lesson_${chapter.id}",
              subjectId = subject.id,
              chapterId = chapter.id,
              year = subject.year,
              semester = subject.semester,
              hitType = SearchHitType.LESSON,
              titleBn = "${subject.titleBn}: ${chapter.titleBn}",
              subtitleBn = "বর্ষ ${subject.year} • সেমিস্টার ${subject.semester} • ${chapter.titleEn}",
              snippetBn = chapter.banglaExplanation.take(120) + "...",
            )
        }

        // 2. English Terminology + Bangla meaning
        for (term in chapter.englishTerms) {
          if (matchesAll(term.termEn, term.meaningBn, term.usageExampleBn, subject.titleBn)) {
            results +=
              AcademySearchHit(
                id = "hit_term_${chapter.id}_${term.termEn}",
                subjectId = subject.id,
                chapterId = chapter.id,
                year = subject.year,
                semester = subject.semester,
                hitType = SearchHitType.TERMINOLOGY,
                titleBn = "${term.termEn} — ${term.meaningBn}",
                subtitleBn = "${subject.titleBn} • ${chapter.titleBn}",
                snippetBn = term.usageExampleBn,
              )
          }
        }

        // 3. Definitions
        for ((defTitle, defBody) in chapter.definitions) {
          if (matchesAll(defTitle, defBody, subject.titleBn)) {
            results +=
              AcademySearchHit(
                id = "hit_def_${chapter.id}_${defTitle.hashCode()}",
                subjectId = subject.id,
                chapterId = chapter.id,
                year = subject.year,
                semester = subject.semester,
                hitType = SearchHitType.DEFINITION,
                titleBn = "সংজ্ঞা: $defTitle",
                subtitleBn = "${subject.titleBn} • ${chapter.titleBn}",
                snippetBn = defBody,
              )
          }
        }

        // 4. Short & Descriptive Q&As
        for (qa in chapter.shortQAs + chapter.descriptiveQAs) {
          if (matchesAll(qa.questionBn, qa.answerBn, subject.titleBn)) {
            results +=
              AcademySearchHit(
                id = "hit_qa_${qa.id}",
                subjectId = subject.id,
                chapterId = chapter.id,
                year = subject.year,
                semester = subject.semester,
                hitType = SearchHitType.QUESTION_ANSWER,
                titleBn = qa.questionBn,
                subtitleBn = "${subject.titleBn} • প্রশ্নোত্তর",
                snippetBn = qa.answerBn.take(120) + "...",
              )
          }
        }

        // 5. MCQs
        for (mcq in chapter.mcqs) {
          if (
            matchesAll(
              mcq.questionBn,
              mcq.explanationBn,
              mcq.optionsBn.joinToString(" "),
              subject.titleBn,
            )
          ) {
            results +=
              AcademySearchHit(
                id = "hit_mcq_${mcq.id}",
                subjectId = subject.id,
                chapterId = chapter.id,
                year = subject.year,
                semester = subject.semester,
                hitType = SearchHitType.MCQ,
                titleBn = mcq.questionBn,
                subtitleBn = "${subject.titleBn} • BSN নমুনা প্রশ্ন",
                snippetBn = mcq.explanationBn,
                targetMcqId = mcq.id,
              )
          }
        }

        // 6. Notes, Exercises & Assignments
        if (matchesAll(chapter.importantNotes.joinToString(" "), subject.titleBn)) {
          results +=
            AcademySearchHit(
              id = "hit_note_${chapter.id}",
              subjectId = subject.id,
              chapterId = chapter.id,
              year = subject.year,
              semester = subject.semester,
              hitType = SearchHitType.NOTE,
              titleBn = "গুরুত্বপূর্ণ নোট: ${chapter.titleBn}",
              subtitleBn = subject.titleBn,
              snippetBn = chapter.importantNotes.firstOrNull().orEmpty(),
            )
        }
        if (matchesAll(chapter.practicalExerciseBn, subject.titleBn)) {
          results +=
            AcademySearchHit(
              id = "hit_ex_${chapter.id}",
              subjectId = subject.id,
              chapterId = chapter.id,
              year = subject.year,
              semester = subject.semester,
              hitType = SearchHitType.EXERCISE,
              titleBn = "ব্যবহারিক অনুশীলন: ${subject.titleBn}",
              subtitleBn = chapter.titleBn,
              snippetBn = chapter.practicalExerciseBn.take(120) + "...",
            )
        }
        if (matchesAll(chapter.assignmentBn, subject.titleBn)) {
          results +=
            AcademySearchHit(
              id = "hit_asg_${chapter.id}",
              subjectId = subject.id,
              chapterId = chapter.id,
              year = subject.year,
              semester = subject.semester,
              hitType = SearchHitType.ASSIGNMENT,
              titleBn = "অ্যাসাইনমেন্ট: ${subject.titleBn}",
              subtitleBn = chapter.titleBn,
              snippetBn = chapter.assignmentBn.take(120) + "...",
            )
        }
      }
    }

    return results.distinctBy { "${it.hitType}_${it.subjectId}_${it.chapterId}_${it.titleBn}" }
      .take(40)
  }
}
