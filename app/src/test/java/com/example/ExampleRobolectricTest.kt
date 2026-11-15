package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.config.BsnConfig
import com.example.ui.components.bsnTabs
import com.example.ui.theme.BsnNews24Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun ensureFirebaseInitForTest() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    if (FirebaseApp.getApps(context).isEmpty()) {
      FirebaseApp.initializeApp(
        context,
        FirebaseOptions.Builder()
          .setApplicationId("com.bsnnews24.app")
          .setProjectId("bsn-news-24")
          .setApiKey("test-api-key")
          .build(),
      )
    }
  }

  @Test
  fun appNameAndTabCount_matchSpecification() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    assertEquals("BSN News 24", context.getString(R.string.app_name))
    assertEquals(10, bsnTabs.size)
  }

  @Test
  fun mainShell_opensWithoutLogin_displaysHeaderAndAll10Tabs() {
    composeTestRule.setContent {
      BsnNews24Theme {
        BsnMainShell()
      }
    }
    composeTestRule.waitForIdle()

    // Fixed header welcome message appears on initial Home screen without forced login
    composeTestRule.onNodeWithText(BsnConfig.FIXED_HEADER_WELCOME).assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab1_list").performScrollToNode(hasTestTag("home_whatsapp_news_btn"))
    composeTestRule.onNodeWithTag("home_whatsapp_news_btn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab1_list").performScrollToNode(hasTestTag("home_fb_profile_btn"))
    composeTestRule.onNodeWithTag("home_fb_profile_btn").assertIsDisplayed()

    // Navigate to Tab 2 (FB Group + Telegram)
    composeTestRule.onNodeWithTag("bottom_nav_row").performScrollToNode(hasTestTag("tab_fb_group_telegram"))
    composeTestRule.onNodeWithTag("tab_fb_group_telegram").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("tab2_fb_group_btn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab2_list").performScrollToNode(hasTestTag("tab2_telegram_btn"))
    composeTestRule.onNodeWithTag("tab2_telegram_btn").assertIsDisplayed()

    // Navigate to Tab 8 (BSN WhatsApp Group)
    composeTestRule.onNodeWithTag("bottom_nav_row").performScrollToNode(hasTestTag("tab_whatsapp_group"))
    composeTestRule.onNodeWithTag("tab_whatsapp_group").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("tab8_list").performScrollToNode(hasTestTag("tab8_whatsapp_group_btn"))
    composeTestRule.onNodeWithTag("tab8_whatsapp_group_btn").assertIsDisplayed()

    // Tab 9: Offline Add Link + Search
    composeTestRule.onNodeWithTag("bottom_nav_row").performScrollToNode(hasTestTag("tab_my_links"))
    composeTestRule.onNodeWithTag("tab_my_links").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("tab9_list").performScrollToNode(hasTestTag("add_news_link_btn"))
    composeTestRule.onNodeWithTag("add_news_link_btn").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("dialog_headline_input").performTextInput("ঢাকায় বিশেষ সংবাদ সম্মেলন")
    composeTestRule.onNodeWithTag("dialog_link_input").performTextInput("https://bsnnews24.blogspot.com/test")
    composeTestRule.onNodeWithTag("dialog_save_link_btn").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("tab9_list").performScrollToNode(androidx.compose.ui.test.hasText("ঢাকায় বিশেষ সংবাদ সম্মেলন"))
    composeTestRule.onNodeWithText("ঢাকায় বিশেষ সংবাদ সম্মেলন").assertIsDisplayed()

    // Tab 10: Offline Profile + Digital ID Card without serial number
    composeTestRule.onNodeWithTag("bottom_nav_row").performScrollToNode(hasTestTag("tab_profile_id"))
    composeTestRule.onNodeWithTag("tab_profile_id").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("profile_name_input").performTextInput("রহিম উদ্দিন")
    composeTestRule.onNodeWithTag("profile_designation_input").performTextInput("জেলা প্রতিনিধি")
    composeTestRule.onNodeWithTag("profile_mobile_input").performTextInput("01711000000")
    composeTestRule.onNodeWithTag("profile_district_input").performTextInput("ঢাকা")
    composeTestRule.onNodeWithTag("tab10_list").performScrollToNode(hasTestTag("save_profile_btn"))
    composeTestRule.onNodeWithTag("save_profile_btn").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      composeTestRule.onAllNodesWithText("তথ্য এডিট করুন").fetchSemanticsNodes().isNotEmpty()
    }
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("tab10_list").performScrollToNode(hasTestTag("share_id_card_btn"))
    composeTestRule.onNodeWithText(BsnConfig.TAB10_ID_CARD_PROOF_TEXT).assertIsDisplayed()
    composeTestRule.onNodeWithTag("share_id_card_btn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab10_list").performScrollToNode(hasTestTag("print_id_card_btn"))
    composeTestRule.onNodeWithTag("print_id_card_btn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab10_list").performScrollToNode(hasTestTag("download_id_card_btn"))
    composeTestRule.onNodeWithTag("download_id_card_btn").assertIsDisplayed()
    assertFalse(BsnConfig.TAB10_ID_CARD_PROOF_TEXT.contains("ID No"))
  }
}
