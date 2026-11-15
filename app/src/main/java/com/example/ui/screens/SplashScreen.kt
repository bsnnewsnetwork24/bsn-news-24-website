package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.BsnConfig
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
  val scale = remember { Animatable(0.82f) }
  val alpha = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    scale.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
  }
  LaunchedEffect(Unit) {
    alpha.animateTo(1f, animationSpec = tween(550))
    delay(850)
    onFinished()
  }

  Box(
    modifier =
      Modifier.fillMaxSize()
        .background(
          Brush.radialGradient(
            colors = listOf(Color(0xFF280507), Color(0xFF0A1410), BsnBlack)
          )
        )
        .padding(24.dp),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.scale(scale.value).alpha(alpha.value),
    ) {
      Box(
        modifier =
          Modifier.size(180.dp)
            .clip(CircleShape)
            .border(
              3.dp,
              Brush.sweepGradient(listOf(BsnGold, BsnRed, BsnGreen, BsnGold)),
              CircleShape,
            )
            .padding(4.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.bsn_official_logo),
          contentDescription = "BSN News 24 Official Logo",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize().clip(CircleShape),
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "BSN ",
          color = BsnGreen,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 34.sp,
        )
        Text(
          text = "News 24",
          color = BsnRed,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 34.sp,
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Box(
        modifier =
          Modifier.clip(RoundedCornerShape(50))
            .background(Color(0xFF151B26))
            .border(1.dp, BsnGold.copy(alpha = 0.7f), RoundedCornerShape(50))
            .padding(horizontal = 18.dp, vertical = 7.dp)
      ) {
        Text(
          text = BsnConfig.TAGLINE,
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          textAlign = TextAlign.Center,
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Bangladesh Sangbadik Network",
        color = BsnTextSecondary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
      )

      Spacer(modifier = Modifier.height(28.dp))

      LinearProgressIndicator(
        modifier = Modifier.width(150.dp).height(4.dp).clip(RoundedCornerShape(50)),
        color = BsnGold,
        trackColor = Color(0xFF1E2636),
      )
    }
  }
}
