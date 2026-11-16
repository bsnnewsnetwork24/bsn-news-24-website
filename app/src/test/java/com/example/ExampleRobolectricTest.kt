package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ApplicationProvider
import com.example.config.BsnConfig
import com.example.data.local.JournalistProfile
import com.example.ui.components.bsnTabs
import com.example.ui.theme.BsnNews24Theme
import com.example.utils.IdCardGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

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

    // Tab 9: Pure Offline Add + Edit + Delete Link
    composeTestRule.onNodeWithTag("bottom_nav_row").performScrollToNode(hasTestTag("tab_my_links"))
    composeTestRule.onNodeWithTag("tab_my_links").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("tab9_list").performScrollToNode(hasTestTag("add_news_link_btn"))
    composeTestRule.onNodeWithTag("add_news_link_btn").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("dialog_headline_input").performTextInput("ঢাকায় বিশেষ সংবাদ সম্মেলন")
    composeTestRule.onNodeWithTag("dialog_link_input").performTextInput("https://bsnnews24.blogspot.com/test")
    composeTestRule.onNodeWithTag("dialog_save_link_btn").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      runCatching {
        composeTestRule
          .onNodeWithTag("tab9_list")
          .performScrollToNode(androidx.compose.ui.test.hasText("ঢাকায় বিশেষ সংবাদ সম্মেলন"))
        true
      }.getOrDefault(false)
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("ঢাকায় বিশেষ সংবাদ সম্মেলন").assertIsDisplayed()

    // Edit saved link offline
    composeTestRule.onNodeWithContentDescription("এডিট করুন").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("dialog_headline_input").performTextReplacement("আপডেটেড সংবাদ সম্মেলন")
    composeTestRule.onNodeWithTag("dialog_save_link_btn").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      runCatching {
        composeTestRule
          .onNodeWithTag("tab9_list")
          .performScrollToNode(androidx.compose.ui.test.hasText("আপডেটেড সংবাদ সম্মেলন"))
        true
      }.getOrDefault(false)
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("আপডেটেড সংবাদ সম্মেলন").assertIsDisplayed()

    // Delete saved link offline
    composeTestRule.onNodeWithContentDescription("মুছে ফেলুন").performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("মুছে ফেলুন").performClick()
    composeTestRule.waitUntil(timeoutMillis = 5_000) {
      runCatching {
        composeTestRule
          .onNodeWithTag("tab9_list")
          .performScrollToNode(androidx.compose.ui.test.hasText("সংরক্ষিত নিউজ লিংক (0)"))
        true
      }.getOrDefault(false)
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("সংরক্ষিত নিউজ লিংক (0)").assertIsDisplayed()

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

    val context = ApplicationProvider.getApplicationContext<Context>()
    val idBitmap =
      IdCardGenerator.renderIdCardBitmap(
        context,
        JournalistProfile(
          name = "রহিম উদ্দিন",
          designation = "জেলা প্রতিনিধি",
          mobileNumber = "01711000000",
          district = "ঢাকা",
          isSaved = true,
        ),
      )
    assertTrue(idBitmap.width == 1080 && idBitmap.height == 1600)
  }
}
