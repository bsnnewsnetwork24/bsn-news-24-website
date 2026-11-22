package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnChannelBannerCard
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.theme.FacebookBlue
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab3FbPageScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("tab3_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      BsnChannelBannerCard(
        drawableRes = R.drawable.bsn_press_banner,
        caption = "BSN News 24 - Official Facebook Page",
      )
    }

    // Relocated Facebook Personal Profile Switch Button (moved from Tab 1 Home)
    item {
      BsnSwitchButton(
        title = "রিমন শিকদার - ব্যক্তিগত ফেসবুক প্রোফাইল",
        subtitle = "অফিশিয়াল Facebook প্রোফাইল ভিজিট করুন",
        brand = SocialBrand.FACEBOOK,
        accentColor = FacebookBlue,
        testTag = "tab3_fb_profile_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.FACEBOOK_PERSONAL_PROFILE_URL,
            ExternalLinkLauncher.TargetPlatform.FACEBOOK,
          )
        },
      )
    }

    item {
      BsnSwitchButton(
        title = "Facebook Page Visit করুন",
        subtitle = "BSN News 24 - Bangladesh Sangbadik Network",
        brand = SocialBrand.FACEBOOK,
        accentColor = FacebookBlue,
        testTag = "tab3_fb_page_visit_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.FACEBOOK_PAGE_URL,
            ExternalLinkLauncher.TargetPlatform.FACEBOOK,
          )
        },
      )
    }

    item {
      BsnInfoBoard(
        badgeTitle = "BSN News 24 Facebook Page নির্দেশনা",
        bodyText = BsnConfig.TAB3_FB_PAGE_TEXT,
        accentColor = FacebookBlue,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}
