package com.example.academy.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academy.data.AcademyCurriculumRepository
import com.example.academy.data.AcademyOverviewData
import com.example.academy.data.AcademyProgressState
import com.example.academy.data.AcademyTrainingData
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily

fun LazyListScope.practicalLabItems(
  progress: AcademyProgressState,
  onTogglePractical: (String) -> Unit,
) {
  item {
    LessonSectionCard(
      title = "ব্যবহারিক সাংবাদিকতা ল্যাব (Progressive Practical Training)",
      accent = BsnGold,
    ) {
      Text(
        text =
          "সতর্কতা: নিচের অনুশীলনগুলোতে ব্যবহৃত পরিস্থিতিগুলো সম্পূর্ণ কাল্পনিক অনুশীলনের উদাহরণ—এগুলো কোনো প্রকৃত প্রকাশিত সংবাদ নয়। সম্পন্ন হওয়া ধাপগুলো চিহ্নিত করে আপনার অগ্রগতি সংরক্ষণ করুন।",
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
        lineHeight = 19.sp,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text =
          "সম্পন্ন অনুশীলন: ${progress.completedPracticalIds.size} / ${AcademyTrainingData.practicalExercises.size}",
        color = BsnGreen,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
      )
    }
  }

  AcademyTrainingData.practicalExercises.forEach { mod ->
    val done = progress.completedPracticalIds.contains(mod.id)
    item(key = mod.id) {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.dp, if (done) BsnGreen else BsnOutline),
      ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
          Text(
            text = "${mod.difficultyBn} • ${mod.categoryBn}",
            color = BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = mod.titleBn,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.5.sp,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = mod.scenarioBn,
            color = BsnTextSecondary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 13.sp,
            lineHeight = 19.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          mod.stepsBn.forEachIndexed { idx, step ->
            Text(
              text = "${idx + 1}. $step",
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
              lineHeight = 19.sp,
            )
            Spacer(modifier = Modifier.height(3.dp))
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "নমুনা দিকনির্দেশনা: ${mod.sampleOutputGuideBn}",
            color = BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 12.5.sp,
          )
          Text(
            text = "নৈতিক চেকপয়েন্ট: ${mod.ethicsCheckBn}",
            color = BsnGreen,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 12.5.sp,
          )
        }
      }
    }
    item(key = "${mod.id}_btn") {
      Button(
        onClick = { onTogglePractical(mod.id) },
        colors =
          ButtonDefaults.buttonColors(
            containerColor = if (done) BsnGreen else BsnRed,
            contentColor = Color.White,
          ),
        modifier = Modifier.fillMaxWidth().testTag("practical_btn_${mod.id}"),
      ) {
        Icon(
          imageVector = if (done) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (done) "অনুশীলন সম্পন্ন হয়েছে ✓" else "অনুশীলন সম্পন্ন চিহ্নিত করুন",
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}

fun LazyListScope.bdIntlItems() {
  items(AcademyTrainingData.bdIntlModules, key = { it.id }) { mod ->
    LessonSectionCard(title = "[${mod.badgeBn}] ${mod.titleBn}", accent = BsnGold) {
      Text(
        text = mod.overviewBn,
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
      )
      if (!mod.legalJurisdictionDateNote.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
          modifier =
            Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(BsnRed.copy(alpha = 0.16f))
              .border(1.dp, BsnRed.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
              .padding(9.dp)
        ) {
          Text(
            text = mod.legalJurisdictionDateNote,
            color = BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 12.sp,
            lineHeight = 18.sp,
          )
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
      mod.keyFactsBn.forEach { fact ->
        Text(
          text = "• $fact",
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontSize = 13.sp,
          lineHeight = 19.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "মাঠ নির্দেশিকা:",
        color = BsnGreen,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
      )
      mod.reportingGuideBn.forEach { g ->
        Text(
          text = "✓ $g",
          color = BsnTextSecondary,
          fontFamily = HindSiliguriFontFamily,
          fontSize = 12.5.sp,
        )
      }
    }
  }
}

fun LazyListScope.ethicsSafetyItems() {
  items(AcademyTrainingData.ethicsSafetyModules, key = { it.id }) { mod ->
    LessonSectionCard(title = "[${mod.badgeBn}] ${mod.titleBn}", accent = BsnRed) {
      Text(
        text = "মূল নীতি: ${mod.principleBn}",
        color = BsnGold,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "অবশ্যই করণীয়:",
        color = BsnGreen,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
      )
      mod.mustDoBn.forEach { item ->
        Text(
          text = "✓ $item",
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontSize = 13.sp,
          lineHeight = 19.sp,
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "কঠোরভাবে বর্জনীয়:",
        color = BsnRed,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
      )
      mod.avoidStrictlyBn.forEach { bad ->
        Text(
          text = "✕ $bad",
          color = BsnTextSecondary,
          fontFamily = HindSiliguriFontFamily,
          fontSize = 13.sp,
          lineHeight = 19.sp,
        )
      }
    }
  }
}

fun LazyListScope.roadmapItems(progress: AcademyProgressState) {
  items(AcademyTrainingData.fourYearRoadmap, key = { it.year }) { plan ->
    val yearSubjects = AcademyCurriculumRepository.subjectsForYear(plan.year)
    val totalYearChapters = yearSubjects.sumOf { it.chapters.size }
    val completedYearChapters =
      yearSubjects.sumOf { s -> s.chapters.count { progress.completedChapterIds.contains(it.id) } }
    LessonSectionCard(
      title = "${plan.titleBn} (সম্পন্ন: $completedYearChapters/$totalYearChapters অধ্যায়)",
      accent = BsnGold,
    ) {
      Text(
        text = "বার্ষিক লক্ষ্য: ${plan.annualObjectiveBn}",
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "• ${plan.semester1GoalBn}",
        color = BsnGold,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
      )
      Text(
        text = "• ${plan.semester2GoalBn}",
        color = BsnGold,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "সাপ্তাহিক রুটিন: ${plan.weeklyStudyGuidanceBn}",
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
      )
      Text(
        text = "পাঠ ও অনুশীলন: ${plan.readingScheduleBn} | ${plan.practicalScheduleBn}",
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
      )
      Text(
        text = "রিভিশন পরিকল্পনা: ${plan.revisionScheduleBn}",
        color = BsnGreen,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "বর্ষ সমাপনীর দক্ষতা:",
        color = BsnGreen,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
      )
      plan.completionCompetenciesBn.forEach { comp ->
        Text(
          text = "★ $comp",
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontSize = 12.5.sp,
        )
      }
    }
  }
}

@Composable
fun AcademyExamCenterCard(
  progress: AcademyProgressState,
  onSaveQuizScore: (String, Int, Int) -> Unit,
  onOpenSubject: (String, String) -> Unit,
) {
  var selectedYearFilter by remember { mutableIntStateOf(1) }
  val selections = remember(selectedYearFilter) { mutableStateMapOf<String, Int>() }
  val yearSubjects = remember(selectedYearFilter) {
    AcademyCurriculumRepository.subjectsForYear(selectedYearFilter)
  }
  val examMcqs =
    remember(selectedYearFilter) {
      yearSubjects.flatMap { s ->
        s.chapters.flatMap { ch ->
          ch.mcqs.map { mcq -> Triple(s, ch, mcq) }
        }
      }
    }

  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    LessonSectionCard(
      title = "BSN Model Questions / BSN নমুনা প্রশ্ন ও মূল্যায়ন কেন্দ্র",
      accent = BsnGold,
    ) {
      Text(
        text =
          "স্পষ্ট ঘোষণা: এই পরীক্ষাকেন্দ্রের সকল কুইজ, MCQ, সংক্ষিপ্ত/রচনামূলক এবং পোর্টফোলিও মূল্যায়ন হলো 'BSN Model Questions / BSN নমুনা প্রশ্ন'—এগুলো কোনো বিশ্ববিদ্যালয়ের প্রকৃত বিগত পরীক্ষার প্রশ্নপত্র নয়।",
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
        lineHeight = 19.sp,
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items((1..4).toList()) { yr ->
          val active = yr == selectedYearFilter
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(10.dp))
                .background(if (active) BsnRed else BsnSurfaceElevated)
                .border(1.dp, if (active) BsnGold else BsnOutline, RoundedCornerShape(10.dp))
                .clickable { selectedYearFilter = yr }
                .padding(horizontal = 12.dp, vertical = 7.dp)
                .testTag("exam_year_chip_$yr")
          ) {
            Text(
              text = "বর্ষ $yr পরীক্ষা",
              color = Color.White,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp,
            )
          }
        }
      }
      val savedBest = progress.quizScores["year_exam_$selectedYearFilter"]
      if (savedBest != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "বর্ষ $selectedYearFilter-এ সংরক্ষিত স্কোর: $savedBest",
          color = BsnGreen,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
        )
      }
    }

    examMcqs.forEach { (subject, chapter, mcq) ->
      val picked = selections[mcq.id]
      LessonSectionCard(
        title = "${subject.code} • ${subject.titleBn}",
        accent = BsnGreen,
      ) {
        Text(
          text = mcq.questionBn,
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(6.dp))
        mcq.optionsBn.forEachIndexed { idx, opt ->
          val isPicked = picked == idx
          val isCorrect = idx == mcq.correctIndex
          val bg =
            when {
              picked == null -> BsnSurfaceElevated
              isCorrect -> BsnGreen.copy(alpha = 0.25f)
              isPicked -> BsnRed.copy(alpha = 0.28f)
              else -> BsnSurfaceElevated
            }
          Box(
            modifier =
              Modifier.fillMaxWidth()
                .padding(vertical = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bg)
                .border(
                  1.dp,
                  if (picked != null && isCorrect) BsnGreen else BsnOutline,
                  RoundedCornerShape(10.dp),
                )
                .clickable {
                  selections[mcq.id] = idx
                  val score =
                    examMcqs.count { (_, _, m) ->
                      val ans = if (m.id == mcq.id) idx else selections[m.id]
                      ans == m.correctIndex
                    }
                  onSaveQuizScore("year_exam_$selectedYearFilter", score, examMcqs.size)
                }
                .padding(10.dp)
                .testTag("exam_mcq_${mcq.id}_$idx")
          ) {
            Text(
              text = opt,
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
            )
          }
        }
        if (picked != null) {
          Spacer(modifier = Modifier.height(5.dp))
          Text(
            text =
              (if (picked == mcq.correctIndex) "✓ সঠিক! " else "✗ ভুল। ") + mcq.explanationBn,
            color = if (picked == mcq.correctIndex) BsnGreen else BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 12.5.sp,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "মূল অধ্যায় পড়ুন: ${chapter.titleBn} →",
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          modifier = Modifier.clickable { onOpenSubject(subject.id, chapter.id) },
        )
      }
    }

    LessonSectionCard(
      title = "বর্ষ সমাপনী, চূড়ান্ত প্রকল্প ও পোর্টফোলিও মূল্যায়ন কাঠামো",
      accent = BsnGold,
    ) {
      Text(
        text =
          "• সংক্ষিপ্ত ও রচনামূলক প্রশ্নোত্তর: প্রতিটি বিষয়ের অধ্যায়ের ভেতর ক্লিক করে সরাসরি দেখা ও চর্চা করা যায়।\n" +
            "• ব্যবহারিক রিপোর্টিং, সাক্ষাৎকার ও সম্পাদনা মূল্যায়ন: 'ব্যবহারিক' ট্যাবে ৫টি প্রগতিশীল ল্যাব ধাপ。\n" +
            "• ৪র্থ বর্ষ Capstone & পোর্টফোলিও: ১টি সুনির্দিষ্ট অনুসন্ধানী/মাল্টিমিডিয়া প্রকল্প + ৬–১০টি ক্লিপ + Fact-Check লগ + সোর্স নৈতিকতা বিবরণ।",
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 13.sp,
        lineHeight = 20.sp,
      )
    }
  }
}

fun LazyListScope.sourcesAndHonestyItems() {
  item {
    LessonSectionCard(
      title = "কপিরাইট, তথ্যসূত্র ও একাডেমিক সততা ঘোষণা",
      accent = BsnRed,
    ) {
      Text(
        text = AcademyOverviewData.INDEPENDENT_PROGRAM_NOTICE_BN,
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 13.5.sp,
        lineHeight = 21.sp,
      )
    }
  }
  items(AcademyOverviewData.academicReferences, key = { it.title }) { ref ->
    LessonSectionCard(title = ref.categoryBn, accent = BsnGold) {
      Text(
        text = ref.title,
        color = BsnGreen,
        fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp,
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = ref.detailsBn,
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 13.sp,
        lineHeight = 19.sp,
      )
    }
  }
}
