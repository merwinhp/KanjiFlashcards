package com.kanjiflashcards.data

import android.content.Context
import com.kanjiflashcards.R
import com.kanjiflashcards.model.KanjiCard

object VocabRepository {

    private val cache = mutableMapOf<Int, List<KanjiCard>>()

    fun loadCards(context: Context, resId: Int): List<KanjiCard> {
        return cache.getOrPut(resId) {
            when (resId) {
                R.raw.n5_vocab -> loadN5Vocab(context)
                R.raw.n4_vocab -> loadN4Vocab(context)
                R.raw.n3_vocab -> loadN3Vocab(context)
                R.raw.n2_vocab -> loadN2Vocab(context)
                else -> emptyList()
            }
        }
    }

    private fun loadN5Vocab(context: Context): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(R.raw.n5_vocab)
            .bufferedReader()
            .use { it.readLines() }

        return lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .mapNotNull { line ->
                val cols = line.split("\t")
                val word = cols.getOrNull(4)?.trim() ?: return@mapNotNull null
                val reading = cols.getOrNull(5)?.trim() ?: ""
                val romaji = cols.getOrNull(8)?.trim() ?: ""
                val meaning = cols.getOrNull(9)?.trim() ?: return@mapNotNull null

                if (word.isEmpty() || meaning.isEmpty()) return@mapNotNull null

                KanjiCard(
                    kanji = word,
                    meaning = meaning,
                    on = reading,
                    kun = romaji,
                    onRomaji = "",
                    kunRomaji = ""
                )
            }
    }

    private fun loadN4Vocab(context: Context): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(R.raw.n4_vocab)
            .bufferedReader()
            .use { it.readLines() }

        return lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .mapNotNull { line ->
                val cols = line.split("\t")
                val word = cols.getOrNull(3)?.trim() ?: return@mapNotNull null
                val reading = cols.getOrNull(1)?.trim() ?: ""
                val meaning = cols.getOrNull(2)?.trim() ?: return@mapNotNull null

                if (word.isEmpty() || meaning.isEmpty()) return@mapNotNull null

                KanjiCard(
                    kanji = word,
                    meaning = meaning,
                    on = reading,
                    kun = "",
                    onRomaji = "",
                    kunRomaji = ""
                )
            }
    }

    private fun loadN3Vocab(context: Context): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(R.raw.n3_vocab)
            .bufferedReader()
            .use { it.readLines() }

        return lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .mapNotNull { line ->
                val cols = line.split("\t")
                val word = cols.getOrNull(1)?.trim() ?: return@mapNotNull null
                val reading = cols.getOrNull(3)?.trim() ?: ""
                val meaning = cols.getOrNull(2)?.trim() ?: return@mapNotNull null

                if (word.isEmpty() || meaning.isEmpty()) return@mapNotNull null

                KanjiCard(
                    kanji = word,
                    meaning = meaning,
                    on = reading,
                    kun = "",
                    onRomaji = "",
                    kunRomaji = ""
                )
            }
    }

    private fun loadN2Vocab(context: Context): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(R.raw.n2_vocab)
            .bufferedReader()
            .use { it.readLines() }

        return lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .mapNotNull { line ->
                val cols = line.split("\t")
                val word = cols.getOrNull(1)?.trim() ?: return@mapNotNull null
                val reading = cols.getOrNull(2)?.trim() ?: ""
                val meaning = cols.getOrNull(3)?.trim() ?: return@mapNotNull null

                if (word.isEmpty() || meaning.isEmpty()) return@mapNotNull null

                KanjiCard(
                    kanji = word,
                    meaning = meaning,
                    on = reading,
                    kun = "",
                    onRomaji = "",
                    kunRomaji = ""
                )
            }
    }
}
