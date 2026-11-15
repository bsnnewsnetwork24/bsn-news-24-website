package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val BsnDarkColorScheme =
  darkColorScheme(
    primary = BsnRed,
    onPrimary = BsnTextPrimary,
    primaryContainer = BsnRedDark,
    onPrimaryContainer = BsnTextPrimary,
    secondary = BsnGreen,
    onSecondary = BsnBlack,
    secondaryContainer = BsnGreenDark,
    onSecondaryContainer = BsnTextPrimary,
    tertiary = BsnGold,
    onTertiary = BsnBlack,
    background = BsnBlack,
    onBackground = BsnTextPrimary,
    surface = BsnSurface,
    onSurface = BsnTextPrimary,
    surfaceVariant = BsnSurfaceElevated,
    onSurfaceVariant = BsnTextSecondary,
    outline = BsnOutline,
    error = BsnRedGlow,
    onError = BsnTextPrimary,
  )

private val BsnShapes =
  Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
  )

@Composable
fun BsnNews24Theme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = BsnDarkColorScheme,
    typography = Typography,
    shapes = BsnShapes,
    content = content,
  )
}
