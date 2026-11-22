package com.example.academy.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.academy.data.AcademyProgressState
import com.example.academy.model.AcademySubject
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily

@Composable
fun AcademySubjectDetailScreen(
  subject: AcademySubject,
  initialChapterId: String?,
  progress: AcademyProgressState,
  onBack: () -> Unit,
  onChapterActivated: (String) -> Unit,
  onToggleChapterCompleted: (String) -> Unit,
  onToggleBookmark: (String, String) -> Unit,
  onSaveQuizScore: (String, Int, Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val initialIndex =
    remember(subject.id, initialChapterId) {
      subject.chapters.indexOfFirst { it.id == initialChapterId }.coerceAtLeast(0)
    }
  var selectedChapterIdx by remember(subject.id) { mutableIntStateOf(initialIndex) }
  val chapter = subject.chapters.getOrNull(selectedChapterIdx) ?: subject.chapters.firstOrNull()

  LaunchedEffect(chapter?.id) {
    if (chapter != null) onChapterActivated(chapter.id)
  }

  val mcqSelections = remember(chapter?.id) { mutableStateMapOf<String, Int>() }
  val expandedQaIds = remember(chapter?.id) { mutableStateMapOf<String, Boolean>() }

  LazyColumn(
    modifier = modifier.fillMaxSize().background(BsnBlack).testTag("subject_detail_list"),
    contentPadding = PaddingValues(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier =
            Modifier.clip(RoundedCornerShape(10.dp))
              .background(BsnSurfaceElevated)
              .border(1.dp, BsnOutline, RoundedCornerShape(10.dp))
              .clickable(onClick = onBack)
              .padding(horizontal = 10.dp, vertical = 8.dp)
              .testTag("subject_back_btn"),
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "ফিরে যান",
            tint = BsnGold,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "তালিকায় ফিরুন",
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (chapter != null) {
            val isBookmarked =
              progress.bookmarkedLessons.contains("${subject.id}|${chapter.id}")
            IconButton(
              onClick = { onToggleBookmark(subject.id, chapter.id) },
              modifier = Modifier.testTag("subject_bookmark_btn"),
            ) {
              Icon(
                imageVector =
                  if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = if (isBookmarked) "বুকমার্ক সরান" else "বুকমার্ক করুন",
                tint = if (isBookmarked) BsnGold else BsnTextSecondary,
              )
            }
          }
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(8.dp))
                .background(BsnGreen.copy(alpha = 0.2f))
                .border(1.dp, BsnGreen, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = subject.status.badgeBn,
              color = BsnGreen,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
            )
          }
        }
      }
    }

    // Subject Header & Introduction Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.dp, BsnGold.copy(alpha = 0.6f)),
      ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
          Text(
            text = "${subject.code} • বর্ষ ${subject.year} • সেমিস্টার ${subject.semester}",
            color = BsnGold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = subject.titleBn,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
          )
          Text(
            text = subject.titleEn,
            color = BsnTextSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = subject.introBn,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 14.sp,
            lineHeight = 21.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "সূত্র ও একাডেমিক প্রসঙ্গ: ${subject.academicReferenceNote}",
            color = BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 11.5.sp,
            lineHeight = 17.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "শিখনফল (Learning Objectives):",
            color = BsnGreen,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
          subject.learningObjectivesBn.forEach { obj ->
            Text(
              text = "• $obj",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
              lineHeight = 19.sp,
            )
          }
        }
      }
    }

    // Table of Contents / Chapter Selector
    if (subject.chapters.isNotEmpty()) {
      item {
        Text(
          text = "সূচিপত্র (অধ্যায় সমূহ):",
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(subject.chapters.size) { idx ->
            val ch = subject.chapters[idx]
            val selected = idx == selectedChapterIdx
            val done = progress.completedChapterIds.contains(ch.id)
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier =
                Modifier.clip(RoundedCornerShape(10.dp))
                  .background(if (selected) BsnRed else BsnSurfaceElevated)
                  .border(1.dp, if (selected) BsnGold else BsnOutline, RoundedCornerShape(10.dp))
                  .clickable { selectedChapterIdx = idx }
                  .padding(horizontal = 10.dp, vertical = 7.dp),
            ) {
              if (done) {
                Icon(
                  imageVector = Icons.Filled.CheckCircle,
                  contentDescription = "সম্পন্ন",
                  tint = BsnGreen,
                  modifier = Modifier.size(15.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
              }
              Text(
                text = "অধ্যায় ${ch.chapterNumber}: ${ch.titleBn}",
                color = Color.White,
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
              )
            }
          }
        }
      }
    }

    if (chapter != null) {
      val isCompleted = progress.completedChapterIds.contains(chapter.id)

      // Main Lesson Explanation + Completion Toggle
      item {
        LessonSectionCard(title = "অধ্যায় ${chapter.chapterNumber}: ${chapter.titleBn}", accent = BsnGold) {
          Text(
            text = chapter.titleEn,
            color = BsnTextSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.5.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = chapter.banglaExplanation,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 14.5.sp,
            lineHeight = 23.sp,
          )
          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = { onToggleChapterCompleted(chapter.id) },
            colors =
              ButtonDefaults.buttonColors(
                containerColor = if (isCompleted) BsnGreen else BsnRed,
                contentColor = Color.White,
              ),
            modifier = Modifier.fillMaxWidth().testTag("chapter_complete_btn"),
          ) {
            Icon(
              imageVector =
                if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text =
                if (isCompleted) "অধ্যায় সম্পন্ন চিহ্নিত আছে ✓"
                else "অধ্যায়টি পড়া সম্পন্ন হয়েছে হিসেবে চিহ্নিত করুন",
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
            )
          }
        }
      }

      // Definitions & Concepts
      item {
        LessonSectionCard(title = "মূল সংজ্ঞা ও ধারণা (Definitions & Concepts)", accent = BsnGreen) {
          chapter.definitions.forEach { (term, meaning) ->
            Text(
              text = "• $term",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
            )
            Text(
              text = meaning,
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.5.sp,
              lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
          }
        }
      }

      // English Terminology + Bangla Meanings
      item {
        LessonSectionCard(title = "ইংরেজি পরিভাষা ও বাংলা অর্থ (Terminology)", accent = BsnGold) {
          chapter.englishTerms.forEach { itemTerm ->
            Text(
              text = "${itemTerm.termEn} → ${itemTerm.meaningBn}",
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
            )
            Text(
              text = "প্রয়োগ: ${itemTerm.usageExampleBn}",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 12.5.sp,
              lineHeight = 18.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
          }
        }
      }

      // Practical Examples, Case Study & Notes
      item {
        LessonSectionCard(title = "বাস্তব উদাহরণ, কেস স্টাডি ও জরুরি নোট", accent = BsnRed) {
          chapter.practicalExamples.forEach { ex ->
            Text(
              text = "▸ $ex",
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.5.sp,
              lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
          }
          Spacer(modifier = Modifier.height(6.dp))
          Box(
            modifier =
              Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0C121D))
                .border(1.dp, BsnOutline, RoundedCornerShape(10.dp))
                .padding(10.dp)
          ) {
            Text(
              text = chapter.caseStudyBn,
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
              lineHeight = 20.sp,
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "মনে রাখার নোট:",
            color = BsnGreen,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
          chapter.importantNotes.forEach { note ->
            Text(
              text = "★ $note",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
              lineHeight = 19.sp,
            )
          }
        }
      }

      // Short & Descriptive Q&A (BSN Model Questions)
      item {
        LessonSectionCard(
          title = "BSN Model Questions / BSN নমুনা প্রশ্ন (সংক্ষিপ্ত ও রচনামূলক)",
          accent = BsnGold,
        ) {
          (chapter.shortQAs + chapter.descriptiveQAs).forEach { qa ->
            val open = expandedQaIds[qa.id] ?: true
            Column(
              modifier =
                Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color(0xFF0C1018))
                  .border(1.dp, BsnOutline, RoundedCornerShape(10.dp))
                  .clickable { expandedQaIds[qa.id] = !open }
                  .padding(10.dp)
            ) {
              Text(
                text =
                  (if (qa.isDescriptive) "[রচনামূলক] " else "[সংক্ষিপ্ত] ") + qa.questionBn,
                color = BsnGold,
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
              )
              if (open) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                  text = qa.answerBn,
                  color = BsnTextPrimary,
                  fontFamily = HindSiliguriFontFamily,
                  fontSize = 13.sp,
                  lineHeight = 20.sp,
                )
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }

      // Chapter Interactive MCQ Quiz Assessment (separate LazyColumn items for smooth scroll & testability)
      item {
        LessonSectionCard(
          title = "অধ্যায় মূল্যায়ন কুইজ (BSN Model Questions / BSN নমুনা প্রশ্ন)",
          accent = BsnGreen,
        ) {
          Text(
            text = "নিচের প্রতিটি প্রশ্নের সঠিক উত্তরে ট্যাপ করুন—তাৎক্ষণিক ব্যাখ্যা ও স্কোর দেখা যাবে।",
            color = BsnTextSecondary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 12.5.sp,
          )
        }
      }

      items(chapter.mcqs, key = { it.id }) { mcq ->
        val picked = mcqSelections[mcq.id]
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = BsnSurfaceCard,
          border = BorderStroke(1.dp, BsnGreen.copy(alpha = 0.45f)),
        ) {
          Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Text(
              text = mcq.questionBn,
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
            mcq.optionsBn.forEachIndexed { optIdx, optionText ->
              val isPicked = picked == optIdx
              val isRight = optIdx == mcq.correctIndex
              val bg =
                when {
                  picked == null -> BsnSurfaceElevated
                  isRight -> BsnGreen.copy(alpha = 0.25f)
                  isPicked -> BsnRed.copy(alpha = 0.28f)
                  else -> BsnSurfaceElevated
                }
              val border =
                when {
                  picked != null && isRight -> BsnGreen
                  isPicked -> BsnRed
                  else -> BsnOutline
                }
              Row(
                modifier =
                  Modifier.fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, border, RoundedCornerShape(10.dp))
                    .clickable {
                      mcqSelections[mcq.id] = optIdx
                      val total = chapter.mcqs.size
                      val currentScore =
                        chapter.mcqs.count { m ->
                          val s = if (m.id == mcq.id) optIdx else mcqSelections[m.id]
                          s == m.correctIndex
                        }
                      onSaveQuizScore("chapter_${chapter.id}", currentScore, total)
                    }
                    .padding(horizontal = 10.dp, vertical = 9.dp)
                    .testTag("mcq_option_${mcq.id}_$optIdx"),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = optionText,
                  color = BsnTextPrimary,
                  fontFamily = HindSiliguriFontFamily,
                  fontSize = 13.sp,
                )
              }
            }
            if (picked != null) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text =
                  (if (picked == mcq.correctIndex) "✓ সঠিক উত্তর! " else "✗ ভুল উত্তর। ") +
                    mcq.explanationBn,
                color = if (picked == mcq.correctIndex) BsnGreen else BsnGold,
                fontFamily = HindSiliguriFontFamily,
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
              )
            }
          }
        }
      }

      val answeredCount = chapter.mcqs.count { mcqSelections[it.id] != null }
      val score = chapter.mcqs.count { mcqSelections[it.id] == it.correctIndex }
      if (answeredCount > 0) {
        item {
          Surface(
            modifier = Modifier.fillMaxWidth().testTag("chapter_quiz_score"),
            shape = RoundedCornerShape(12.dp),
            color = BsnSurfaceElevated,
            border = BorderStroke(1.dp, BsnGold),
          ) {
            Text(
              text = "বর্তমান কুইজ স্কোর: $score / ${chapter.mcqs.size}",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
              modifier = Modifier.padding(12.dp),
            )
          }
        }
      }

      // Practical Exercise, Assignment & Revision
      item {
        LessonSectionCard(title = "অনুশীলন, অ্যাসাইনমেন্ট ও চটজলদি রিভিশন", accent = BsnGold) {
          Text(
            text = "ব্যবহারিক অনুশীলন:",
            color = BsnGold,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
          Text(
            text = chapter.practicalExerciseBn,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "অ্যাসাইনমেন্ট:",
            color = BsnGreen,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
          Text(
            text = chapter.assignmentBn,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontSize = 13.5.sp,
            lineHeight = 20.sp,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "রিভিশন সারসংক্ষেপ:",
            color = BsnRed,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
          chapter.revisionPointsBn.forEach { rev ->
            Text(
              text = "✓ $rev",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 13.sp,
            )
          }
        }
      }
    }
  }
}

@Composable
fun LessonSectionCard(
  title: String,
  accent: Color,
  content: @Composable () -> Unit,
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = BsnSurfaceCard,
    border = BorderStroke(1.dp, accent.copy(alpha = 0.55f)),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
      Text(
        text = title,
        color = accent,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
      )
      Spacer(modifier = Modifier.height(8.dp))
      content()
    }
  }
}
