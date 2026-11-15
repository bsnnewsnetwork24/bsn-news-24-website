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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.BsnSwitchButton
import com.example.ui.components.SocialBrand
import com.example.ui.theme.BloggerOrange
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab7WebsiteScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // Website Preview-Style Card modeled after Image 9 (bsnnews24.blogspot.com)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border = BorderStroke(1.5.dp, BloggerOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Browser Address Bar Chrome
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .background(Color(0xFF0A0D14))
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BsnRed))
            Spacer(modifier = Modifier.width(5.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BsnGold))
            Spacer(modifier = Modifier.width(5.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BsnGreen))
            Spacer(modifier = Modifier.width(10.dp))
            Row(
              modifier =
                Modifier.weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF171E2C))
                  .border(1.dp, BsnOutline, RoundedCornerShape(8.dp))
                  .padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "HTTPS",
                tint = BsnGreen,
                modifier = Modifier.size(13.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "bsnnews24.blogspot.com",
                color = BsnTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
              )
            }
          }

          // Website Header Preview matching uploaded Image 9
          Column(
            modifier =
              Modifier.fillMaxWidth()
                .background(
                  Brush.verticalGradient(listOf(Color(0xFF423A3E), Color(0xFF1B2330)))
                )
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
              )
              Text(
                text = "WEB PORTAL PREVIEW",
                color = BsnGold,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
              )
              Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
              )
            }

            // Red/White Breaking News Strip from Image 9
            Row(
              modifier =
                Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.White)
                  .border(1.5.dp, BsnRed, RoundedCornerShape(8.dp)),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Box(
                modifier = Modifier.background(BsnRed).padding(horizontal = 10.dp, vertical = 7.dp)
              ) {
                Text(
                  text = "ব্রেকিং নিউজ",
                  color = Color.White,
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                )
              }
              Text(
                text = "  BSN News 24 - সব খবর সবার আগে",
                color = Color(0xFF111111),
                fontFamily = HindSiliguriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
              )
            }

            Image(
              painter = painterResource(id = R.drawable.bsn_press_banner),
              contentDescription = "BSN News 24 Website Banner",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(10.dp)),
            )
          }
        }
      }
    }

    item {
      BsnSwitchButton(
        title = "Google Website ভিজিট করুন",
        subtitle = "https://bsnnews24.blogspot.com • ব্রাউজারে ওপেন করুন",
        brand = SocialBrand.WEBSITE,
        accentColor = BloggerOrange,
        testTag = "tab7_website_btn",
        onClick = {
          ExternalLinkLauncher.openUrl(
            context,
            BsnConfig.WEBSITE_URL,
            ExternalLinkLauncher.TargetPlatform.BROWSER,
          )
        },
      )
    }

    item {
      BsnInfoBoard(
        badgeTitle = "ওয়েবসাইট পোর্টাল তথ্য",
        bodyText = BsnConfig.TAB7_WEBSITE_TEXT,
        accentColor = BloggerOrange,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}
