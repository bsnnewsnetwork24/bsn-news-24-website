package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BloggerOrange
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.theme.InstagramOrange
import com.example.ui.theme.InstagramPink
import com.example.ui.theme.InstagramPurple
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.YouTubeRed

enum class SocialBrand {
  WHATSAPP,
  FACEBOOK,
  TELEGRAM,
  INSTAGRAM,
  YOUTUBE,
  TIKTOK,
  WEBSITE,
}

@Composable
fun SocialBrandBadge(
  brand: SocialBrand,
  size: Dp = 46.dp,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.size(size).clip(RoundedCornerShape(14.dp)),
    contentAlignment = Alignment.Center,
  ) {
    when (brand) {
      SocialBrand.WHATSAPP -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Brush.linearGradient(listOf(Color(0xFF25D366), Color(0xFF128C7E))))
              .border(1.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Canvas(modifier = Modifier.size(size * 0.58f)) {
            val w = this.size.width
            val h = this.size.height
            drawCircle(
              color = Color.White,
              radius = w * 0.44f,
              style = Stroke(width = w * 0.11f),
            )
            val tail =
              Path().apply {
                moveTo(w * 0.16f, h * 0.72f)
                lineTo(w * 0.06f, h * 0.94f)
                lineTo(w * 0.30f, h * 0.84f)
                close()
              }
            drawPath(tail, Color.White)
            // Phone receiver arc inside
            drawArc(
              color = Color.White,
              startAngle = 25f,
              sweepAngle = 140f,
              useCenter = false,
              topLeft = Offset(w * 0.28f, h * 0.28f),
              size = Size(w * 0.44f, h * 0.44f),
              style = Stroke(width = w * 0.11f, cap = StrokeCap.Round),
            )
          }
        }
      }
      SocialBrand.FACEBOOK -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Brush.linearGradient(listOf(FacebookBlue, Color(0xFF0D47A1))))
              .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "f",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.62f).sp,
          )
        }
      }
      SocialBrand.TELEGRAM -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Brush.linearGradient(listOf(Color(0xFF37AEE2), TelegramBlue)))
              .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Canvas(modifier = Modifier.size(size * 0.54f)) {
            val w = this.size.width
            val h = this.size.height
            val plane =
              Path().apply {
                moveTo(w * 0.94f, h * 0.12f)
                lineTo(w * 0.05f, h * 0.48f)
                lineTo(w * 0.38f, h * 0.62f)
                lineTo(w * 0.78f, h * 0.28f)
                lineTo(w * 0.48f, h * 0.68f)
                lineTo(w * 0.76f, h * 0.88f)
                close()
              }
            drawPath(plane, Color.White)
          }
        }
      }
      SocialBrand.INSTAGRAM -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(
                Brush.linearGradient(listOf(InstagramPurple, InstagramPink, InstagramOrange))
              )
              .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Canvas(modifier = Modifier.size(size * 0.55f)) {
            val w = this.size.width
            drawRoundRect(
              color = Color.White,
              cornerRadius = CornerRadius(w * 0.25f, w * 0.25f),
              style = Stroke(width = w * 0.1f),
            )
            drawCircle(
              color = Color.White,
              radius = w * 0.21f,
              style = Stroke(width = w * 0.1f),
            )
            drawCircle(
              color = Color.White,
              radius = w * 0.06f,
              center = Offset(w * 0.76f, w * 0.24f),
            )
          }
        }
      }
      SocialBrand.YOUTUBE -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Brush.linearGradient(listOf(YouTubeRed, Color(0xFFB71C1C))))
              .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Canvas(modifier = Modifier.size(size * 0.5f)) {
            val w = this.size.width
            val h = this.size.height
            val tri =
              Path().apply {
                moveTo(w * 0.24f, h * 0.16f)
                lineTo(w * 0.86f, h * 0.50f)
                lineTo(w * 0.24f, h * 0.84f)
                close()
              }
            drawPath(tri, Color.White)
          }
        }
      }
      SocialBrand.TIKTOK -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Color(0xFF0A0A0C))
              .border(
                1.5.dp,
                Brush.linearGradient(listOf(TikTokCyan, TikTokPink)),
                RoundedCornerShape(14.dp),
              ),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "♪",
            color = TikTokCyan,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.56f).sp,
          )
        }
      }
      SocialBrand.WEBSITE -> {
        Box(
          modifier =
            Modifier.matchParentSize()
              .background(Brush.linearGradient(listOf(BloggerOrange, Color(0xFFD84315))))
              .border(1.5.dp, BsnGold.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "B",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.56f).sp,
          )
        }
      }
    }
  }
}

@Composable
fun BsnSwitchButton(
  title: String,
  subtitle: String,
  brand: SocialBrand,
  accentColor: Color,
  testTag: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier =
      modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 68.dp)
        .clip(RoundedCornerShape(18.dp))
        .clickable(onClick = onClick)
        .testTag(testTag),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
    border =
      BorderStroke(
        1.5.dp,
        Brush.horizontalGradient(listOf(accentColor, BsnGold.copy(alpha = 0.7f))),
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
  ) {
    Row(
      modifier =
        Modifier.fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(accentColor.copy(alpha = 0.16f), Color.Transparent)
            )
          )
          .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      SocialBrandBadge(brand = brand, size = 46.dp)
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 15.5.sp,
          lineHeight = 21.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      // Switch-style pill toggle visual indicator
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
          Modifier.clip(RoundedCornerShape(50))
            .background(accentColor)
            .padding(horizontal = 10.dp, vertical = 7.dp),
      ) {
        Text(
          text = "ON",
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 11.sp,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = title,
          tint = Color.White,
          modifier = Modifier.size(16.dp),
        )
      }
    }
  }
}

@Composable
fun BsnInfoBoard(
  badgeTitle: String,
  bodyText: String,
  accentColor: Color = BsnGold,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    color = BsnSurfaceElevated,
    border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.55f)),
    tonalElevation = 4.dp,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(
          modifier =
            Modifier.size(10.dp)
              .clip(CircleShape)
              .background(accentColor)
        )
        Text(
          text = badgeTitle,
          color = accentColor,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
        )
      }
      Spacer(modifier = Modifier.height(10.dp))
      Box(
        modifier =
          Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C1018))
            .border(1.dp, BsnOutline, RoundedCornerShape(12.dp))
            .padding(14.dp)
      ) {
        Text(
          text = bodyText,
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.5.sp,
          lineHeight = 23.sp,
        )
      }
    }
  }
}

@Composable
fun BsnChannelBannerCard(
  drawableRes: Int = R.drawable.bsn_channel_banner,
  caption: String = "BSN News 24 • বাংলাদেশ সাংবাদিক নেটওয়ার্ক",
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
    border =
      BorderStroke(
        1.5.dp,
        Brush.horizontalGradient(listOf(BsnRed, BsnGold, BsnGreen)),
      ),
  ) {
    Column {
      Image(
        painter = painterResource(id = drawableRes),
        contentDescription = caption,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().height(112.dp),
      )
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .background(Color(0xFF0B0E15))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = caption,
          color = BsnTextSecondary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.5.sp,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Verified",
            tint = BsnGreen,
            modifier = Modifier.size(15.dp),
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "PRESS",
            color = BsnGold,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
          )
        }
      }
    }
  }
}
