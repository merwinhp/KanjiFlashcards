package com.kanjiflashcards.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kanjiflashcards.model.ExampleSentence

object ExampleRepository {

    private val cache = mutableMapOf<Int, List<List<ExampleSentence>>>()

    fun loadExamples(context: Context, resId: Int, cardCount: Int): List<List<ExampleSentence>> {
        return cache.getOrPut(resId) {
            val json = context.resources
                .openRawResource(resId)
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<List<ExampleSentence>>>() {}.type
            val all: List<List<ExampleSentence>> = Gson().fromJson(json, type)

            if (all.size != cardCount) {
                android.util.Log.w(
                    "ExampleRepository",
                    "Example count ${all.size} != card count $cardCount for resource $resId"
                )
            }

            all
        }
    }

    fun clearCache() {
        cache.clear()
    }
}
