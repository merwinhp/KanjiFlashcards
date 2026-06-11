package com.kanjiflashcards

import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrashHandler : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("KanjiFlashcards", "FATAL CRASH in thread ${t.name}", e)

        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        val crashMsg = "[${SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())}] ${sw}"

        try {
            crashFile?.writeText(crashMsg)
        } catch (_: Exception) {}

        defaultHandler?.uncaughtException(t, e)
    }

    companion object {
        var crashFile: File? = null

        fun install(crashFile: File?) {
            this.crashFile = crashFile
            if (Thread.getDefaultUncaughtExceptionHandler() !is CrashHandler) {
                Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
            }
        }

        fun readLastCrash(crashFile: File): String? {
            return try {
                if (crashFile.exists()) crashFile.readText().trim() else null
            } catch (_: Exception) {
                null
            }
        }

        fun clear(crashFile: File) {
            try { crashFile.delete() } catch (_: Exception) {}
        }
    }
}
