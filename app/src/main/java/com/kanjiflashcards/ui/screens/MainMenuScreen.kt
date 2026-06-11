package com.kanjiflashcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanjiflashcards.ui.theme.*

enum class StudyDeck {
    N5_KANJI,
    N4_KANJI,
    N3_KANJI,
    N2_KANJI,
    HIRAGANA,
    KATAKANA,
    N5_VOCAB,
    N4_VOCAB,
    N3_VOCAB,
    N2_VOCAB
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onKanjiClick: () -> Unit,
    onVocabClick: () -> Unit,
    onHiraganaClick: () -> Unit,
    onKatakanaClick: () -> Unit
) {
    val colors = LocalKanjiColors.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "\u6F22",
                            fontSize = 26.sp,
                            color = colors.accent
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Kanji Flashcards",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.topBarContent,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "JLPT N5 \u2013 N2",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = colors.topBarContent.copy(alpha = 0.7f),
                                letterSpacing = 2.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarContent
                )
            )
        },
        bottomBar = {
            Surface(
                color = colors.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Theme",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, colors.textSecondary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .background(colors.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (!isDarkTheme) colors.accent else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { if (isDarkTheme) onToggleTheme() }
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "\u2600 Light",
                                fontSize = 13.sp,
                                fontWeight = if (!isDarkTheme) FontWeight.Bold else FontWeight.Normal,
                                color = if (!isDarkTheme) KanjiWhite else colors.textPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isDarkTheme) colors.accent else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { if (!isDarkTheme) onToggleTheme() }
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "\uD83C\uDF19 Dark",
                                fontSize = 13.sp,
                                fontWeight = if (isDarkTheme) FontWeight.Bold else FontWeight.Normal,
                                color = if (isDarkTheme) KanjiWhite else colors.textPrimary
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose a category",
                style = MaterialTheme.typography.titleMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            MenuCardButton(
                title = "Kanji",
                subtitle = "N5, N4, N3",
                description = "Kanji characters with on'yomi and kun'yomi readings",
                color = KanjiAccent,
                onClick = onKanjiClick,
                iconText = "\u6F22"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "Vocab",
                subtitle = "N5, N4, N3",
                description = "Vocabulary words with readings and meanings",
                color = KanjiGold,
                onClick = onVocabClick,
                iconText = "\u8A9E"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "Hiragana",
                subtitle = "46 characters",
                description = "Basic hiragana syllabary",
                color = KanjiMidBlue,
                onClick = onHiraganaClick,
                iconText = "\u3042"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "Katakana",
                subtitle = "46 characters",
                description = "Basic katakana syllabary",
                color = KanjiDarkBlue,
                onClick = onKatakanaClick,
                iconText = "\u30A2"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanjiMenuScreen(
    onBack: () -> Unit,
    onDeckSelected: (StudyDeck) -> Unit
) {
    val colors = LocalKanjiColors.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\u6F22", fontSize = 22.sp, color = colors.accent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kanji", fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("< Back", color = colors.topBarContent, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarContent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Select a level",
                style = MaterialTheme.typography.titleMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            MenuCardButton(
                title = "N5 Kanji",
                subtitle = "104 kanji",
                description = "Basic kanji characters for JLPT N5 level",
                color = KanjiAccent,
                onClick = { onDeckSelected(StudyDeck.N5_KANJI) },
                iconText = "\u4E94"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N4 Kanji",
                subtitle = "208 kanji",
                description = "Kanji characters for JLPT N4 level",
                color = KanjiMidBlue,
                onClick = { onDeckSelected(StudyDeck.N4_KANJI) },
                iconText = "\u56DB"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N3 Kanji",
                subtitle = "451 kanji",
                description = "Kanji characters for JLPT N3 level",
                color = KanjiDarkBlue,
                onClick = { onDeckSelected(StudyDeck.N3_KANJI) },
                iconText = "\u4E09"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N2 Kanji",
                subtitle = "368 kanji",
                description = "Kanji characters for JLPT N2 level",
                color = KanjiGold,
                onClick = { onDeckSelected(StudyDeck.N2_KANJI) },
                iconText = "\u4E8C"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabMenuScreen(
    onBack: () -> Unit,
    onDeckSelected: (StudyDeck) -> Unit
) {
    val colors = LocalKanjiColors.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\u8A9E", fontSize = 22.sp, color = colors.accent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vocab", fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("< Back", color = colors.topBarContent, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarContent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Select a level",
                style = MaterialTheme.typography.titleMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            MenuCardButton(
                title = "N5 Vocab",
                subtitle = "657 words",
                description = "Basic vocabulary words for JLPT N5 level",
                color = KanjiAccent,
                onClick = { onDeckSelected(StudyDeck.N5_VOCAB) },
                iconText = "\u4E94"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N4 Vocab",
                subtitle = "704 words",
                description = "Vocabulary words for JLPT N4 level",
                color = KanjiMidBlue,
                onClick = { onDeckSelected(StudyDeck.N4_VOCAB) },
                iconText = "\u56DB"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N3 Vocab",
                subtitle = "1891 words",
                description = "Vocabulary words for JLPT N3 level",
                color = KanjiDarkBlue,
                onClick = { onDeckSelected(StudyDeck.N3_VOCAB) },
                iconText = "\u4E09"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuCardButton(
                title = "N2 Vocab",
                subtitle = "2273 words",
                description = "Vocabulary words for JLPT N2 level",
                color = KanjiGold,
                onClick = { onDeckSelected(StudyDeck.N2_VOCAB) },
                iconText = "\u4E8C"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingSoonScreen(
    title: String,
    onBack: () -> Unit
) {
    val colors = LocalKanjiColors.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("< Back", color = colors.topBarContent, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarContent
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Coming Soon",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MenuCardButton(
    title: String,
    subtitle: String,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconText: String? = null
) {
    val colors = LocalKanjiColors.current
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBg),
        enabled = enabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText ?: title.first().toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (color == KanjiGold) KanjiDarkBlue else KanjiWhite
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = colors.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = subtitle,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
