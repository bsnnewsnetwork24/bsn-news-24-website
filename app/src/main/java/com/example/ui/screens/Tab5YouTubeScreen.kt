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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnChannelBannerCard
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.components.SocialBrandBadge
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.YouTubeRed
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab5YouTubeScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      BsnChannelBannerCard(
        drawableRes = R.drawable.bsn_channel_banner,
        caption = "BSN News 24 • @BSNNews24 YouTube Channel",
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.5.dp, YouTubeRed),
      ) {
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(YouTubeRed.copy(alpha = 0.22f), Color(0xFF141822))
                )
              )
              .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          SocialBrandBadge(brand = SocialBrand.YOUTUBE, size = 54.dp)
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "BSN News 24 YouTube",
              color = BsnTextPrimary,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
            )
            Text(
              text = "@bsnnews24 • Bangladesh Sangbadik Network",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp,
            )
          }
        }
      }
    }

    item {
      BsnSwitchButton(
        title = "YouTube চ্যানেলে যান",
        subtitle = "@bsnnews24 • ভিডিও ও সংবাদ প্রতিবেদন দেখুন",
        brand = SocialBrand.YOUTUBE,
        accentColor = YouTubeRed,
        testTag = "tab5_youtube_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.YOUTUBE_URL,
            ExternalLinkLauncher.TargetPlatform.YOUTUBE,
          )
        },
      )
    }

    item {
      BsnInfoBoard(
        badgeTitle = "YouTube চ্যানেল তথ্য",
        bodyText = BsnConfig.TAB5_YOUTUBE_TEXT,
        accentColor = YouTubeRed,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}
