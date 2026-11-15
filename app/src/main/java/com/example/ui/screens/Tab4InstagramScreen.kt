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
import com.example.config.BsnConfig
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.components.SocialBrandBadge
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.InstagramOrange
import com.example.ui.theme.InstagramPink
import com.example.ui.theme.InstagramPurple
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab4InstagramScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border =
          BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(listOf(InstagramPurple, InstagramPink, InstagramOrange)),
          ),
      ) {
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(
                    InstagramPurple.copy(alpha = 0.25f),
                    InstagramPink.copy(alpha = 0.18f),
                    Color(0xFF121620),
                  )
                )
              )
              .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          SocialBrandBadge(brand = SocialBrand.INSTAGRAM, size = 56.dp)
          Spacer(modifier = Modifier.width(14.dp))
          Column {
            Text(
              text = "BSN News 24 • Instagram",
              color = BsnTextPrimary,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
            )
            Text(
              text = "@bsnnews24 • Breaking News 24/7",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
          }
        }
      }
    }

    item {
      BsnSwitchButton(
        title = "Instagram প্রোফাইল দেখুন",
        subtitle = "@bsnnews24 • Instagram অ্যাপ বা ব্রাউজারে ওপেন করুন",
        brand = SocialBrand.INSTAGRAM,
        accentColor = InstagramPink,
        testTag = "tab4_instagram_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.INSTAGRAM_URL,
            ExternalLinkLauncher.TargetPlatform.INSTAGRAM,
          )
        },
      )
    }

    item {
      BsnInfoBoard(
        badgeTitle = "Instagram চ্যানেল বার্তা",
        bodyText = BsnConfig.TAB4_INSTAGRAM_TEXT,
        accentColor = InstagramPink,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}
