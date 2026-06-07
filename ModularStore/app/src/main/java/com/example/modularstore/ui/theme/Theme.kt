package com.example.modularstore.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class AppThemeMode { BLUE, GREEN, PURPLE, SUNSET }

private val defaultTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize   = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize   = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp
    )
)

private val sunsetTypography = defaultTypography.copy(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize   = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp
    )
)

@Composable
fun ModularStoreTheme(
    themeMode: AppThemeMode = AppThemeMode.BLUE,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.BLUE   -> lightColorScheme(
            primary          = BluePrimary,
            background       = Surface,
            surface          = CardBg,
            onPrimary        = CardBg,
            primaryContainer = Brand100,
            onPrimaryContainer = Brand900
        )
        AppThemeMode.GREEN  -> lightColorScheme(primary = GreenPrimary, background = Surface)
        AppThemeMode.PURPLE -> lightColorScheme(primary = PurplePrimary, background = Surface)
        AppThemeMode.SUNSET -> lightColorScheme(
            primary    = SunsetPrimary,
            secondary  = SunsetSecondary,
            background = SunsetBackground,
            surface    = SunsetSurface
        )
    }
    val typography = if (themeMode == AppThemeMode.SUNSET) sunsetTypography else defaultTypography

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = typography,
        content     = content
    )
}