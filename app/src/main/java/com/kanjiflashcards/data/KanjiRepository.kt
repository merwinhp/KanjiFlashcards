package com.kanjiflashcards.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kanjiflashcards.R
import com.kanjiflashcards.model.KanjiCard

object KanjiRepository {

    private val cache = mutableMapOf<Int, List<KanjiCard>>()

    fun loadCards(context: Context, resId: Int): List<KanjiCard> {
        return cache.getOrPut(resId) {
            when (resId) {
                R.raw.n5_kanji -> loadN5Json(context)
                R.raw.n2_kanji -> loadN2Kanji(context)
                else -> loadKanjiTsv(context, resId)
            }
        }
    }

    private fun loadN5Json(context: Context): List<KanjiCard> {
        val json = context.resources
            .openRawResource(R.raw.n5_kanji)
            .bufferedReader()
            .use { it.readText() }
        val type = object : TypeToken<List<KanjiCard>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun loadN2Kanji(context: Context): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(R.raw.n2_kanji)
            .bufferedReader()
            .use { reader -> reader.readLines() }

        return lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .mapNotNull { line ->
                val cols = line.split("\t")
                val kanji = cols.getOrNull(1)?.trim() ?: return@mapNotNull null
                val readings = cols.getOrNull(2)?.trim() ?: ""
                val meaning = cols.getOrNull(3)?.trim()?.replace("<br>", " ") ?: return@mapNotNull null

                val parts = readings.split("<br>", limit = 2)
                val on = parts.getOrNull(0)?.trim() ?: ""
                val kun = parts.getOrNull(1)?.trim() ?: ""

                if (kanji.isEmpty() || meaning.isEmpty()) return@mapNotNull null

                KanjiCard(
                    kanji = kanji,
                    meaning = meaning,
                    on = on,
                    kun = kun,
                    onRomaji = "",
                    kunRomaji = ""
                )
            }
    }

    private fun loadKanjiTsv(context: Context, resId: Int): List<KanjiCard> {
        val lines = context.resources
            .openRawResource(resId)
            .bufferedReader()
            .use { reader -> reader.readLines() }

        val dataLines = lines
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .dropWhile { line ->
                val cols = line.split("\t")
                cols.getOrNull(1) == "kanji"
            }

        return dataLines.mapNotNull { line ->
            val cols = line.split("\t")
            val kanji = cols.getOrNull(1)?.trim() ?: return@mapNotNull null
            val on = cols.getOrNull(2)?.trim() ?: ""
            val kun = cols.getOrNull(3)?.trim() ?: ""
            val meaning = cols.getOrNull(4)?.trim()?.replace("<br>", " ") ?: return@mapNotNull null

            if (kanji.isEmpty() || meaning.isEmpty()) return@mapNotNull null

            KanjiCard(
                kanji = kanji,
                meaning = meaning,
                on = on,
                kun = kun,
                onRomaji = "",
                kunRomaji = ""
            )
        }
    }
}
