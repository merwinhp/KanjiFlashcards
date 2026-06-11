package com.kanjiflashcards.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val KanjiDarkBlue = Color(0xFF1A1A2E)
val KanjiMidBlue = Color(0xFF16213E)
val KanjiAccent = Color(0xFFE94560)
val KanjiGold = Color(0xFFFFD369)
val KanjiLightGray = Color(0xFFF5F5F5)
val KanjiWhite = Color(0xFFFFFFFF)
val KanjiTextPrimary = Color(0xFF1A1A1A)
val KanjiTextSecondary = Color(0xFF555555)

val KanjiDarkBg = Color(0xFF0D1117)
val KanjiDarkSurface = Color(0xFF161B22)
val KanjiDarkCard = Color(0xFF21262D)
val KanjiDarkTextPrimary = Color(0xFFD4D9E0)
val KanjiDarkTextSecondary = Color(0xFF9CA3AF)

@Immutable
data class KanjiAppColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val topBarBg: Color,
    val topBarContent: Color,
    val cardBg: Color,
    val accent: Color,
    val accentText: Color,
    val gold: Color,
    val goldText: Color,
    val midBlue: Color,
    val darkBlue: Color,
    val divider: Color,
)

val LightAppColors = KanjiAppColors(
    background = KanjiLightGray,
    surface = KanjiWhite,
    surfaceVariant = KanjiLightGray.copy(alpha = 0.5f),
    textPrimary = KanjiTextPrimary,
    textSecondary = KanjiTextSecondary,
    topBarBg = KanjiMidBlue,
    topBarContent = KanjiWhite,
    cardBg = KanjiWhite,
    accent = KanjiAccent,
    accentText = KanjiWhite,
    gold = KanjiGold,
    goldText = KanjiTextPrimary,
    midBlue = KanjiMidBlue,
    darkBlue = KanjiDarkBlue,
    divider = KanjiAccent,
)

val DarkAppColors = KanjiAppColors(
    background = KanjiDarkBg,
    surface = KanjiDarkSurface,
    surfaceVariant = KanjiDarkCard,
    textPrimary = KanjiDarkTextPrimary,
    textSecondary = KanjiDarkTextSecondary,
    topBarBg = Color(0xFF0D1117),
    topBarContent = KanjiDarkTextPrimary,
    cardBg = KanjiDarkCard,
    accent = KanjiAccent,
    accentText = KanjiWhite,
    gold = KanjiGold,
    goldText = KanjiDarkBlue,
    midBlue = Color(0xFF7B9ED0),
    darkBlue = KanjiDarkBlue,
    divider = KanjiGold,
)

val LocalKanjiColors = staticCompositionLocalOf { LightAppColors }
