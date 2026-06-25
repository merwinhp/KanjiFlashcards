package com.kanjiflashcards

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.kanjiflashcards.data.ExampleRepository
import com.kanjiflashcards.data.KanaData
import com.kanjiflashcards.data.KanjiRepository
import com.kanjiflashcards.data.VocabRepository
import com.kanjiflashcards.model.KanjiCard
import com.kanjiflashcards.R
import com.kanjiflashcards.ui.screens.*
import com.kanjiflashcards.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        CrashHandler.install(null)
        super.onCreate(savedInstanceState)
        val crashFile = File(filesDir, "last_crash.txt")
        CrashHandler.crashFile = crashFile

        val lastCrash = CrashHandler.readLastCrash(crashFile)
        CrashHandler.clear(crashFile)

        setContent {
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences("kanji_prefs", Context.MODE_PRIVATE) }
            var isDarkTheme by remember {
                mutableStateOf(prefs.getBoolean("dark_theme", false))
            }
            KanjiFlashcardsTheme(isDarkTheme = isDarkTheme) {
                if (lastCrash != null) {
                    CrashScreen(crashText = lastCrash)
                } else {
                    AppNavigation(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = {
                            isDarkTheme = !isDarkTheme
                            prefs.edit().putBoolean("dark_theme", isDarkTheme).apply()
                        }
                    )
                }
            }
        }
    }
}

enum class ReviewMode {
    NOT_REVIEWED,
    REVIEWED
}

private sealed class Screen {
    object MainMenu : Screen()
    object KanjiMenu : Screen()
    object VocabMenu : Screen()
    data class ReviewModeSelect(val title: String, val cards: List<KanjiCard>, val isVocabDeck: Boolean, val fromMenu: () -> Unit) : Screen()
    data class Review(val title: String, val cards: List<KanjiCard>, val mode: ReviewMode, val isVocabDeck: Boolean, val fromMenu: () -> Unit) : Screen()
    object Loading : Screen()
    data class Error(val message: String) : Screen()
}

