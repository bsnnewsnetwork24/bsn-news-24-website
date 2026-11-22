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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.academy.data.AcademyCurriculumRepository
import com.example.academy.data.AcademyOverviewData
import com.example.academy.model.AcademySubject
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextMuted
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily

@Composable
fun AcademyRootScreen(
  viewModel: AcademyViewModel,
  onExitToHome: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val route by viewModel.route.collectAsStateWithLifecycle()
  val progress by viewModel.progressState.collectAsStateWithLifecycle()
  val section by viewModel.selectedSection.collectAsStateWithLifecycle()
  val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
  val selectedSemester by viewModel.selectedSemester.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

  when (val currentRoute = route) {
    is AcademyRoute.SubjectDetail -> {
      val subject = AcademyCurriculumRepository.findSubject(currentRoute.subjectId)
      if (subject != null) {
        AcademySubjectDetailScreen(
          subject = subject,
          initialChapterId = currentRoute.initialChapterId,
          progress = progress,
          onBack = { viewModel.navigateBackToHub() },
          onChapterActivated = { chId -> viewModel.recordChapterActive(subject.id, chId) },
          onToggleChapterCompleted = { chId -> viewModel.toggleChapterCompleted(chId) },
          onToggleBookmark = { sId, chId -> viewModel.toggleBookmark(sId, chId) },
          onSaveQuizScore = { k, sc, tot -> viewModel.saveQuizScore(k, sc, tot) },
          modifier = modifier,
        )
      }
    }
    AcademyRoute.Hub -> {
      val semesterSubjects =
        AcademyCurriculumRepository.subjectsForSemester(selectedYear, selectedSemester)
      val yearSemesters =
        AcademyCurriculumRepository.semesters.filter { it.year == selectedYear }
      val currentYearInfo =
        AcademyCurriculumRepository.years.firstOrNull { it.year == selectedYear }
      val currentSemInfo =
        yearSemesters.firstOrNull { it.semesterNumber == selectedSemester }

      LazyColumn(
        modifier = modifier.fillMaxSize().background(BsnBlack).testTag("academy_hub_list"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        // Academy Top Bar & Independent Programme + Offline Status Badges
        item {
          Column(
            modifier =
              Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF1F090C), Color(0xFF0E1722))))
                .border(1.dp, BsnGold.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
                .padding(12.dp)
          ) {
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
                    .clickable(onClick = onExitToHome)
                    .padding(horizontal = 10.dp, vertical = 7.dp)
                    .testTag("academy_back_btn"),
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "হোম পেজে ফিরুন",
                  tint = BsnGold,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "হোম পেজ",
                  color = Color.White,
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp,
                )
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                  Modifier.clip(RoundedCornerShape(8.dp))
                    .background(BsnGreen.copy(alpha = 0.2f))
                    .border(1.dp, BsnGreen.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
              ) {
                Icon(
                  imageVector = Icons.Filled.OfflinePin,
                  contentDescription = "অফলাইন পাঠ প্রস্তুত",
                  tint = BsnGreen,
                  modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "অফলাইন প্রস্তুত",
                  color = BsnGreen,
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = AcademyOverviewData.ACADEMY_TITLE_EN,
              color = BsnGold,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
            )
            Text(
              text = AcademyOverviewData.ACADEMY_TITLE_BILINGUAL,
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.5.sp,
            )
            Text(
              text =
                "মোট বিষয়: ${AcademyCurriculumRepository.allSubjects.size} • অধ্যায়: ${AcademyCurriculumRepository.totalChapterCount()} • সম্পন্ন পাঠ: ${progress.completedChapterIds.size} • বুকমার্ক: ${progress.bookmarkedLessons.size}",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontSize = 12.sp,
            )
          }
        }

        // Smart Full-Content Offline Search Bar (Part 14)
        item {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = {
              Text(
                text = "বাংলা বা ইংরেজিতে পাঠ, পরিভাষা, প্রশ্ন বা MCQ খুঁজুন...",
                fontFamily = HindSiliguriFontFamily,
                fontSize = 12.5.sp,
                color = BsnTextMuted,
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "অনুসন্ধান",
                tint = BsnGold,
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                  Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "সার্চ মুছুন",
                    tint = BsnTextSecondary,
                  )
                }
              }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.commitSearchQuery(searchQuery) }),
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BsnSurfaceCard,
                unfocusedContainerColor = BsnSurfaceCard,
                focusedBorderColor = BsnGold,
                unfocusedBorderColor = BsnOutline,
                focusedTextColor = BsnTextPrimary,
                unfocusedTextColor = BsnTextPrimary,
              ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().testTag("academy_search_input"),
          )

          if (searchQuery.isBlank() && progress.searchHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Filled.History,
                  contentDescription = null,
                  tint = BsnGold,
                  modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "সাম্প্রতিক খোঁজ:",
                  color = BsnTextSecondary,
                  fontFamily = HindSiliguriFontFamily,
                  fontSize = 11.5.sp,
                )
              }
              Text(
                text = "মুছুন",
                color = BsnRed,
                fontFamily = HindSiliguriFontFamily,
                fontSize = 11.5.sp,
                modifier = Modifier.clickable { viewModel.clearSearchHistory() },
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(progress.searchHistory) { past ->
                Box(
                  modifier =
                    Modifier.clip(RoundedCornerShape(8.dp))
                      .background(BsnSurfaceElevated)
                      .border(1.dp, BsnOutline, RoundedCornerShape(8.dp))
                      .clickable { viewModel.commitSearchQuery(past) }
                      .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = past,
                    color = BsnGold,
                    fontFamily = HindSiliguriFontFamily,
                    fontSize = 11.5.sp,
                  )
                }
              }
            }
          }
        }

        // Live Search Results (Clicks directly into exact Subject/Chapter/Lesson/MCQ)
        if (searchQuery.trim().length >= 2) {
          if (searchResults.isEmpty()) {
            item {
              LessonSectionCard(title = "অনুসন্ধানের ফলাফল (০)", accent = BsnRed) {
                Text(
                  text =
                    "“$searchQuery” এর সাথে মিল পাওয়া যায়নি। অন্য একটি সহজ বাংলা বা ইংরেজি শব্দ চেষ্টা করুন—যেমন: ‘গেটকিপিং’, ‘Lead’, ‘OSINT’, ‘RTI’, বা ‘ফ্যাক্ট-চেক’।",
                  color = BsnTextSecondary,
                  fontFamily = HindSiliguriFontFamily,
                  fontSize = 13.sp,
                  lineHeight = 19.sp,
                )
              }
            }
          } else {
            item {
              Text(
                text = "অনুসন্ধানের ফলাফল (${searchResults.size}) — ক্লিক করে সরাসরি পাঠ খুলুন:",
                color = BsnGreen,
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
              )
            }
            items(searchResults, key = { it.id }) { hit ->
              Card(
                modifier =
                  Modifier.fillMaxWidth()
                    .clickable {
                      viewModel.commitSearchQuery(searchQuery)
                      viewModel.openSubject(hit.subjectId, hit.chapterId, hit.targetMcqId)
                    }
                    .testTag("search_hit_${hit.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
                border = BorderStroke(1.dp, BsnGold.copy(alpha = 0.5f)),
              ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                  Text(
                    text = "${hit.hitType.labelBn} • ${hit.subtitleBn}",
                    color = BsnGold,
                    fontFamily = HindSiliguriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                  )
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(
                    text = hit.titleBn,
                    color = BsnTextPrimary,
                    fontFamily = HindSiliguriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = hit.snippetBn,
                    color = BsnTextSecondary,
                    fontFamily = HindSiliguriFontFamily,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                  )
                }
              }
            }
          }
        } else {
          // Resume Reading Banner + Bookmarks Quick Row
          val lastReadParts = progress.lastReadLesson?.split("|")
          val resumeSubject = lastReadParts?.getOrNull(0)?.let { AcademyCurriculumRepository.findSubject(it) }
          val resumeChapterId = lastReadParts?.getOrNull(1)
          if (resumeSubject != null && resumeChapterId != null) {
            item {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                  Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BsnRed.copy(alpha = 0.22f))
                    .border(1.dp, BsnGold, RoundedCornerShape(14.dp))
                    .clickable { viewModel.openSubject(resumeSubject.id, resumeChapterId) }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("academy_resume_btn"),
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "যেখান থেকে থেমেছিলেন — পড়া চালিয়ে যান",
                    color = BsnGold,
                    fontFamily = HindSiliguriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                  )
                  Text(
                    text = "${resumeSubject.code}: ${resumeSubject.titleBn}",
                    color = Color.White,
                    fontFamily = HindSiliguriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                  )
                }
                Icon(
                  imageVector = Icons.Filled.PlayArrow,
                  contentDescription = "Resume Reading",
                  tint = BsnGold,
                )
              }
            }
          }

          if (progress.bookmarkedLessons.isNotEmpty()) {
            item {
              Text(
                text = "সংরক্ষিত বুকমার্ক সমূহ:",
                color = BsnGold,
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
              )
              Spacer(modifier = Modifier.height(5.dp))
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(progress.bookmarkedLessons) { token ->
                  val parts = token.split("|")
                  val sub = parts.getOrNull(0)?.let { AcademyCurriculumRepository.findSubject(it) }
                  val chId = parts.getOrNull(1)
                  if (sub != null) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier =
                        Modifier.clip(RoundedCornerShape(10.dp))
                          .background(BsnSurfaceElevated)
                          .border(1.dp, BsnGold.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                          .clickable { viewModel.openSubject(sub.id, chId) }
                          .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                      Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = null,
                        tint = BsnGold,
                        modifier = Modifier.size(14.dp),
                      )
                      Spacer(modifier = Modifier.width(5.dp))
                      Text(
                        text = sub.titleBn,
                        color = BsnTextPrimary,
                        fontFamily = HindSiliguriFontFamily,
                        fontSize = 12.sp,
                      )
                    }
                  }
                }
              }
            }
          }

          // Academy Navigation Sections
          item {
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.testTag("academy_sections_row"),
            ) {
              items(AcademySectionTab.entries) { tab ->
                val active = tab == section
                Box(
                  modifier =
                    Modifier.clip(RoundedCornerShape(12.dp))
                      .background(if (active) BsnRed else BsnSurfaceElevated)
                      .border(
                        1.dp,
                        if (active) BsnGold else BsnOutline,
                        RoundedCornerShape(12.dp),
                      )
                      .clickable { viewModel.selectSection(tab) }
                      .padding(horizontal = 12.dp, vertical = 8.dp)
                      .testTag("academy_sec_${tab.name}")
                ) {
                  Text(
                    text = tab.labelBn,
                    color = Color.White,
                    fontFamily = HindSiliguriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                  )
                }
              }
            }
          }

          when (section) {
            AcademySectionTab.CURRICULUM -> {
              // Years 1..4 Tabs
              item {
                LazyRow(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.testTag("academy_years_row"),
                ) {
                  items(AcademyCurriculumRepository.years) { yr ->
                    val active = yr.year == selectedYear
                    Column(
                      modifier =
                        Modifier.clip(RoundedCornerShape(12.dp))
                          .background(if (active) BsnGold else BsnSurfaceCard)
                          .border(
                            1.dp,
                            if (active) BsnGold else BsnOutline,
                            RoundedCornerShape(12.dp),
                          )
                          .clickable { viewModel.selectYear(yr.year) }
                          .padding(horizontal = 12.dp, vertical = 8.dp)
                          .testTag("academy_year_${yr.year}")
                    ) {
                      Text(
                        text = "বর্ষ ${yr.year}",
                        color = if (active) BsnBlack else BsnGold,
                        fontFamily = HindSiliguriFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                      )
                      Text(
                        text = yr.titleEn.substringAfter("— ").take(20),
                        color = if (active) BsnBlack else BsnTextSecondary,
                        fontSize = 10.5.sp,
                      )
                    }
                  }
                }
              }

              // Semesters under Selected Year
              item {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  yearSemesters.forEach { sem ->
                    val active = sem.semesterNumber == selectedSemester
                    Box(
                      modifier =
                        Modifier.weight(1f)
                          .clip(RoundedCornerShape(12.dp))
                          .background(if (active) BsnGreen.copy(alpha = 0.25f) else BsnSurfaceElevated)
                          .border(
                            1.5.dp,
                            if (active) BsnGreen else BsnOutline,
                            RoundedCornerShape(12.dp),
                          )
                          .clickable { viewModel.selectSemester(sem.semesterNumber) }
                          .padding(horizontal = 10.dp, vertical = 8.dp)
                          .testTag("academy_sem_${sem.semesterNumber}")
                    ) {
                      Text(
                        text = sem.titleBn,
                        color = if (active) BsnGreen else BsnTextPrimary,
                        fontFamily = HindSiliguriFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                      )
                    }
                  }
                }
              }

              if (currentYearInfo != null && currentSemInfo != null) {
                item {
                  LessonSectionCard(title = currentYearInfo.titleBn, accent = BsnGold) {
                    Text(
                      text = currentYearInfo.overviewBn,
                      color = BsnTextSecondary,
                      fontFamily = HindSiliguriFontFamily,
                      fontSize = 12.5.sp,
                      lineHeight = 19.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "সেমিস্টার লক্ষ্য: ${currentSemInfo.goalBn}",
                      color = BsnGreen,
                      fontFamily = HindSiliguriFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.5.sp,
                    )
                  }
                }
              }

              items(semesterSubjects, key = { it.id }) { subject ->
                SubjectListCard(
                  subject = subject,
                  isCompleted =
                    subject.chapters.isNotEmpty() &&
                      subject.chapters.all { progress.completedChapterIds.contains(it.id) },
                  onClick = { viewModel.openSubject(subject.id) },
                )
              }
            }
            AcademySectionTab.PRACTICAL ->
              practicalLabItems(progress = progress, onTogglePractical = { viewModel.togglePracticalCompleted(it) })
            AcademySectionTab.BD_INTL -> bdIntlItems()
            AcademySectionTab.ETHICS -> ethicsSafetyItems()
            AcademySectionTab.ROADMAP -> roadmapItems(progress = progress)
            AcademySectionTab.EXAMS ->
              item {
                AcademyExamCenterCard(
                  progress = progress,
                  onSaveQuizScore = { k, sc, tot -> viewModel.saveQuizScore(k, sc, tot) },
                  onOpenSubject = { sId, chId -> viewModel.openSubject(sId, chId) },
                )
              }
            AcademySectionTab.SOURCES -> sourcesAndHonestyItems()
          }
        }
      }
    }
  }
}

@Composable
private fun SubjectListCard(
  subject: AcademySubject,
  isCompleted: Boolean,
  onClick: () -> Unit,
) {
  Card(
    modifier =
      Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .clickable(onClick = onClick)
        .testTag("subject_card_${subject.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
    border =
      BorderStroke(
        1.dp,
        if (isCompleted) BsnGreen else BsnGold.copy(alpha = 0.45f),
      ),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "${subject.code} • ${subject.chapters.size} অধ্যায়",
          color = BsnGold,
          fontWeight = FontWeight.Bold,
          fontSize = 11.5.sp,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isCompleted) {
            Icon(
              imageVector = Icons.Filled.CheckCircle,
              contentDescription = "সম্পন্ন",
              tint = BsnGreen,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
          }
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(6.dp))
                .background(BsnGreen.copy(alpha = 0.18f))
                .padding(horizontal = 7.dp, vertical = 2.dp)
          ) {
            Text(
              text = subject.status.badgeBn,
              color = BsnGreen,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(5.dp))
      Text(
        text = subject.titleBn,
        color = BsnTextPrimary,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.5.sp,
      )
      Text(
        text = subject.titleEn,
        color = BsnGold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subject.introBn,
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontSize = 12.5.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}
