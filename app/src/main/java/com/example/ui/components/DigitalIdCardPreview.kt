package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.config.BsnConfig
import com.example.data.local.JournalistProfile
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnFlagGreen
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnRedDark
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnTextMuted
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily
import java.io.File

@Composable
fun DigitalIdCardPreview(
  profile: JournalistProfile,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0F17)),
    border =
      BorderStroke(
        2.dp,
        Brush.linearGradient(listOf(BsnGold, Color(0xFFFFF0A5), Color(0xFFC5941F), BsnGold)),
      ),
    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Top Red Header Ribbon with BSN Logo & Bangladesh Flag
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(BsnRedDark, BsnRed)))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Image(
          painter = painterResource(id = R.drawable.bsn_official_logo),
          contentDescription = "BSN News 24 লোগো",
          contentScale = ContentScale.Crop,
          modifier =
            Modifier.size(54.dp)
              .clip(CircleShape)
              .border(2.dp, BsnGold, CircleShape),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "BSN NEWS 24",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
          )
          Text(
            text = "BANGLADESH SANGBADIK NETWORK",
            color = Color(0xFFFFE082),
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
          )
          Spacer(modifier = Modifier.height(3.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier =
                Modifier.clip(RoundedCornerShape(6.dp))
                  .background(BsnGold)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "PRESS",
                color = BsnBlack,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = BsnConfig.TAGLINE,
              color = Color.White,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
            )
          }
        }
        Spacer(modifier = Modifier.width(6.dp))
        // Bangladesh National Flag
        BangladeshFlagMini(modifier = Modifier.size(width = 46.dp, height = 28.dp))
      }

      Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // User Photograph
        val photoFile =
          if (profile.photoLocalPath.isNotBlank()) File(profile.photoLocalPath) else null
        Box(
          modifier =
            Modifier.size(116.dp)
              .clip(RoundedCornerShape(18.dp))
              .background(BsnSurfaceCard)
              .border(2.5.dp, BsnGreen, RoundedCornerShape(18.dp)),
          contentAlignment = Alignment.Center,
        ) {
          if (photoFile != null && photoFile.exists()) {
            AsyncImage(
              model = photoFile,
              contentDescription = "সাংবাদিকের ছবি",
              contentScale = ContentScale.Crop,
              modifier = Modifier.matchParentSize(),
            )
          } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "ছবি নেই",
                tint = BsnTextMuted,
                modifier = Modifier.size(44.dp),
              )
              Text(
                text = "ছবি নেই",
                color = BsnTextMuted,
                fontFamily = HindSiliguriFontFamily,
                fontSize = 11.sp,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = profile.name,
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
          modifier =
            Modifier.clip(RoundedCornerShape(50))
              .background(BsnGreen)
              .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
          Text(
            text = profile.designation,
            color = BsnBlack,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Details Table (STRICTLY NO SERIAL NUMBER)
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF131926))
              .border(1.dp, BsnOutline, RoundedCornerShape(14.dp))
              .padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          IdCardDetailRow("পিতার নাম", profile.fatherName.ifBlank { "-" })
          HorizontalDivider(color = BsnOutline.copy(alpha = 0.6f))
          IdCardDetailRow("পদবি", profile.designation)
          HorizontalDivider(color = BsnOutline.copy(alpha = 0.6f))
          IdCardDetailRow("মোবাইল নম্বর", profile.mobileNumber)
          HorizontalDivider(color = BsnOutline.copy(alpha = 0.6f))
          IdCardDetailRow("জেলা", profile.district)
          HorizontalDivider(color = BsnOutline.copy(alpha = 0.6f))
          IdCardDetailRow("রক্তের গ্রুপ", profile.bloodGroup.ifBlank { "-" })
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Exact Required Proof Statement
        Box(
          modifier =
            Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Brush.horizontalGradient(listOf(BsnRedDark, BsnFlagGreen)))
              .border(1.dp, BsnGold, RoundedCornerShape(12.dp))
              .padding(vertical = 10.dp, horizontal = 12.dp),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = BsnConfig.TAB10_ID_CARD_PROOF_TEXT,
            color = Color.White,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Composable
private fun IdCardDetailRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = "$label:",
      color = BsnGold,
      fontFamily = HindSiliguriFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      modifier = Modifier.width(108.dp),
    )
    Text(
      text = value,
      color = BsnTextPrimary,
      fontFamily = HindSiliguriFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.5.sp,
      modifier = Modifier.weight(1f),
    )
  }
}

@Composable
fun BangladeshFlagMini(modifier: Modifier = Modifier) {
  Canvas(
    modifier =
      modifier
        .clip(RoundedCornerShape(4.dp))
        .border(1.dp, BsnGold.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
  ) {
    drawRect(color = BsnFlagGreen)
    drawCircle(
      color = Color(0xFFF42A41),
      radius = size.height * 0.31f,
      center = Offset(size.width * 0.45f, size.height * 0.5f),
    )
  }
}