@Composable
private fun AppNavigation(isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }
    var reviewedIndices by remember { mutableStateOf(setOf<Int>()) }
    val reviewedPrefs = remember { context.getSharedPreferences("kanji_reviewed", Context.MODE_PRIVATE) }
    var backPressTime by remember { mutableLongStateOf(0L) }

    fun loadReviewedIndices(title: String) {
        val saved = reviewedPrefs.getStringSet("reviewed_$title", null)
        reviewedIndices = saved?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
    }

    suspend fun attachExamples(deck: StudyDeck, cards: List<KanjiCard>): List<KanjiCard> {
        val exampleResId = when (deck) {
            StudyDeck.N5_KANJI -> R.raw.n5_kanji_examples
            StudyDeck.N4_KANJI -> R.raw.n4_kanji_examples
            StudyDeck.N3_KANJI -> R.raw.n3_kanji_examples
            StudyDeck.N2_KANJI -> R.raw.n2_kanji_examples
            StudyDeck.N5_VOCAB -> R.raw.n5_vocab_examples
            StudyDeck.N4_VOCAB -> R.raw.n4_vocab_examples
            StudyDeck.N3_VOCAB -> R.raw.n3_vocab_examples
            StudyDeck.N2_VOCAB -> R.raw.n2_vocab_examples
            else -> return cards
        }
        val examples = withContext(Dispatchers.IO) {
            ExampleRepository.loadExamples(context, exampleResId, cards.size)
        }
        if (examples.size == cards.size) {
            return cards.mapIndexed { i, card -> card.copy(examples = examples[i]) }
        }
        return cards
    }

    fun loadDeck(deck: StudyDeck, fromMenu: () -> Unit) {
        scope.launch {
            currentScreen = Screen.Loading
            try {
                var cards: List<KanjiCard>
                val title: String
                when (deck) {
                StudyDeck.N5_KANJI -> {
                    cards = withContext(Dispatchers.IO) {
                        KanjiRepository.loadCards(context, R.raw.n5_kanji)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N5 Kanji"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.N4_KANJI -> {
                    cards = withContext(Dispatchers.IO) {
                        KanjiRepository.loadCards(context, R.raw.n4_kanji)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N4 Kanji"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.N3_KANJI -> {
                    cards = withContext(Dispatchers.IO) {
                        KanjiRepository.loadCards(context, R.raw.n3_kanji)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N3 Kanji"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.N2_KANJI -> {
                    cards = withContext(Dispatchers.IO) {
                        KanjiRepository.loadCards(context, R.raw.n2_kanji)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N2 Kanji"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.HIRAGANA -> {
                    cards = KanaData.hiragana
                    title = "Hiragana"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.KATAKANA -> {
                    cards = KanaData.katakana
                    title = "Katakana"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = false, fromMenu)
                    return@launch
                }
                StudyDeck.N5_VOCAB -> {
                    cards = withContext(Dispatchers.IO) {
                        VocabRepository.loadCards(context, R.raw.n5_vocab)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N5 Vocab"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = true, fromMenu)
                    return@launch
                }
                StudyDeck.N4_VOCAB -> {
                    cards = withContext(Dispatchers.IO) {
                        VocabRepository.loadCards(context, R.raw.n4_vocab)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N4 Vocab"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = true, fromMenu)
                    return@launch
                }
                StudyDeck.N3_VOCAB -> {
                    cards = withContext(Dispatchers.IO) {
                        VocabRepository.loadCards(context, R.raw.n3_vocab)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N3 Vocab"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = true, fromMenu)
                    return@launch
                }
                StudyDeck.N2_VOCAB -> {
                    cards = withContext(Dispatchers.IO) {
                        VocabRepository.loadCards(context, R.raw.n2_vocab)
                    }
                    cards = attachExamples(deck, cards)
                    title = "N2 Vocab"
                    loadReviewedIndices(title)
                    currentScreen = Screen.ReviewModeSelect(title, cards, isVocabDeck = true, fromMenu)
                    return@launch
                }
            }
            } catch (e: Exception) {
                currentScreen = Screen.Error(e.message ?: "Failed to load deck")
            }
        }
    }

    when (val screen = currentScreen) {
        Screen.MainMenu -> {
            BackHandler {
                if (System.currentTimeMillis() - backPressTime < 2000) {
                    (context as? ComponentActivity)?.finishAffinity()
                } else {
                    backPressTime = System.currentTimeMillis()
                    Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                }
            }
            MainMenuScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onKanjiClick = { currentScreen = Screen.KanjiMenu },
                onVocabClick = { currentScreen = Screen.VocabMenu },
                onHiraganaClick = { loadDeck(StudyDeck.HIRAGANA) { currentScreen = Screen.MainMenu } },
                onKatakanaClick = { loadDeck(StudyDeck.KATAKANA) { currentScreen = Screen.MainMenu } }
            )
        }
        Screen.KanjiMenu -> {
            BackHandler { currentScreen = Screen.MainMenu }
            KanjiMenuScreen(
                onBack = { currentScreen = Screen.MainMenu },
                onDeckSelected = { deck -> loadDeck(deck) { currentScreen = Screen.KanjiMenu } }
            )
        }
        Screen.VocabMenu -> {
            BackHandler { currentScreen = Screen.MainMenu }
            VocabMenuScreen(
                onBack = { currentScreen = Screen.MainMenu },
                onDeckSelected = { deck -> loadDeck(deck) { currentScreen = Screen.VocabMenu } }
            )
        }
        is Screen.ReviewModeSelect -> {
            BackHandler { screen.fromMenu() }
            ReviewModeSelectScreen(
                title = screen.title,
                cards = screen.cards,
                reviewedCount = reviewedIndices.size,
                totalCount = screen.cards.size,
                onSelectMode = { mode ->
                    currentScreen = Screen.Review(screen.title, screen.cards, mode, screen.isVocabDeck, screen.fromMenu)
                },
                onBack = screen.fromMenu
            )
        }
        is Screen.Review -> {
            BackHandler {
                currentScreen = Screen.ReviewModeSelect(screen.title, screen.cards, screen.isVocabDeck, screen.fromMenu)
            }
            FlashcardScreen(
                deckTitle = screen.title,
                deckCards = screen.cards,
                reviewMode = screen.mode,
                isVocabDeck = screen.isVocabDeck,
                reviewedIndices = reviewedIndices,
                onReviewedIndicesChange = {
                    reviewedIndices = it
                    reviewedPrefs.edit().putStringSet("reviewed_${screen.title}", it.map { idx -> idx.toString() }.toSet()).apply()
                },
                onBack = { currentScreen = Screen.ReviewModeSelect(screen.title, screen.cards, screen.isVocabDeck, screen.fromMenu) }
            )
        }
        Screen.Loading -> {
            BackHandler { currentScreen = Screen.MainMenu }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = KanjiAccent)
            }
        }
        is Screen.Error -> {
            BackHandler { currentScreen = Screen.MainMenu }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = screen.message,
                        color = KanjiAccent,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    TextButton(onClick = { currentScreen = Screen.MainMenu }) {
                        Text("Back to menu", color = KanjiMidBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewModeSelectScreen(
    title: String,
    cards: List<KanjiCard>,
    reviewedCount: Int,
    totalCount: Int,
    onSelectMode: (ReviewMode) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose review mode",
                style = MaterialTheme.typography.titleMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                onClick = { onSelectMode(ReviewMode.NOT_REVIEWED) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBg)
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
                            .background(KanjiAccent, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\u65B0", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = KanjiWhite)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Not Reviewed", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                        Text("Study new kanji you haven't seen yet", fontSize = 13.sp, color = colors.textSecondary)
                    }
                    Surface(
                        color = KanjiAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${totalCount - reviewedCount} cards",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = KanjiAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                onClick = { onSelectMode(ReviewMode.REVIEWED) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBg)
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
                            .background(KanjiGold, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\u5FA9", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.goldText)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Already Reviewed", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                        Text("Review kanji you've already studied", fontSize = 13.sp, color = colors.textSecondary)
                    }
                    Surface(
                        color = KanjiGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "$reviewedCount cards",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.goldText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CrashScreen(crashText: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = KanjiLightGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "App crashed!",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = KanjiAccent
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = KanjiWhite,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = crashText,
                    modifier = Modifier.padding(16.dp),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = KanjiTextPrimary,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
