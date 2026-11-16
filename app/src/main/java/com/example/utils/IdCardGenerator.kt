package com.example.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.config.BsnConfig
import com.example.data.local.JournalistProfile
import java.io.File
import java.io.FileOutputStream

object IdCardGenerator {

  fun renderIdCardBitmap(context: Context, profile: JournalistProfile): Bitmap {
    val baseTypeface =
      runCatching { ResourcesCompat.getFont(context, R.font.hind_siliguri) }.getOrNull()
        ?: Typeface.DEFAULT
    val boldTypeface = Typeface.create(baseTypeface, Typeface.BOLD)
    val width = 1080
    val height = 1600
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // 1. Dark premium gradient background
    val bgPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader =
          LinearGradient(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            intArrayOf(
              Color.parseColor("#080B10"),
              Color.parseColor("#111622"),
              Color.parseColor("#0A0D14"),
            ),
            null,
            Shader.TileMode.CLAMP,
          )
      }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Subtle red and green corner ambient glow
    val redGlow =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(42, 230, 0, 0)
      }
    canvas.drawCircle(120f, 160f, 340f, redGlow)
    val greenGlow =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(38, 0, 200, 83)
      }
    canvas.drawCircle(width - 100f, height - 180f, 360f, greenGlow)

    // Outer Gold Metallic Frame
    val cardRect = RectF(28f, 28f, width - 28f, height - 28f)
    val goldStroke =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        shader =
          LinearGradient(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            intArrayOf(
              Color.parseColor("#FFD700"),
              Color.parseColor("#FFF0A5"),
              Color.parseColor("#C5941F"),
              Color.parseColor("#FFD700"),
            ),
            null,
            Shader.TileMode.CLAMP,
          )
      }
    canvas.drawRoundRect(cardRect, 42f, 42f, goldStroke)

    // Top Red Header Ribbon
    val headerPath =
      Path().apply {
        addRoundRect(
          RectF(36f, 36f, width - 36f, 265f),
          floatArrayOf(36f, 36f, 36f, 36f, 0f, 0f, 0f, 0f),
          Path.Direction.CW,
        )
      }
    val headerPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader =
          LinearGradient(
            0f,
            36f,
            width.toFloat(),
            265f,
            Color.parseColor("#B50000"),
            Color.parseColor("#E60000"),
            Shader.TileMode.CLAMP,
          )
      }
    canvas.drawPath(headerPath, headerPaint)

    // Official BSN Logo on Top Left
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bsn_official_logo)
    if (logoBitmap != null) {
      val logoRect = RectF(64f, 64f, 232f, 232f)
      val clipPath = Path().apply { addOval(logoRect, Path.Direction.CW) }
      canvas.save()
      canvas.clipPath(clipPath)
      canvas.drawBitmap(logoBitmap, null, logoRect, Paint(Paint.FILTER_BITMAP_FLAG))
      canvas.restore()

      val ringPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
          style = Paint.Style.STROKE
          strokeWidth = 5f
          color = Color.parseColor("#FFD700")
        }
      canvas.drawOval(logoRect, ringPaint)
    }

    // Bangladesh National Flag on Top Right
    val flagRect = RectF(width - 216f, 78f, width - 66f, 168f)
    val flagBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#006A4E") }
    canvas.drawRoundRect(flagRect, 10f, 10f, flagBgPaint)
    val flagCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#F42A41") }
    canvas.drawCircle(
      flagRect.left + flagRect.width() * 0.45f,
      flagRect.centerY(),
      flagRect.height() * 0.30f,
      flagCirclePaint,
    )
    val flagBorder =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.parseColor("#FFD700")
      }
    canvas.drawRoundRect(flagRect, 10f, 10f, flagBorder)

    // Header Title & PRESS Badge
    val titlePaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 54f
        typeface = boldTypeface
      }
    canvas.drawText("BSN NEWS 24", 260f, 128f, titlePaint)

    val subTitlePaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFE082")
        textSize = 27f
        typeface = boldTypeface
      }
    canvas.drawText("BANGLADESH SANGBADIK NETWORK", 260f, 172f, subTitlePaint)

    // PRESS Pill inside Header
    val pressRect = RectF(260f, 190f, 440f, 240f)
    val pressBg =
      Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FFD700") }
    canvas.drawRoundRect(pressRect, 14f, 14f, pressBg)
    val pressText =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0A0D14")
        textSize = 30f
        textAlign = Paint.Align.CENTER
        typeface = boldTypeface
      }
    canvas.drawText("PRESS", pressRect.centerX(), pressRect.centerY() + 10f, pressText)

    val taglinePaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 26f
        typeface = boldTypeface
      }
    canvas.drawText(BsnConfig.TAGLINE, 464f, 224f, taglinePaint)

    // User Photograph Circle / Rounded Frame
    val photoSize = 310f
    val photoLeft = (width - photoSize) / 2f
    val photoTop = 310f
    val photoRect = RectF(photoLeft, photoTop, photoLeft + photoSize, photoTop + photoSize)

    val photoBgPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#182030") }
    canvas.drawRoundRect(photoRect, 32f, 32f, photoBgPaint)

    val userPhoto =
      if (profile.photoLocalPath.isNotBlank() && File(profile.photoLocalPath).exists()) {
        BitmapFactory.decodeFile(profile.photoLocalPath)
      } else {
        null
      }

    if (userPhoto != null) {
      val photoClip = Path().apply { addRoundRect(photoRect, 32f, 32f, Path.Direction.CW) }
      canvas.save()
      canvas.clipPath(photoClip)
      // Center-crop user photo
      val srcRatio = userPhoto.width.toFloat() / userPhoto.height.toFloat()
      val dstRatio = 1f
      val srcRect =
        if (srcRatio > dstRatio) {
          val cropW = (userPhoto.height * dstRatio).toInt()
          val left = (userPhoto.width - cropW) / 2
          Rect(left, 0, left + cropW, userPhoto.height)
        } else {
          val cropH = (userPhoto.width / dstRatio).toInt()
          val top = (userPhoto.height - cropH) / 2
          Rect(0, top, userPhoto.width, top + cropH)
        }
      canvas.drawBitmap(userPhoto, srcRect, photoRect, Paint(Paint.FILTER_BITMAP_FLAG))
      canvas.restore()
    } else {
      val noPhotoPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
          color = Color.parseColor("#94A3B8")
          textSize = 30f
          textAlign = Paint.Align.CENTER
          typeface = boldTypeface
        }
      canvas.drawText("ছবি সংযুক্ত নেই", photoRect.centerX(), photoRect.centerY(), noPhotoPaint)
    }

    val photoBorder =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 7f
        color = Color.parseColor("#00C853")
      }
    canvas.drawRoundRect(photoRect, 32f, 32f, photoBorder)

    // Journalist Name & Designation Banner
    val namePaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 48f
        textAlign = Paint.Align.CENTER
        typeface = boldTypeface
      }
    canvas.drawText(
      profile.name.ifBlank { "সাংবাদিকের নাম" },
      width / 2f,
      690f,
      namePaint,
    )

    val desigPillRect = RectF(170f, 718f, width - 170f, 782f)
    val desigBg =
      Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#00C853") }
    canvas.drawRoundRect(desigPillRect, 32f, 32f, desigBg)
    val desigPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#07090D")
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = boldTypeface
      }
    canvas.drawText(
      profile.designation.ifBlank { "সাংবাদিক / প্রতিনিধি" },
      desigPillRect.centerX(),
      desigPillRect.centerY() + 11f,
      desigPaint,
    )

    // Details Box (NO ID CARD SERIAL NUMBER)
    val infoBoxRect = RectF(76f, 822f, width - 76f, 1280f)
    val infoBoxPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#161D2B") }
    canvas.drawRoundRect(infoBoxRect, 28f, 28f, infoBoxPaint)
    val infoBorderPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.parseColor("#2B374E")
      }
    canvas.drawRoundRect(infoBoxRect, 28f, 28f, infoBorderPaint)

    val labelPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD700")
        textSize = 31f
        typeface = boldTypeface
      }
    val valuePaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 33f
        typeface = boldTypeface
      }

    val rows =
      listOf(
        "পিতার নাম:" to profile.fatherName.ifBlank { "-" },
        "পদবি:" to profile.designation.ifBlank { "-" },
        "মোবাইল নম্বর:" to profile.mobileNumber.ifBlank { "-" },
        "জেলা:" to profile.district.ifBlank { "-" },
        "রক্তের গ্রুপ:" to profile.bloodGroup.ifBlank { "-" },
      )
    var rowY = 900f
    val dividerPaint =
      Paint().apply {
        color = Color.parseColor("#263044")
        strokeWidth = 2f
      }
    rows.forEachIndexed { index, (label, value) ->
      canvas.drawText(label, 120f, rowY, labelPaint)
      canvas.drawText(value, 390f, rowY, valuePaint)
      if (index < rows.lastIndex) {
        canvas.drawLine(110f, rowY + 30f, width - 110f, rowY + 30f, dividerPaint)
      }
      rowY += 84f
    }

    // Exact Proof Text Banner
    val proofRect = RectF(76f, 1316f, width - 76f, 1418f)
    val proofBg =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader =
          LinearGradient(
            proofRect.left,
            proofRect.top,
            proofRect.right,
            proofRect.bottom,
            Color.parseColor("#B50000"),
            Color.parseColor("#006A4E"),
            Shader.TileMode.CLAMP,
          )
      }
    canvas.drawRoundRect(proofRect, 22f, 22f, proofBg)
    val proofStroke =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.parseColor("#FFD700")
      }
    canvas.drawRoundRect(proofRect, 22f, 22f, proofStroke)

    val proofTextPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 34f
        textAlign = Paint.Align.CENTER
        typeface = boldTypeface
      }
    canvas.drawText(
      BsnConfig.TAB10_ID_CARD_PROOF_TEXT,
      proofRect.centerX(),
      proofRect.centerY() + 12f,
      proofTextPaint,
    )

    // Footer Brand Line
    val footerPaint =
      Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CBD5E1")
        textSize = 26f
        textAlign = Paint.Align.CENTER
        typeface = boldTypeface
      }
    canvas.drawText(
      "BSN News 24 • Bangladesh Sangbadik Network",
      width / 2f,
      1498f,
      footerPaint,
    )

    return bitmap
  }

  fun shareIdCard(context: Context, bitmap: Bitmap): Result<Unit> {
    return try {
      val sharedDir = File(context.cacheDir, "shared").apply { mkdirs() }
      val file = File(sharedDir, "bsn_news_24_id_card.png")
      FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
      val uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
      val shareIntent =
        Intent(Intent.ACTION_SEND).apply {
          type = "image/png"
          putExtra(Intent.EXTRA_STREAM, uri)
          putExtra(
            Intent.EXTRA_TEXT,
            "${BsnConfig.OFFICIAL_NAME} - ${BsnConfig.TAB10_ID_CARD_PROOF_TEXT}",
          )
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
      val chooser =
        Intent.createChooser(shareIntent, "ডিজিটাল আইডি কার্ড শেয়ার করুন").apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
      context.startActivity(chooser)
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  fun saveIdCardToGallery(context: Context, bitmap: Bitmap): Result<String> {
    val fileName = "BSN_ID_Card_${System.currentTimeMillis()}.png"
    return try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values =
          ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(
              MediaStore.Images.Media.RELATIVE_PATH,
              "${Environment.DIRECTORY_PICTURES}/BSN News 24",
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
          }
        val resolver = context.contentResolver
        val imageUri =
          resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: return Result.failure(IllegalStateException("গ্যালারিতে ফাইল তৈরি করা যায়নি।"))
        resolver.openOutputStream(imageUri)?.use { out ->
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } ?: return Result.failure(IllegalStateException("ইমেজ সেভ করা যায়নি।"))
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(imageUri, values, null, null)
        Result.success("গ্যালারির Pictures/BSN News 24 ফোল্ডারে সেভ হয়েছে।")
      } else {
        val appDir =
          File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir,
            "BSN News 24",
          )
            .apply { mkdirs() }
        val file = File(appDir, fileName)
        FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        Result.success("ইমেজ সংরক্ষণ করা হয়েছে: ${file.name}")
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  fun printIdCard(activity: Activity, bitmap: Bitmap): Result<Unit> {
    return try {
      val printManager =
        activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager
          ?: return Result.failure(IllegalStateException("প্রিন্ট সার্ভিস পাওয়া যায়নি।"))
      val jobName = "BSN_News_24_Digital_ID_Card"
      val adapter =
        object : PrintDocumentAdapter() {
          private var pageAttributes: PrintAttributes? = null

          override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback,
            extras: Bundle?,
          ) {
            if (cancellationSignal?.isCanceled == true) {
              callback.onLayoutCancelled()
              return
            }
            pageAttributes = newAttributes
            val info =
              PrintDocumentInfo.Builder("$jobName.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1)
                .build()
            callback.onLayoutFinished(info, true)
          }

          override fun onWrite(
            pages: Array<out PageRange>,
            destination: ParcelFileDescriptor,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback,
          ) {
            val attrs =
              pageAttributes
                ?: PrintAttributes.Builder()
                  .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                  .build()
            val pdfDocument = PrintedPdfDocument(activity, attrs)
            try {
              val page = pdfDocument.startPage(0)
              if (cancellationSignal?.isCanceled == true) {
                callback.onWriteCancelled()
                pdfDocument.close()
                return
              }
              val pageCanvas = page.canvas
              val contentWidth = pageCanvas.width.toFloat()
              val contentHeight = pageCanvas.height.toFloat()
              val margin = 36f
              val availW = (contentWidth - margin * 2).coerceAtLeast(100f)
              val availH = (contentHeight - margin * 2).coerceAtLeast(100f)
              val scale = minOf(availW / bitmap.width, availH / bitmap.height)
              val drawW = bitmap.width * scale
              val drawH = bitmap.height * scale
              val left = (contentWidth - drawW) / 2f
              val top = (contentHeight - drawH) / 2f
              pageCanvas.drawBitmap(
                bitmap,
                null,
                RectF(left, top, left + drawW, top + drawH),
                Paint(Paint.FILTER_BITMAP_FLAG),
              )
              pdfDocument.finishPage(page)
              FileOutputStream(destination.fileDescriptor).use { out -> pdfDocument.writeTo(out) }
              callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            } catch (e: Exception) {
              callback.onWriteFailed(e.message)
            } finally {
              pdfDocument.close()
            }
          }
        }
      printManager.print(jobName, adapter, null)
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
