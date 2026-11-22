package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.academy.data.AcademyCurriculumRepository
import com.example.academy.ui.AcademyRootScreen
import com.example.academy.ui.AcademyViewModel
import com.example.ui.components.BsnBottomNavBar
import com.example.ui.components.BsnFixedHeader
import com.example.ui.components.bsnTabs
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.Tab10ProfileIdCardScreen
import com.example.ui.screens.Tab1HomeScreen
import com.example.ui.screens.Tab2FbGroupTelegramScreen
import com.example.ui.screens.Tab3FbPageScreen
import com.example.ui.screens.Tab4InstagramScreen
import com.example.ui.screens.Tab5YouTubeScreen
import com.example.ui.screens.Tab6TikTokScreen
import com.example.ui.screens.Tab7WebsiteScreen
import com.example.ui.screens.Tab8WhatsAppGroupScreen
import com.example.ui.screens.Tab9MyNewsLinksScreen
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnNews24Theme
import com.example.ui.viewmodel.NewsLinksViewModel
import com.example.ui.viewmodel.ProfileIdViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      BsnNews24Theme {
        BsnNews24AppRoot()
      }
    }
  }
}

@Composable
fun BsnNews24AppRoot() {
  var showSplash by rememberSaveable { mutableStateOf(true) }

  if (showSplash) {
    SplashScreen(onFinished = { showSplash = false })
  } else {
    BsnMainShell()
  }
}

@Composable
fun BsnMainShell() {
  val context = LocalContext.current
  var selectedTab by rememberSaveable { mutableIntStateOf(0) }
  var showAcademy by rememberSaveable { mutableStateOf(false) }

  val newsLinksViewModel: NewsLinksViewModel =
    viewModel(factory = NewsLinksViewModel.Factory(context))
  val profileIdViewModel: ProfileIdViewModel =
    viewModel(factory = ProfileIdViewModel.Factory(context))
  val academyViewModel: AcademyViewModel =
    viewModel(factory = AcademyViewModel.Factory(context))
  val academyProgress by academyViewModel.progressState.collectAsStateWithLifecycle()

  // Handle system back press inside Academy (subject -> hub -> home) and tabs 2..10 -> Home
  BackHandler(enabled = showAcademy) {
    if (!academyViewModel.navigateBackToHub()) {
      showAcademy = false
    }
  }
  BackHandler(enabled = !showAcademy && selectedTab != 0) {
    selectedTab = 0
  }

  val currentTabTitle =
    if (showAcademy) "BSN Journalism Academy"
    else bsnTabs.getOrNull(selectedTab)?.fullTitle ?: bsnTabs.first().fullTitle

  Scaffold(
    modifier = Modifier.fillMaxSize().background(BsnBlack),
    containerColor = BsnBlack,
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      BsnFixedHeader(currentTabTitle = currentTabTitle)
    },
    bottomBar = {
      BsnBottomNavBar(
        selectedIndex = selectedTab,
        onTabSelected = {
          showAcademy = false
          selectedTab = it
        },
      )
    },
  ) { innerPadding ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .background(BsnBlack)
          .padding(innerPadding)
    ) {
      if (showAcademy) {
        AcademyRootScreen(
          viewModel = academyViewModel,
          onExitToHome = { showAcademy = false },
        )
      } else {
        Crossfade(targetState = selectedTab, label = "bsn_tab_crossfade") { tabIndex ->
          when (tabIndex) {
            0 ->
              Tab1HomeScreen(
                completedAcademyChapters = academyProgress.completedChapterIds.size,
                totalAcademyChapters = AcademyCurriculumRepository.totalChapterCount(),
                onOpenAcademy = { showAcademy = true },
              )
            1 -> Tab2FbGroupTelegramScreen()
            2 -> Tab3FbPageScreen()
            3 -> Tab4InstagramScreen()
            4 -> Tab5YouTubeScreen()
            5 -> Tab6TikTokScreen()
            6 -> Tab7WebsiteScreen()
            7 -> Tab8WhatsAppGroupScreen()
            8 -> Tab9MyNewsLinksScreen(viewModel = newsLinksViewModel)
            9 -> Tab10ProfileIdCardScreen(viewModel = profileIdViewModel)
            else ->
              Tab1HomeScreen(
                completedAcademyChapters = academyProgress.completedChapterIds.size,
                totalAcademyChapters = AcademyCurriculumRepository.totalChapterCount(),
                onOpenAcademy = { showAcademy = true },
              )
          }
        }
      }
    }
  }
}
