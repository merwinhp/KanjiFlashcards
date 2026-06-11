package com.kanjiflashcards.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanjiflashcards.ReviewMode
import com.kanjiflashcards.model.KanjiCard
import com.kanjiflashcards.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    deckTitle: String,
    deckCards: List<KanjiCard>,
    reviewMode: ReviewMode,
    isVocabDeck: Boolean,
    reviewedIndices: Set<Int>,
    onReviewedIndicesChange: (Set<Int>) -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalKanjiColors.current
    var allCards by remember(deckCards) { mutableStateOf(deckCards) }
    var cards by remember(deckCards, reviewMode) {
        mutableStateOf(
            when (reviewMode) {
                ReviewMode.NOT_REVIEWED -> deckCards.filterIndexed { idx, _ -> idx !in reviewedIndices }
                ReviewMode.REVIEWED -> deckCards.filterIndexed { idx, _ -> idx in reviewedIndices }
            }
        )
    }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var isRandom by remember { mutableStateOf(false) }
    var selectedKanji by remember { mutableStateOf(setOf<Int>()) }
    var showSelection by remember { mutableStateOf(false) }

    fun markCurrentReviewed() {
        if (cards.isEmpty() || currentIndex >= cards.size) return
        val current = cards[currentIndex]
        var newSet = reviewedIndices
        allCards.forEachIndexed { idx, card ->
            if (card.kanji == current.kanji && card.meaning == current.meaning && card.on == current.on) {
                newSet = newSet + idx
            }
        }
        onReviewedIndicesChange(newSet)
    }

    fun unmarkCurrentReviewed() {
        if (cards.isEmpty() || currentIndex >= cards.size) return
        val current = cards[currentIndex]
        var newSet = reviewedIndices
        allCards.forEachIndexed { idx, card ->
            if (card.kanji == current.kanji && card.meaning == current.meaning && card.on == current.on) {
                newSet = newSet - idx
            }
        }
        onReviewedIndicesChange(newSet)
    }

    fun buildReviewList(): List<KanjiCard> {
        val base = when (reviewMode) {
            ReviewMode.NOT_REVIEWED -> {
                if (selectedKanji.isEmpty())
                    allCards.filterIndexed { idx, _ -> idx !in reviewedIndices }
                else
                    allCards.filterIndexed { idx, _ -> idx in selectedKanji && idx !in reviewedIndices }
            }
            ReviewMode.REVIEWED -> {
                if (selectedKanji.isEmpty())
                    allCards.filterIndexed { idx, _ -> idx in reviewedIndices }
                else
                    allCards.filterIndexed { idx, _ -> idx in selectedKanji && idx in reviewedIndices }
            }
        }
        return if (isRandom) base.toMutableList().also { it.shuffle() } else base
    }

    fun nextCard() {
        if (cards.isEmpty()) return
        isFlipped = false
        currentIndex = if (currentIndex + 1 >= cards.size) 0 else currentIndex + 1
    }

    fun prevCard() {
        if (cards.isEmpty()) return
        isFlipped = false
        currentIndex = if (currentIndex == 0) cards.size - 1 else currentIndex - 1
    }

    fun reshuffle() {
        isFlipped = false
        cards = buildReviewList()
        currentIndex = 0
    }

    fun onSelectionDone() {
        showSelection = false
        cards = buildReviewList()
        currentIndex = 0
        isFlipped = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(deckTitle) },
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
        },
        bottomBar = {
            ControlBar(
                modeLabel = when (reviewMode) {
                    ReviewMode.NOT_REVIEWED -> "New"
                    ReviewMode.REVIEWED -> "Reviewed"
                },
                onPrev = { prevCard() },
                onNext = { nextCard() },
                onSelect = { showSelection = true }
            )
        }
    ) { padding ->
        if (showSelection) {
            KanjiSelectionScreen(
                allCards = allCards,
                isVocabDeck = isVocabDeck,
                selectedIndices = selectedKanji,
                reviewedIndices = reviewedIndices,
                onToggle = { index ->
                    selectedKanji = if (index in selectedKanji)
                        selectedKanji - index
                    else
                        selectedKanji + index
                },
                onSelectAll = { selectedKanji = allCards.indices.toSet() },
                onDeselectAll = { selectedKanji = emptySet() },
                onSelectReviewed = { selectedKanji = reviewedIndices },
                onSelectNotReviewed = { selectedKanji = allCards.indices.filter { it !in reviewedIndices }.toSet() },
                onToggleReviewed = { index ->
                    onReviewedIndicesChange(
                        if (index in reviewedIndices)
                            reviewedIndices - index
                        else
                            reviewedIndices + index
                    )
                },
                onDone = { onSelectionDone() },
                modifier = Modifier.padding(padding)
            )
        } else if (cards.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = KanjiAccent.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = when (reviewMode) {
                                ReviewMode.NOT_REVIEWED -> "\u2606"
                                ReviewMode.REVIEWED -> "\u2713"
                            },
                            fontSize = 36.sp,
                            color = KanjiAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = when (reviewMode) {
                        ReviewMode.NOT_REVIEWED -> "All clear!"
                        ReviewMode.REVIEWED -> "Nothing here yet"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (reviewMode) {
                        ReviewMode.NOT_REVIEWED -> "You've reviewed everything in this deck. Great job!"
                        ReviewMode.REVIEWED -> "Mark some cards as reviewed from the study deck."
                    },
                    fontSize = 15.sp,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(28.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { showSelection = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.midBlue)
                    ) {
                        Text("Manage Cards", fontWeight = FontWeight.Medium)
                    }
                    Button(
                        onClick = {
                            onReviewedIndicesChange(emptySet())
                            selectedKanji = emptySet()
                            cards = buildReviewList()
                            currentIndex = 0
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Start Over", fontWeight = FontWeight.Bold, color = KanjiWhite)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = colors.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / ${cards.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                FilterChip(
                    selected = isRandom,
                    onClick = {
                        isRandom = !isRandom
                        reshuffle()
                    },
                    label = { Text("\uD83C\uDFB2 Random", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.accent,
                        selectedLabelColor = KanjiWhite
                    ),
                    modifier = Modifier.height(32.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlashCard(
                    card = cards[currentIndex],
                    isVocabDeck = isVocabDeck,
                    isFlipped = isFlipped,
                    onFlip = { isFlipped = !isFlipped },
                    onSwipeLeft = { nextCard() },
                    onSwipeRight = { prevCard() },
                    notYetLabel = when (reviewMode) {
                        ReviewMode.NOT_REVIEWED -> "Not Yet"
                        ReviewMode.REVIEWED -> "OK"
                    },
                    gotItLabel = when (reviewMode) {
                        ReviewMode.NOT_REVIEWED -> "Got It"
                        ReviewMode.REVIEWED -> "Move Back"
                    },
                    onGotIt = {
                        when (reviewMode) {
                            ReviewMode.NOT_REVIEWED -> {
                                markCurrentReviewed()
                                val idx = currentIndex
                                val updated = cards.toMutableList().also { it.removeAt(idx) }
                                cards = updated
                                isFlipped = false
                                if (updated.isNotEmpty()) {
                                    currentIndex = if (idx >= updated.size) updated.size - 1 else idx
                                }
                            }
                            ReviewMode.REVIEWED -> {
                                unmarkCurrentReviewed()
                                val idx = currentIndex
                                val updated = cards.toMutableList().also { it.removeAt(idx) }
                                cards = updated
                                isFlipped = false
                                if (updated.isNotEmpty()) {
                                    currentIndex = if (idx >= updated.size) updated.size - 1 else idx
                                }
                            }
                        }
                    },
                    onNotYet = {
                        when (reviewMode) {
                            ReviewMode.NOT_REVIEWED -> {
                                nextCard()
                            }
                            ReviewMode.REVIEWED -> {
                                nextCard()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = colors.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "\uD83D\uDC46 Tap to flip  \u00A0\u00A0|\u00A0\u00A0  \uD83D\uDC48\uD83D\uDC49 Swipe to navigate",
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FlashCard(
    card: KanjiCard,
    isVocabDeck: Boolean,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    notYetLabel: String,
    gotItLabel: String,
    onGotIt: () -> Unit,
    onNotYet: () -> Unit
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val colors = LocalKanjiColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            dragOffset < -150f -> onSwipeLeft()
                            dragOffset > 150f -> onSwipeRight()
                        }
                        dragOffset = 0f
                    },
                    onHorizontalDrag = { _, amount ->
                        dragOffset += amount
                    }
                )
            }
            .graphicsLayer {
                translationX = dragOffset
            }
            .clickable { onFlip() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isFlipped)
                            listOf(KanjiGold.copy(alpha = 0.3f), colors.cardBg)
                        else
                            listOf(colors.cardBg, colors.surfaceVariant)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isFlipped,
                transitionSpec = {
                    (scaleIn(initialScale = 0.92f, animationSpec = tween(300)) + fadeIn(tween(250)))
                        .togetherWith(scaleOut(targetScale = 0.92f, animationSpec = tween(200)) + fadeOut(tween(150)))
                        .using(SizeTransform(clip = false))
                },
                label = "card_flip"
            ) { flipped ->
                if (flipped) {
                    BackContent(card, isVocabDeck, notYetLabel, gotItLabel, onGotIt, onNotYet)
                } else {
                    FrontContent(card, isVocabDeck)
                }
            }
        }
    }
}

@Composable
private fun FrontContent(card: KanjiCard, isVocabDeck: Boolean) {
    val isKana = card.on.isEmpty() && card.kun.isEmpty()
    val colors = LocalKanjiColors.current
    val fontSize = when {
        card.kanji.length <= 2 -> if (isVocabDeck && !isKana) 52.sp else 96.sp
        card.kanji.length <= 4 -> if (isVocabDeck && !isKana) 38.sp else 64.sp
        else -> if (isVocabDeck && !isKana) 26.sp else 36.sp
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(KanjiAccent.copy(alpha = 0.08f), RoundedCornerShape(60.dp))
                .align(Alignment.Center)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = card.kanji,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = fontSize * 1.15f
            )
            if (isVocabDeck && !isKana) {
                Surface(
                    color = colors.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = card.on,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(KanjiAccent.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
            )
        }

        Text(
            text = "?",
            fontSize = 14.sp,
            color = colors.textSecondary.copy(alpha = 0.4f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun BackContent(card: KanjiCard, isVocabDeck: Boolean, notYetLabel: String, gotItLabel: String, onGotIt: () -> Unit, onNotYet: () -> Unit) {
    val isKana = card.on.isEmpty() && card.kun.isEmpty()
    val colors = LocalKanjiColors.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = KanjiAccent.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = card.kanji,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = KanjiAccent,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isKana) {
                Text(
                    text = card.meaning,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.midBlue,
                    textAlign = TextAlign.Center
                )
            } else {
                Surface(
                    color = KanjiGold.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = card.meaning,
                        fontSize = if (isVocabDeck) 15.sp else 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = if (isVocabDeck) 19.sp else 20.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = colors.midBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Reading",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.textSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = card.on.ifEmpty { "-" },
                                    fontSize = if (isVocabDeck) 14.sp else 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.midBlue,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = if (isVocabDeck) 17.sp else 19.sp
                                )
                                if (card.onRomaji.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = card.onRomaji,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = colors.textSecondary,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = colors.midBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Romaji",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.textSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = card.kun.ifEmpty { "-" },
                                    fontSize = if (isVocabDeck) 14.sp else 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.midBlue,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = if (isVocabDeck) 17.sp else 19.sp
                                )
                                if (card.kunRomaji.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = card.kunRomaji,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = colors.textSecondary,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onNotYet,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textSecondary)
            ) {
                Text(notYetLabel, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Button(
                onClick = onGotIt,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = KanjiWhite
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(gotItLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ControlBar(
    modeLabel: String,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSelect: () -> Unit
) {
    val colors = LocalKanjiColors.current
    Surface(
        color = colors.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrev) {
                Text("\u25C0", fontSize = 18.sp, color = colors.midBlue)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = if (modeLabel == "New") colors.accent else colors.gold,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = modeLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (modeLabel == "New") KanjiWhite else colors.goldText
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                FilterChip(
                    selected = false,
                    onClick = onSelect,
                    label = { Text("\u2630 Select", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.midBlue,
                        selectedLabelColor = KanjiWhite
                    ),
                    modifier = Modifier.height(32.dp)
                )
            }

            IconButton(onClick = onNext) {
                Text("\u25B6", fontSize = 18.sp, color = colors.midBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanjiSelectionScreen(
    allCards: List<KanjiCard>,
    isVocabDeck: Boolean,
    selectedIndices: Set<Int>,
    reviewedIndices: Set<Int>,
    onToggle: (Int) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onSelectReviewed: () -> Unit,
    onSelectNotReviewed: () -> Unit,
    onToggleReviewed: (Int) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isVocab = isVocabDeck
    val colors = LocalKanjiColors.current

    val notReviewedItems = allCards.indices.filter { it !in reviewedIndices }
    val reviewedItems = allCards.mapIndexedNotNull { idx, _ -> if (idx in reviewedIndices) idx else null }

    var activeTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        "Not Reviewed (${notReviewedItems.size})",
        "Reviewed (${reviewedItems.size})"
    )

    val currentTabItems = if (activeTab == 0) notReviewedItems else reviewedItems
    val selectedInTab = selectedIndices.count { it in currentTabItems }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Select ${if (isVocab) "Vocab" else "Kanji"} (${selectedInTab}/${currentTabItems.size})")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarContent
                ),
                actions = {
                    TextButton(onClick = {
                        onDeselectAll()
                        currentTabItems.forEach { onToggle(it) }
                    }) {
                        Text("All", color = colors.topBarContent, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = onDeselectAll) {
                        Text("Clear", color = colors.topBarContent, fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        bottomBar = {
            Surface(color = colors.surface, shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onDone,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.midBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Review", color = KanjiWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = colors.surface,
                contentColor = colors.midBlue
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (isVocab) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(currentTabItems.size, key = { currentTabItems[it] }) { i ->
                        val index = currentTabItems[i]
                        VocabSelectionRow(
                            card = allCards[index],
                            isSelected = index in selectedIndices,
                            isReviewed = index in reviewedIndices,
                            onClick = { onToggle(index) },
                            onToggleReviewed = { onToggleReviewed(index) }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(currentTabItems.size, key = { currentTabItems[it] }) { i ->
                        GridCell(
                            card = allCards[currentTabItems[i]],
                            index = currentTabItems[i],
                            isSelected = currentTabItems[i] in selectedIndices,
                            isReviewed = currentTabItems[i] in reviewedIndices,
                            colors = colors,
                            onToggle = onToggle,
                            onToggleReviewed = onToggleReviewed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GridCell(
    card: KanjiCard,
    index: Int,
    isSelected: Boolean,
    isReviewed: Boolean,
    colors: KanjiAppColors,
    onToggle: (Int) -> Unit,
    onToggleReviewed: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(
                color = if (isSelected) colors.midBlue.copy(alpha = 0.3f) else colors.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onToggle(index) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = card.kanji,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                color = if (isSelected) colors.midBlue else colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
            Text(
                text = card.meaning,
                fontSize = 7.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSelected) colors.midBlue.copy(alpha = 0.8f) else colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 9.sp
            )
        }
        Text(
            text = if (isReviewed) "\u2713" else "\u25EF",
            fontSize = if (isReviewed) 12.sp else 10.sp,
            fontWeight = if (isReviewed) FontWeight.Bold else FontWeight.Normal,
            color = KanjiGold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onToggleReviewed(index) }
                .padding(4.dp)
        )
    }
}

@Composable
private fun VocabSelectionRow(
    card: KanjiCard,
    isSelected: Boolean,
    isReviewed: Boolean,
    onClick: () -> Unit,
    onToggleReviewed: () -> Unit
) {
    val colors = LocalKanjiColors.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = when {
            isSelected -> colors.midBlue.copy(alpha = 0.15f)
            isReviewed -> colors.surfaceVariant.copy(alpha = 0.5f)
            else -> colors.surfaceVariant
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.kanji,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = when {
                        isSelected -> colors.midBlue
                        isReviewed -> colors.textSecondary.copy(alpha = 0.6f)
                        else -> colors.textPrimary
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )
                if (card.on.isNotEmpty()) {
                    Text(
                        text = card.on,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = when {
                            isSelected -> colors.midBlue.copy(alpha = 0.7f)
                            isReviewed -> colors.textSecondary.copy(alpha = 0.4f)
                            else -> colors.textSecondary
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = card.meaning,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = when {
                    isSelected -> colors.midBlue.copy(alpha = 0.8f)
                    isReviewed -> colors.textSecondary.copy(alpha = 0.5f)
                    else -> colors.textSecondary
                },
                textAlign = TextAlign.End,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onToggleReviewed() },
                contentAlignment = Alignment.Center
            ) {
                if (isReviewed) {
                    Text("\u2713", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = KanjiGold)
                } else {
                    Text("\u25EF", fontSize = 14.sp, color = colors.textSecondary.copy(alpha = 0.4f))
                }
            }
        }
    }
}
