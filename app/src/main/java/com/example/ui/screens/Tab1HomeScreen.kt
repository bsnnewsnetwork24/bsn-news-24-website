package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnChannelBannerCard
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab1HomeScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("tab1_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      BsnChannelBannerCard(
        drawableRes = R.drawable.bsn_channel_banner,
        caption = "BSN News 24 • ${BsnConfig.TAGLINE}",
      )
    }

    // Founder Introduction Card (Uses uploaded official BSN Logo per Part 2A rule)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border =
          BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(BsnGold, BsnRed, BsnGreen)),
          ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      ) {
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(Color(0xFF1F0A0C), Color(0xFF101924), Color(0xFF0A1B14))
                )
              )
              .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier =
              Modifier.size(84.dp)
                .clip(CircleShape)
                .border(2.5.dp, BsnGold, CircleShape)
                .padding(2.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.bsn_official_logo),
              contentDescription = "BSN News 24 লোগো প্রোফাইল",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize().clip(CircleShape),
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier =
                  Modifier.clip(RoundedCornerShape(6.dp))
                    .background(BsnRed)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "FOUNDER & EDITOR",
                  color = Color.White,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 10.sp,
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = "Verified Founder",
                tint = BsnGold,
                modifier = Modifier.size(16.dp),
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = BsnConfig.FOUNDER_NAME_BN,
              color = BsnTextPrimary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 19.sp,
            )
            Text(
              text = BsnConfig.FOUNDER_NAME_EN,
              color = BsnGold,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "${BsnConfig.FOUNDER_DESIGNATION}।",
              color = BsnGreen,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
            )
          }
        }
      }
    }

    // WhatsApp News Submission Switch Button
    item {
      BsnSwitchButton(
        title = "WhatsApp এ নিউজ পাঠান",
        subtitle = "নম্বর: ${BsnConfig.WHATSAPP_NEWS_NUMBER_INTL} • সরাসরি WhatsApp ওপেন করুন",
        brand = SocialBrand.WHATSAPP,
        accentColor = WhatsAppGreen,
        testTag = "home_whatsapp_news_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.WHATSAPP_NEWS_URL,
            ExternalLinkLauncher.TargetPlatform.WHATSAPP,
          )
        },
      )
    }

    // Facebook Personal Profile Switch Button
    item {
      BsnSwitchButton(
        title = "রিমন শিকদার - ব্যক্তিগত ফেসবুক প্রোফাইল",
        subtitle = "অফিশিয়াল Facebook প্রোফাইল ভিজিট করুন",
        brand = SocialBrand.FACEBOOK,
        accentColor = FacebookBlue,
        testTag = "home_fb_profile_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.FACEBOOK_PERSONAL_PROFILE_URL,
            ExternalLinkLauncher.TargetPlatform.FACEBOOK,
          )
        },
      )
    }

    // Exact BSN Introduction Board
    item {
      BsnInfoBoard(
        badgeTitle = "BSN পরিচিতি বোর্ড • বাংলাদেশ সাংবাদিক নেটওয়ার্ক",
        bodyText = BsnConfig.TAB1_INTRO_BOARD_TEXT,
        accentColor = BsnGold,
      )
    }

    item {
      Text(
        text = "BSN News 24 • ${BsnConfig.OFFICIAL_NAME}",
        color = BsnTextSecondary,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
      )
    }
  }
}
