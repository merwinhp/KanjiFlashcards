package com.kanjiflashcards.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = KanjiAccent,
    onPrimary = KanjiWhite,
    primaryContainer = KanjiGold,
    secondary = KanjiMidBlue,
    onSecondary = KanjiWhite,
    background = KanjiLightGray,
    surface = KanjiWhite,
    onBackground = KanjiTextPrimary,
    onSurface = KanjiTextPrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = KanjiAccent,
    onPrimary = KanjiWhite,
    primaryContainer = KanjiAccent.copy(alpha = 0.3f),
    secondary = KanjiGold,
    onSecondary = KanjiDarkBlue,
    background = KanjiDarkBg,
    surface = KanjiDarkSurface,
    onBackground = KanjiDarkTextPrimary,
    onSurface = KanjiDarkTextPrimary,
)

@Composable
fun KanjiFlashcardsTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkAppColors else LightAppColors
    CompositionLocalProvider(LocalKanjiColors provides colors) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
            content = content
        )
    }
}
