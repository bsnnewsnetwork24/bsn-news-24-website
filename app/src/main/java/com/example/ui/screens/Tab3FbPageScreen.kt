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
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnChannelBannerCard
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab3FbPageScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      BsnChannelBannerCard(
        drawableRes = R.drawable.bsn_press_banner,
        caption = "BSN News 24 - Official Facebook Page",
      )
    }

    item {
      BsnSwitchButton(
        title = "WhatsApp এ নিউজ পাঠান",
        subtitle = "নম্বর: ${BsnConfig.WHATSAPP_NEWS_NUMBER_INTL}",
        brand = SocialBrand.WHATSAPP,
        accentColor = WhatsAppGreen,
        testTag = "tab3_whatsapp_news_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.WHATSAPP_NEWS_URL,
            ExternalLinkLauncher.TargetPlatform.WHATSAPP,
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
