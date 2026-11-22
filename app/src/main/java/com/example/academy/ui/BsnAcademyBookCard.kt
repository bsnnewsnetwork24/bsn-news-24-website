package com.example.academy.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.academy.data.AcademyOverviewData
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily

/**
 * Displays the user-uploaded cover artwork intact (2352x1008 natural aspect ratio, ContentScale.Fit)
 * without cropping, stretching, or altering its original design/text.
 */
@Composable
fun BsnAcademyBookCard(
  completedCount: Int,
  totalChapters: Int,
  onOpenAcademy: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .clickable(onClick = onOpenAcademy)
        .testTag("home_academy_book_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
    border =
      BorderStroke(
        1.5.dp,
        Brush.horizontalGradient(listOf(BsnGold, BsnRed, BsnGreen)),
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Exact uploaded book cover preserved uncropped at natural 2352:1008 (21:9) aspect ratio
      Box(
        modifier =
          Modifier.fillMaxWidth()
            .background(BsnBlack)
            .padding(6.dp),
        contentAlignment = Alignment.Center,
      ) {
        Image(
          painter = painterResource(id = R.drawable.bsn_academy_cover),
          contentDescription = "BSN Journalism Academy - Mass Communication & Journalism Cover",
          contentScale = ContentScale.Fit,
          modifier =
            Modifier.fillMaxWidth()
              .aspectRatio(2352f / 1008f)
              .clip(RoundedCornerShape(12.dp)),
        )
      }

      Column(
        modifier =
          Modifier.fillMaxWidth()
            .background(
              Brush.verticalGradient(
                listOf(Color(0xFF151923), Color(0xFF0E131D))
              )
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(6.dp))
                .background(BsnRed)
                .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = AcademyOverviewData.ACADEMY_TITLE_EN,
              color = Color.White,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 11.sp,
            )
          }
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(6.dp))
                .background(BsnGreen.copy(alpha = 0.22f))
                .border(1.dp, BsnGreen.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .padding(horizontal = 7.dp, vertical = 2.dp)
          ) {
            Text(
              text = "৪ বছর • ৮ সেমিস্টার",
              color = BsnGreen,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = AcademyOverviewData.ACADEMY_TITLE_BILINGUAL,
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          lineHeight = 22.sp,
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = AcademyOverviewData.ACADEMY_SUBTITLE_EN,
          color = BsnGold,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
        )

        Text(
          text = AcademyOverviewData.ACADEMY_SUBTITLE_BN,
          color = BsnTextSecondary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          lineHeight = 19.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier =
            Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Brush.horizontalGradient(listOf(BsnRed, Color(0xFF8E131D))))
              .border(1.dp, BsnGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(horizontal = 12.dp, vertical = 9.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.School,
              contentDescription = "Academy Lessons",
              tint = BsnGold,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text =
                if (completedCount > 0)
                  "পড়া চালিয়ে যান • সম্পন্ন $completedCount/$totalChapters"
                else "একাডেমি খুলুন ও পড়া শুরু করুন",
              color = Color.White,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
              Modifier.clip(RoundedCornerShape(50))
                .background(BsnBlack.copy(alpha = 0.55f))
                .border(1.dp, BsnOutline, RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 3.dp),
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = null,
              tint = BsnGold,
              modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
              imageVector = Icons.Filled.PlayArrow,
              contentDescription = "Open Academy",
              tint = Color.White,
              modifier = Modifier.size(14.dp),
            )
          }
        }
      }
    }
  }
}
