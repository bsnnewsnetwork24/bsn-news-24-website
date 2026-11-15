package com.example.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast

object ExternalLinkLauncher {

  enum class TargetPlatform {
    WHATSAPP,
    TELEGRAM,
    FACEBOOK,
    INSTAGRAM,
    YOUTUBE,
    TIKTOK,
    BROWSER,
  }

  fun openUrl(
    context: Context,
    rawUrl: String,
    platform: TargetPlatform = TargetPlatform.BROWSER,
  ) {
    val trimmed = rawUrl.trim()
    if (trimmed.isEmpty()) {
      Toast.makeText(context, "লিংকটি সঠিক নয়।", Toast.LENGTH_SHORT).show()
      return
    }
    val normalized =
      if (
        trimmed.startsWith("http://", ignoreCase = true) ||
          trimmed.startsWith("https://", ignoreCase = true)
      ) {
        trimmed
      } else {
        "https://$trimmed"
      }

    val uri =
      try {
        Uri.parse(normalized)
      } catch (_: Exception) {
        Toast.makeText(context, "লিংকটি ওপেন করা যায়নি।", Toast.LENGTH_SHORT).show()
        return
      }

    val candidatePackages =
      when (platform) {
        TargetPlatform.WHATSAPP -> listOf("com.whatsapp", "com.whatsapp.w4b")
        TargetPlatform.TELEGRAM ->
          listOf("org.telegram.messenger", "org.telegram.messenger.web", "tw.nekomimi.nekogram")
        TargetPlatform.FACEBOOK -> listOf("com.facebook.katana", "com.facebook.lite")
        TargetPlatform.INSTAGRAM -> listOf("com.instagram.android")
        TargetPlatform.YOUTUBE -> listOf("com.google.android.youtube")
        TargetPlatform.TIKTOK -> listOf("com.zhiliaoapp.musically", "com.ss.android.ugc.trill")
        TargetPlatform.BROWSER -> emptyList()
      }

    // Try launching installed target app directly first
    for (pkg in candidatePackages) {
      if (isPackageInstalled(context, pkg)) {
        try {
          val appIntent =
            Intent(Intent.ACTION_VIEW, uri).apply {
              setPackage(pkg)
              addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
          context.startActivity(appIntent)
          return
        } catch (_: Exception) {
          // Fall through to next candidate or external browser
        }
      }
    }

    // Fallback to system default handler / external browser
    try {
      val browserIntent =
        Intent(Intent.ACTION_VIEW, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
      context.startActivity(browserIntent)
    } catch (_: ActivityNotFoundException) {
      Toast.makeText(
        context,
        "এই লিংকটি খোলার জন্য উপযুক্ত অ্যাপ বা ব্রাউজার পাওয়া যায়নি।",
        Toast.LENGTH_LONG,
      )
        .show()
    } catch (_: Exception) {
      Toast.makeText(context, "লিংকটি ওপেন করতে সমস্যা হয়েছে।", Toast.LENGTH_SHORT).show()
    }
  }

  private fun isPackageInstalled(context: Context, packageName: String): Boolean {
    return try {
      context.packageManager.getPackageInfo(packageName, 0)
      true
    } catch (_: PackageManager.NameNotFoundException) {
      false
    } catch (_: Exception) {
      false
    }
  }
}
