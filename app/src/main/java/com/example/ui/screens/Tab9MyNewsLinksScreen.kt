package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.config.BsnConfig
import com.example.data.local.NewsLinkEntity
import com.example.ui.components.BsnInfoBoard
import com.example.ui.theme.BsnBlack
import com.example.ui.theme.BsnGold
import com.example.ui.theme.BsnGreen
import com.example.ui.theme.BsnOutline
import com.example.ui.theme.BsnRed
import com.example.ui.theme.BsnRedGlow
import com.example.ui.theme.BsnSurfaceCard
import com.example.ui.theme.BsnSurfaceElevated
import com.example.ui.theme.BsnTextMuted
import com.example.ui.theme.BsnTextPrimary
import com.example.ui.theme.BsnTextSecondary
import com.example.ui.theme.HindSiliguriFontFamily
import com.example.ui.viewmodel.NewsLinksViewModel
import com.example.utils.ExternalLinkLauncher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Tab9MyNewsLinksScreen(
  viewModel: NewsLinksViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  var showAddEditDialog by remember { mutableStateOf(false) }
  var editingLink by remember { mutableStateOf<NewsLinkEntity?>(null) }
  var deletingLink by remember { mutableStateOf<NewsLinkEntity?>(null) }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("tab9_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    // Required Subtitle Banner
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BsnSurfaceCard,
        border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(BsnRed, BsnGold, BsnGreen))),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = Icons.Filled.Bookmark,
            contentDescription = null,
            tint = BsnGold,
            modifier = Modifier.size(26.dp),
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = BsnConfig.TAB9_SUBTITLE_TEXT,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
          )
        }
      }
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = state.searchQuery,
        onValueChange = viewModel::onSearchQueryChanged,
        modifier = Modifier.fillMaxWidth().testTag("news_link_search_input"),
        placeholder = {
          Text(
            text = "শিরোনাম বা লিংক দিয়ে খুঁজুন...",
            color = BsnTextMuted,
            fontFamily = HindSiliguriFontFamily,
          )
        },
        leadingIcon = {
          Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = BsnGold)
        },
        trailingIcon = {
          if (state.searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
              Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = BsnTextMuted)
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors =
          OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BsnGold,
            unfocusedBorderColor = BsnOutline,
            focusedTextColor = BsnTextPrimary,
            unfocusedTextColor = BsnTextPrimary,
            focusedContainerColor = BsnSurfaceElevated,
            unfocusedContainerColor = BsnSurfaceElevated,
          ),
      )
    }

    // Add Link Button (100% Offline In-App Persistence)
    item {
      Button(
        onClick = {
          editingLink = null
          showAddEditDialog = true
        },
        modifier =
          Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .testTag("add_news_link_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BsnRed, contentColor = Color.White),
      ) {
        Icon(imageVector = Icons.Filled.AddLink, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "নতুন লিংক যুক্ত করুন",
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 15.5.sp,
        )
      }
    }

    // Status or Error feedback banner
    if (state.statusMessage != null || state.errorMessage != null) {
      item {
        val isError = state.errorMessage != null
        val text = state.errorMessage ?: state.statusMessage.orEmpty()
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          color = if (isError) Color(0xFF2D0E12) else Color(0xFF0E291B),
          border = BorderStroke(1.dp, if (isError) BsnRedGlow else BsnGreen),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = text,
              color = Color.White,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              modifier = Modifier.weight(1f),
            )
            IconButton(onClick = viewModel::clearMessages) {
              Icon(imageVector = Icons.Filled.Close, contentDescription = "বন্ধ করুন", tint = Color.White)
            }
          }
        }
      }
    }

    // Saved Links List Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "সংরক্ষিত নিউজ লিংক (${state.links.size})",
          color = BsnGold,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
        )
        Text(
          text = "অফলাইন স্টোরেজ",
          color = BsnGreen,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
        )
      }
    }

    if (state.links.isEmpty()) {
      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          color = BsnSurfaceCard,
          border = BorderStroke(1.dp, BsnOutline),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Icon(
              imageVector = Icons.Filled.AddLink,
              contentDescription = null,
              tint = BsnTextMuted,
              modifier = Modifier.size(38.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text =
                if (state.searchQuery.isNotBlank()) "আপনার খোঁজা শিরোনামে কোনো লিংক পাওয়া যায়নি।"
                else "এখনো কোনো নিউজ লিংক যুক্ত করা হয়নি।\nউপরের \"নতুন লিংক যুক্ত করুন\" বাটনে চাপ দিন।",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
            )
          }
        }
      }
    } else {
      items(state.links, key = { it.id }) { item ->
        SavedNewsLinkCard(
          item = item,
          onOpenClick = {
            ExternalLinkLauncher.openUrl(
              context,
              item.url,
              ExternalLinkLauncher.TargetPlatform.BROWSER,
            )
          },
          onEditClick = {
            editingLink = item
            showAddEditDialog = true
          },
          onDeleteClick = { deletingLink = item },
        )
      }
    }

    // Offline In-App Persistence Info Board
    item {
      Spacer(modifier = Modifier.height(4.dp))
      BsnInfoBoard(
        badgeTitle = "গুরুত্বপূর্ণ তথ্য • অফলাইন লিংক সংরক্ষণ",
        bodyText =
          "আপনার সংরক্ষিত সব নিউজ লিংক সম্পূর্ণ অফলাইনে এই অ্যাপের ভেতরেই জমা থাকবে।\n" +
            "অ্যাপটি যতক্ষণ আপনার মোবাইলে ইনস্টল থাকবে, ইন্টারনেট বা লগইন ছাড়াই যেকোনো সময় লিংকগুলো দেখতে, এডিট করতে ও ওপেন করতে পারবেন।",
        accentColor = BsnGold,
      )
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }

  if (showAddEditDialog) {
    AddEditLinkDialog(
      initialHeadline = editingLink?.headline ?: "",
      initialUrl = editingLink?.url ?: "",
      isEditing = editingLink != null,
      onDismiss = { showAddEditDialog = false },
      onSave = { headline, url ->
        viewModel.saveNewsLink(headline, url, existingId = editingLink?.id)
        showAddEditDialog = false
      },
    )
  }

  deletingLink?.let { target ->
    AlertDialog(
      onDismissRequest = { deletingLink = null },
      containerColor = BsnSurfaceCard,
      title = {
        Text(
          text = "লিংকটি মুছে ফেলবেন?",
          color = BsnTextPrimary,
          fontFamily = HindSiliguriFontFamily,
          fontWeight = FontWeight.Bold,
        )
      },
      text = {
        Text(
          text = target.headline,
          color = BsnTextSecondary,
          fontFamily = HindSiliguriFontFamily,
        )
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteNewsLink(target)
            deletingLink = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = BsnRed),
        ) {
          Text("মুছে ফেলুন", fontFamily = HindSiliguriFontFamily, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { deletingLink = null }) {
          Text("বাতিল", color = BsnTextSecondary, fontFamily = HindSiliguriFontFamily)
        }
      },
    )
  }
}

