package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.BsnConfig
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.components.SocialBrandBadge
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.TelegramBlue
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab2FbGroupTelegramScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("tab2_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Section 1: Facebook Group (Distinct Facebook Blue Identity)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.5.dp, FacebookBlue),
      ) {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  listOf(FacebookBlue.copy(alpha = 0.20f), Color(0xFF0E1420))
                )
              )
              .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            SocialBrandBadge(brand = SocialBrand.FACEBOOK, size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "BSN Facebook Group",
                color = BsnTextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
              )
              Text(
                text = "উন্মুক্ত সাংবাদিক নিউজ শেয়ারিং গ্রুপ",
                color = BsnGold,
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
              )
            }
          }

          BsnSwitchButton(
            title = "Facebook Group এ যোগ দিন",
            subtitle = "সকল নিউজ ও লিংক সরাসরি শেয়ার করুন",
            brand = SocialBrand.FACEBOOK,
            accentColor = FacebookBlue,
            testTag = "tab2_fb_group_btn",
            onClick = {
              ExternalLinkLauncher.openUrl(
                context,
                BsnConfig.FACEBOOK_GROUP_URL,
                ExternalLinkLauncher.TargetPlatform.FACEBOOK,
              )
            },
          )

          BsnInfoBoard(
            badgeTitle = "Facebook Group তথ্য",
            bodyText = BsnConfig.TAB2_FB_GROUP_TEXT,
            accentColor = FacebookBlue,
          )
        }
      }
    }

    // Section 2: Telegram Channel (Distinct Telegram Sky-Blue Identity)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.5.dp, TelegramBlue),
      ) {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  listOf(TelegramBlue.copy(alpha = 0.22f), Color(0xFF0C151D))
                )
              )
              .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            SocialBrandBadge(brand = SocialBrand.TELEGRAM, size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "BSN News 24 Telegram Channel",
                color = BsnTextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.5.sp,
              )
              Text(
                text = "t.me/bsnnews24official",
                color = TelegramBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
              )
            }
          }

          BsnSwitchButton(
            title = "Telegram Channel এ প্রবেশ করুন",
            subtitle = "সরাসরি Telegram অ্যাপ বা ওয়েব লিংকে খুলবে",
            brand = SocialBrand.TELEGRAM,
            accentColor = TelegramBlue,
            testTag = "tab2_telegram_btn",
            onClick = {
              ExternalLinkLauncher.openUrl(
                context,
                BsnConfig.TELEGRAM_CHANNEL_URL,
                ExternalLinkLauncher.TargetPlatform.TELEGRAM,
              )
            },
          )

          BsnInfoBoard(
            badgeTitle = "Telegram Channel তথ্য",
            bodyText = BsnConfig.TAB2_TELEGRAM_TEXT,
            accentColor = TelegramBlue,
          )
        }
      }
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}
