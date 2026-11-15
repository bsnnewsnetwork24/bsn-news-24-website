package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.components.BsnInfoBoard
import com.example.ui.components.SocialBrand
import com.example.ui.components.SocialBrandBadge
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.ExternalLinkLauncher

@Composable
fun Tab8WhatsAppGroupScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("tab8_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Most Prominent Premium Hero Card for BSN WhatsApp Group
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
        border =
          BorderStroke(
            2.dp,
            Brush.linearGradient(listOf(WhatsAppGreen, BsnGold, BsnGreen, BsnRed)),
          ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
      ) {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  listOf(
                    Color(0xFF062919),
                    Color(0xFF0C1A16),
                    Color(0xFF0A0F18),
                  )
                )
              )
              .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
          ) {
            Image(
              painter = painterResource(id = R.drawable.bsn_official_logo),
              contentDescription = "BSN Official Logo",
              contentScale = ContentScale.Crop,
              modifier =
                Modifier.size(68.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, BsnGold, CircleShape),
            )
            Spacer(modifier = Modifier.width(12.dp))
            SocialBrandBadge(brand = SocialBrand.WHATSAPP, size = 64.dp)
          }

          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(50))
                .background(WhatsAppGreen.copy(alpha = 0.2f))
                .border(1.dp, WhatsAppGreen, RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 4.dp)
          ) {
            Text(
              text = "OFFICIAL JOURNALIST NETWORK",
              color = WhatsAppGreen,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 11.sp,
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "(BSN) ✦ বাংলাদেশ সাংবাদিক নেটওয়ার্ক",
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
          )
          Text(
            text = "BSN WhatsApp Group • Bangladesh Journalists Network",
            color = BsnGold,
            fontWeight = FontWeight.Bold,
            fontSize = 12.5.sp,
            textAlign = TextAlign.Center,
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
          ) {
            WhatsAppHighlightChip(Icons.Filled.Groups, "সকল জেলার সাংবাদিক")
            WhatsAppHighlightChip(Icons.Filled.Public, "দেশব্যাপী নেটওয়ার্ক")
            WhatsAppHighlightChip(Icons.Filled.VerifiedUser, "সরাসরি যোগাযোগ")
          }
        }
      }
    }

    // Large Attractive Switch Button for WhatsApp Group Invite
    item {
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = 82.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable {
              ExternalLinkLauncher.openUrl(
                context,
                BsnConfig.WHATSAPP_GROUP_URL,
                ExternalLinkLauncher.TargetPlatform.WHATSAPP,
              )
            }
            .testTag("tab8_whatsapp_group_btn"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A2A1A)),
        border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(WhatsAppGreen, BsnGold))),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
      ) {
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(WhatsAppGreen.copy(alpha = 0.35f), Color(0xFF0E291C))
                )
              )
              .padding(horizontal = 16.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          SocialBrandBadge(brand = SocialBrand.WHATSAPP, size = 54.dp)
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "BSN WhatsApp Group এ যুক্ত হোন",
              color = Color.White,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "ইনভাইটেশন ওপেন করতে ট্যাপ করুন • WhatsApp",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp,
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
              Modifier.clip(RoundedCornerShape(50))
                .background(WhatsAppGreen)
                .padding(horizontal = 14.dp, vertical = 10.dp),
          ) {
            Text(
              text = "JOIN",
              color = BsnBlack,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Join WhatsApp Group",
              tint = BsnBlack,
              modifier = Modifier.size(16.dp),
            )
          }
        }
      }
    }

    // Exact Premium Information Board
    item {
      BsnInfoBoard(
        badgeTitle = "গুরুত্বপূর্ণ নির্দেশনা • BSN WhatsApp Group",
        bodyText = BsnConfig.TAB8_WHATSAPP_GROUP_TEXT,
        accentColor = WhatsAppGreen,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}

@Composable
private fun WhatsAppHighlightChip(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier =
      Modifier.clip(RoundedCornerShape(12.dp))
        .background(Color(0xFF10241A))
        .border(1.dp, WhatsAppGreen.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
        .padding(horizontal = 8.dp, vertical = 7.dp),
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = WhatsAppGreen,
      modifier = Modifier.size(18.dp),
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = label,
      color = BsnTextSecondary,
      fontFamily = HindSiliguriFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 10.5.sp,
    )
  }
}