@Composable
private fun SavedNewsLinkCard(
  item: NewsLinkEntity,
  onOpenClick: () -> Unit,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
) {
  val dateStr = remember(item.updatedAtMillis) {
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(item.updatedAtMillis))
  }

  Card(
    modifier =
      Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .clickable(onClick = onOpenClick)
        .testTag("saved_link_card_${item.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
    border = BorderStroke(1.dp, BsnOutline),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.headline,
            color = BsnTextPrimary,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.5.sp,
            lineHeight = 22.sp,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = item.url,
            color = BsnGold,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.OpenInNew,
          contentDescription = "নিউজ ওপেন করুন",
          tint = BsnGreen,
          modifier = Modifier.size(20.dp),
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier =
            Modifier.clip(RoundedCornerShape(50))
              .background(Color(0xFF0E291B))
              .border(1.dp, BsnGreen.copy(alpha = 0.6f), RoundedCornerShape(50))
              .padding(horizontal = 9.dp, vertical = 4.dp),
        ) {
          Icon(
            imageVector = Icons.Filled.PhoneAndroid,
            contentDescription = null,
            tint = BsnGreen,
            modifier = Modifier.size(13.dp),
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "অ্যাপে সংরক্ষিত",
            color = BsnGreen,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 10.5.sp,
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = dateStr,
            color = BsnTextMuted,
            fontSize = 10.5.sp,
          )
          IconButton(onClick = onEditClick, modifier = Modifier.size(48.dp)) {
            Icon(
              imageVector = Icons.Filled.Edit,
              contentDescription = "এডিট করুন",
              tint = BsnTextSecondary,
              modifier = Modifier.size(18.dp),
            )
          }
          IconButton(onClick = onDeleteClick, modifier = Modifier.size(48.dp)) {
            Icon(
              imageVector = Icons.Filled.Delete,
              contentDescription = "মুছে ফেলুন",
              tint = BsnRedGlow,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AddEditLinkDialog(
  initialHeadline: String,
  initialUrl: String,
  isEditing: Boolean,
  onDismiss: () -> Unit,
  onSave: (String, String) -> Unit,
) {
  var headline by remember { mutableStateOf(initialHeadline) }
  var link by remember { mutableStateOf(initialUrl) }
  var validationError by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = BsnSurfaceCard,
    title = {
      Text(
        text = if (isEditing) "নিউজ লিংক পরিবর্তন করুন" else "নতুন লিংক যুক্ত করুন",
        color = BsnGold,
        fontFamily = HindSiliguriFontFamily,
        fontWeight = FontWeight.Bold,
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = headline,
          onValueChange = {
            headline = it
            validationError = null
          },
          label = { Text("Headline (নিউজের শিরোনাম)", fontFamily = HindSiliguriFontFamily) },
          modifier = Modifier.fillMaxWidth().testTag("dialog_headline_input"),
          maxLines = 3,
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BsnGold,
              unfocusedBorderColor = BsnOutline,
              focusedTextColor = BsnTextPrimary,
              unfocusedTextColor = BsnTextPrimary,
              focusedLabelColor = BsnGold,
              unfocusedLabelColor = BsnTextMuted,
            ),
        )
        OutlinedTextField(
          value = link,
          onValueChange = {
            link = it
            validationError = null
          },
          label = { Text("Link (নিউজ লিংক)", fontFamily = HindSiliguriFontFamily) },
          placeholder = { Text("https://...") },
          modifier = Modifier.fillMaxWidth().testTag("dialog_link_input"),
          singleLine = true,
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BsnGold,
              unfocusedBorderColor = BsnOutline,
              focusedTextColor = BsnTextPrimary,
              unfocusedTextColor = BsnTextPrimary,
              focusedLabelColor = BsnGold,
              unfocusedLabelColor = BsnTextMuted,
            ),
        )
        if (validationError != null) {
          Text(
            text = validationError.orEmpty(),
            color = BsnRedGlow,
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.5.sp,
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (headline.isBlank() || link.isBlank()) {
            validationError = "অনুগ্রহ করে Headline এবং Link উভয়ই পূরণ করুন।"
          } else {
            onSave(headline, link)
          }
        },
        modifier = Modifier.testTag("dialog_save_link_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = BsnGreen, contentColor = BsnBlack),
      ) {
        Text("সংরক্ষণ করুন", fontFamily = HindSiliguriFontFamily, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("বাতিল", color = BsnTextSecondary, fontFamily = HindSiliguriFontFamily)
      }
    },
  )
}
