package com.example

import com.example.academy.data.AcademyCurriculumRepository
import com.example.academy.data.AcademyOverviewData
import com.example.academy.data.AcademyTrainingData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AcademyCurriculumTest {
  @Test
  fun curriculum_hasFourYearsEightSemestersAndRealContent() {
    assertEquals(4, AcademyCurriculumRepository.years.size)
    assertEquals(8, AcademyCurriculumRepository.semesters.size)
    assertTrue(AcademyCurriculumRepository.allSubjects.size >= 45)

    for (yr in 1..4) {
      val sems = AcademyCurriculumRepository.semesters.filter { it.year == yr }
      assertEquals("Year $yr should have 2 semesters", 2, sems.size)
      for (sem in sems) {
        val subs = AcademyCurriculumRepository.subjectsForSemester(yr, sem.semesterNumber)
        assertTrue("Semester ${sem.semesterNumber} should not be empty", subs.isNotEmpty())
      }
    }

    for (subject in AcademyCurriculumRepository.allSubjects) {
      assertTrue("${subject.id} intro blank", subject.introBn.isNotBlank())
      assertTrue("${subject.id} objectives empty", subject.learningObjectivesBn.isNotEmpty())
      assertTrue("${subject.id} chapters empty", subject.chapters.isNotEmpty())
      for (ch in subject.chapters) {
        assertTrue("${ch.id} explanation blank", ch.banglaExplanation.length >= 80)
        assertTrue("${ch.id} definitions empty", ch.definitions.isNotEmpty())
        assertTrue("${ch.id} englishTerms empty", ch.englishTerms.isNotEmpty())
        assertTrue("${ch.id} QAs empty", ch.shortQAs.isNotEmpty() && ch.descriptiveQAs.isNotEmpty())
        assertTrue("${ch.id} MCQs empty", ch.mcqs.isNotEmpty())
        assertTrue("${ch.id} exercise/assignment blank", ch.practicalExerciseBn.isNotBlank() && ch.assignmentBn.isNotBlank())
        for (mcq in ch.mcqs) {
          assertTrue("${mcq.id} options size", mcq.optionsBn.size >= 4)
          assertTrue("${mcq.id} valid answer index", mcq.correctIndex in mcq.optionsBn.indices)
          assertTrue("${mcq.id} explanation blank", mcq.explanationBn.isNotBlank())
        }
      }
    }
  }

  @Test
  fun smartSearch_matchesBanglaEnglishAndSpellingVariants() {
    val gatekeepingStd = AcademyCurriculumRepository.search("গেটকিপিং")
    val gatekeepingAlt = AcademyCurriculumRepository.search("গেটকীপিং")
    assertTrue(gatekeepingStd.isNotEmpty())
    assertTrue(gatekeepingAlt.isNotEmpty())

    val osintHits = AcademyCurriculumRepository.search("OSINT")
    assertTrue(osintHits.any { it.subjectId.contains("osint") })

    val rtiHits = AcademyCurriculumRepository.search("RTI")
    assertTrue(rtiHits.any { it.subjectId == "y4s7_media_law_press_freedom" })

    val noneHits = AcademyCurriculumRepository.search("zzzznonexistentkeyword")
    assertTrue(noneHits.isEmpty())
  }

  @Test
  fun trainingModules_andHonestyNotices_areComplete() {
    assertEquals(5, AcademyTrainingData.practicalExercises.size)
    assertEquals(4, AcademyTrainingData.bdIntlModules.size)
    assertEquals(3, AcademyTrainingData.ethicsSafetyModules.size)
    assertEquals(4, AcademyTrainingData.fourYearRoadmap.size)
    assertTrue(AcademyOverviewData.INDEPENDENT_PROGRAM_NOTICE_BN.contains("স্বাধীন"))
    assertFalse(AcademyOverviewData.academicReferences.isEmpty())
  }
}
