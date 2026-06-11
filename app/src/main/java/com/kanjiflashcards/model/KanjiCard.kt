package com.kanjiflashcards.model

data class KanjiCard(
    val kanji: String,
    val meaning: String,
    val on: String = "",
    val kun: String = "",
    val onRomaji: String = "",
    val kunRomaji: String = ""
)
