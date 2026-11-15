package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.components.DigitalIdCardPreview
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
import com.example.ui.viewmodel.ProfileIdViewModel
import java.io.File

private val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tab10ProfileIdCardScreen(
  viewModel: ProfileIdViewModel,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val activity = context.findActivity()
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  val photoPickerLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.PickVisualMedia(),
      onResult = { uri ->
        if (uri != null) {
          viewModel.importPickedPhoto(uri)
        }
      },
    )

  val listState = rememberLazyListState()

  LaunchedEffect(state.showEditForm) {
    listState.scrollToItem(0)
  }

  LazyColumn(
    state = listState,
    modifier = modifier.fillMaxSize().testTag("tab10_list"),
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // Mode Switch Header if saved profile already exists
    if (state.savedProfile != null) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Button(
            onClick = { viewModel.toggleEditForm(false) },
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = if (!state.showEditForm) BsnRed else BsnSurfaceElevated,
                contentColor = Color.White,
              ),
          ) {
            Icon(imageVector = Icons.Filled.Badge, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ডিজিটাল আইডি কার্ড",
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
          }

          Button(
            onClick = { viewModel.toggleEditForm(true) },
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = if (state.showEditForm) BsnGreen else BsnSurfaceElevated,
                contentColor = if (state.showEditForm) BsnBlack else Color.White,
              ),
          ) {
            Icon(imageVector = Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "তথ্য এডিট করুন",
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
          }
        }
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

    if (state.showEditForm || state.savedProfile == null) {
      // Profile Input Form
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = BsnSurfaceCard),
          border = BorderStroke(1.2.dp, BsnGold.copy(alpha = 0.6f)),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Text(
              text = "ব্যক্তিগত প্রোফাইল ও ডিজিটাল আইডি কার্ড ফর্ম",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.5.sp,
            )

            // Photo Picker Section (Zero-Permission Android Photo Picker)
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              val photoFile =
                if (state.photoLocalPath.isNotBlank()) File(state.photoLocalPath) else null
              Box(
                modifier =
                  Modifier.size(84.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BsnSurfaceElevated)
                    .border(2.dp, BsnGreen, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
              ) {
                if (photoFile != null && photoFile.exists()) {
                  AsyncImage(
                    model = photoFile,
                    contentDescription = "নির্বাচিত ছবি",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                  )
                } else {
                  Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "ছবি নেই",
                    tint = BsnTextMuted,
                    modifier = Modifier.size(36.dp),
                  )
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              OutlinedButton(
                onClick = {
                  photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                modifier = Modifier.weight(1f).defaultMinSize(minHeight = 50.dp).testTag("pick_photo_btn"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, BsnGold),
              ) {
                Icon(
                  imageVector = Icons.Filled.AddAPhoto,
                  contentDescription = null,
                  tint = BsnGold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (state.photoLocalPath.isNotBlank()) "ছবি পরিবর্তন করুন" else "গ্যালারি থেকে ছবি আপলোড করুন",
                  color = BsnTextPrimary,
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                )
              }
            }

            ProfileTextField(
              value = state.name,
              onValueChange = viewModel::onNameChanged,
              label = "Name / নাম *",
              tag = "profile_name_input",
            )
            ProfileTextField(
              value = state.fatherName,
              onValueChange = viewModel::onFatherNameChanged,
              label = "Father Name / পিতার নাম",
              tag = "profile_father_input",
            )
            ProfileTextField(
              value = state.designation,
              onValueChange = viewModel::onDesignationChanged,
              label = "Designation / পদবি *",
              tag = "profile_designation_input",
            )
            ProfileTextField(
              value = state.mobileNumber,
              onValueChange = viewModel::onMobileChanged,
              label = "Mobile Number / মোবাইল নম্বর *",
              keyboardType = KeyboardType.Phone,
              tag = "profile_mobile_input",
            )
            ProfileTextField(
              value = state.district,
              onValueChange = viewModel::onDistrictChanged,
              label = "District / জেলা *",
              tag = "profile_district_input",
            )

            // Blood Group Quick Chips + Editable Field
            Text(
              text = "Blood Group / রক্তের গ্রুপ",
              color = BsnTextSecondary,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
            )
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              bloodGroups.forEach { group ->
                val selected = state.bloodGroup.equals(group, ignoreCase = true)
                Box(
                  modifier =
                    Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 40.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .background(if (selected) BsnRed else BsnSurfaceElevated)
                      .border(1.dp, if (selected) BsnGold else BsnOutline, RoundedCornerShape(10.dp))
                      .clickable { viewModel.onBloodGroupChanged(group) }
                      .padding(horizontal = 12.dp, vertical = 8.dp),
                  contentAlignment = Alignment.Center,
                ) {
                  Text(
                    text = group,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                  )
                }
              }
            }
            ProfileTextField(
              value = state.bloodGroup,
              onValueChange = viewModel::onBloodGroupChanged,
              label = "রক্তের গ্রুপ (যেমন: O+)",
              tag = "profile_blood_input",
            )
          }
        }
      }

      item {
        Button(
          onClick = viewModel::saveProfileAndGenerateCard,
          enabled = !state.isSaving,
          modifier =
            Modifier.fillMaxWidth()
              .defaultMinSize(minHeight = 54.dp)
              .testTag("save_profile_btn"),
          shape = RoundedCornerShape(14.dp),
          colors =
            ButtonDefaults.buttonColors(containerColor = BsnGreen, contentColor = BsnBlack),
        ) {
          if (state.isSaving) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              color = BsnBlack,
              strokeWidth = 2.dp,
            )
          } else {
            Icon(imageVector = Icons.Filled.Save, contentDescription = null)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "সংরক্ষণ করুন",
            fontFamily = HindSiliguriFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
          )
        }
      }
    } else {
      // Generated Digital ID Card + Share / Print / Download Actions
      val saved = state.savedProfile!!
      item {
        DigitalIdCardPreview(profile = saved)
      }

      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = BsnSurfaceElevated),
          border = BorderStroke(1.dp, BsnOutline),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Text(
              text = "আইডি কার্ড অ্যাকশন (অফলাইন সাপোর্টেড)",
              color = BsnGold,
              fontFamily = HindSiliguriFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              Button(
                onClick = viewModel::shareIdCard,
                enabled = !state.isProcessingCard,
                modifier =
                  Modifier.weight(1f)
                    .defaultMinSize(minHeight = 50.dp)
                    .testTag("share_id_card_btn"),
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(containerColor = BsnRed, contentColor = Color.White),
              ) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                  text = "শেয়ার",
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                )
              }

              Button(
                onClick = { viewModel.printIdCard(activity) },
                enabled = !state.isProcessingCard,
                modifier =
                  Modifier.weight(1f)
                    .defaultMinSize(minHeight = 50.dp)
                    .testTag("print_id_card_btn"),
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(containerColor = BsnGold, contentColor = BsnBlack),
              ) {
                Icon(imageVector = Icons.Filled.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                  text = "প্রিন্ট",
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                )
              }

              Button(
                onClick = viewModel::downloadIdCardImage,
                enabled = !state.isProcessingCard,
                modifier =
                  Modifier.weight(1f)
                    .defaultMinSize(minHeight = 50.dp)
                    .testTag("download_id_card_btn"),
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(containerColor = BsnGreen, contentColor = BsnBlack),
              ) {
                Icon(imageVector = Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                  text = "ডাউনলোড",
                  fontFamily = HindSiliguriFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                )
              }
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(6.dp)) }
  }
}

@Composable
private fun ProfileTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  tag: String,
  keyboardType: KeyboardType = KeyboardType.Text,
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(text = label, fontFamily = HindSiliguriFontFamily) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    modifier = Modifier.fillMaxWidth().testTag(tag),
    shape = RoundedCornerShape(12.dp),
    colors =
      OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BsnGold,
        unfocusedBorderColor = BsnOutline,
        focusedTextColor = BsnTextPrimary,
        unfocusedTextColor = BsnTextPrimary,
        focusedLabelColor = BsnGold,
        unfocusedLabelColor = BsnTextMuted,
        focusedContainerColor = BsnSurfaceElevated,
        unfocusedContainerColor = BsnSurfaceElevated,
      ),
  )
}

private tailrec fun Context.findActivity(): Activity? =
  when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
  }
