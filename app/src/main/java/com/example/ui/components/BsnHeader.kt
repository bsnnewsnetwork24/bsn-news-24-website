package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurface
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily

@Composable
fun BsnFixedHeader(
  currentTabTitle: String,
  modifier: Modifier = Modifier,
) {
  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val liveAlpha by
    infiniteTransition.animateFloat(
      initialValue = 1f,
      targetValue = 0.25f,
      animationSpec = infiniteRepeatable(tween(750), RepeatMode.Reverse),
      label = "live_alpha",
    )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = BsnBlack,
    tonalElevation = 8.dp,
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color(0xFF140507), BsnSurface, BsnBlack)
            )
          )
          .statusBarsPadding()
          .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        // Official BSN Logo Crest
        Image(
          painter = painterResource(id = R.drawable.bsn_official_logo),
          contentDescription = "BSN News 24 লোগো",
          contentScale = ContentScale.Crop,
          modifier =
            Modifier.size(46.dp)
              .clip(CircleShape)
              .border(2.dp, BsnGold, CircleShape),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = BsnConfig.FIXED_HEADER_WELCOME,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 17.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // LIVE Badge
        Column(horizontalAlignment = Alignment.End) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
              Modifier.clip(RoundedCornerShape(50))
                .background(BsnRed)
                .border(1.dp, BsnGold.copy(alpha = 0.7f), RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 3.dp),
          ) {
            Box(
              modifier =
                Modifier.size(7.dp)
                  .alpha(liveAlpha)
                  .clip(CircleShape)
                  .background(Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LIVE",
              color = Color.White,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 10.sp,
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "BSN 24",
            color = BsnGold,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Breaking News Ticker Bar inspired by uploaded Website design (Image 9)
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BsnSurfaceElevated)
            .border(
              1.dp,
              Brush.horizontalGradient(listOf(BsnRed, BsnGold.copy(alpha = 0.6f), BsnGreen)),
              RoundedCornerShape(8.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier =
            Modifier.background(BsnRed)
              .padding(horizontal = 9.dp, vertical = 5.dp),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "ব্রেকিং নিউজ",
            color = Color.White,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
          )
        }
        Text(
          text = "  $currentTabTitle • ${BsnConfig.TAGLINE}",
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.5.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f).padding(end = 8.dp),
        )
      }
    }
  }
}
