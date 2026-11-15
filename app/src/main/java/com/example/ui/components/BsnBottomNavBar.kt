package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnSurface
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextMuted
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.HindSiliguriFontFamily

data class BsnTabItem(
  val index: Int,
  val shortLabel: String,
  val fullTitle: String,
  val icon: ImageVector,
  val testTag: String,
)

val bsnTabs =
  listOf(
    BsnTabItem(0, "হোম পেজ", "হোম পেজ", Icons.Filled.Home, "tab_home"),
    BsnTabItem(
      1,
      "FB গ্রুপ + TG",
      "BSN News 24 Facebook Group + Telegram",
      Icons.AutoMirrored.Filled.Send,
      "tab_fb_group_telegram",
    ),
    BsnTabItem(
      2,
      "FB পেজ",
      "BSN News 24 Facebook Page",
      Icons.Filled.Public,
      "tab_fb_page",
    ),
    BsnTabItem(3, "Instagram", "Instagram", Icons.Filled.CameraAlt, "tab_instagram"),
    BsnTabItem(4, "YouTube", "YouTube", Icons.Filled.PlayCircleFilled, "tab_youtube"),
    BsnTabItem(5, "TikTok", "TikTok", Icons.Filled.MusicNote, "tab_tiktok"),
    BsnTabItem(6, "Website", "Google Website", Icons.Filled.Language, "tab_website"),
    BsnTabItem(7, "WA গ্রুপ", "BSN WhatsApp Group", Icons.Filled.Groups, "tab_whatsapp_group"),
    BsnTabItem(8, "আমার লিংক", "আমার নিউজ লিংক", Icons.Filled.BookmarkAdded, "tab_my_links"),
    BsnTabItem(
      9,
      "প্রোফাইল ও আইডি",
      "ব্যক্তিগত প্রোফাইল ও ডিজিটাল আইডি কার্ড",
      Icons.Filled.Badge,
      "tab_profile_id",
    ),
  )

@Composable
fun BsnBottomNavBar(
  selectedIndex: Int,
  onTabSelected: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val listState = rememberLazyListState()

  LaunchedEffect(selectedIndex) {
    listState.animateScrollToItem(index = (selectedIndex - 1).coerceAtLeast(0))
  }

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = BsnBlack,
    tonalElevation = 12.dp,
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth()
          .background(
            Brush.verticalGradient(
              listOf(BsnSurface, BsnBlack)
            )
          )
          .border(1.dp, BsnOutline.copy(alpha = 0.7f))
          .navigationBarsPadding()
          .padding(top = 6.dp, bottom = 6.dp)
    ) {
      // Subtle top accent line
      Box(
        modifier =
          Modifier.fillMaxWidth()
            .height(2.dp)
            .background(Brush.horizontalGradient(listOf(BsnRed, BsnGold, BsnGreen)))
      )

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth().testTag("bottom_nav_row"),
      ) {
        itemsIndexed(bsnTabs, key = { _, item -> item.index }) { index, tab ->
          val isSelected = index == selectedIndex
          val bgColor by
            animateColorAsState(
              targetValue = if (isSelected) BsnRed else BsnSurfaceElevated,
              animationSpec = spring(),
              label = "tab_bg",
            )
          val borderColor by
            animateColorAsState(
              targetValue = if (isSelected) BsnGold else BsnOutline,
              animationSpec = spring(),
              label = "tab_border",
            )
          val contentColor by
            animateColorAsState(
              targetValue = if (isSelected) Color.White else BsnTextMuted,
              animationSpec = spring(),
              label = "tab_fg",
            )

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier =
              Modifier.defaultMinSize(minWidth = 74.dp, minHeight = 52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
                .clickable { onTabSelected(index) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag(tab.testTag),
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${index + 1}.",
                color = if (isSelected) BsnGold else BsnTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
              )
              Spacer(modifier = Modifier.width(3.dp))
              Icon(
                imageVector = tab.icon,
                contentDescription = tab.fullTitle,
                tint = contentColor,
                modifier = Modifier.size(18.dp),
              )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = tab.shortLabel,
              color = contentColor,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
              fontSize = 10.5.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}
