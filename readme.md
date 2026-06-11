# KanjiFlashcards 🇯🇵

 Android flashcard app for learning Japanese — kanji, vocabulary, hiragana, and katakana.

---

## So what does it actually do?

It's a flashcard app. You open it, pick what you want to study, and flip through cards. Simple.

- **Kanji decks** — JLPT N5 through N2. Each card shows the kanji, its on/kun readings, what it means
- **Vocab decks** — Same with kanji, but for words. Word on the front, reading and meaning on the back.
- **Kana decks** — The full hiragana and katakana tables, for when you're just starting out.
- **Track your progress** — The app remembers which cards you've reviewed. You can flip through everything or just the ones you haven't seen yet. Your call.
---

## What's it made of?

| What | With |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Build | Gradle (Kotlin DSL) |
| JSON parsing | Gson |
| Minimum Android | 8.0 (API 26) — runs on pretty much anything |

---

## How it's organized

```
KanjiFlashcards/
├── app/
│   ├── build.gradle.kts            ← dependencies and build config
│   └── src/main/
│       ├── AndroidManifest.xml      ← app entry point and splash setup
│       ├── java/com/kanjiflashcards/
│       │   ├── MainActivity.kt      ← where it all starts
│       │   ├── CrashHandler.kt      ← catches crashes, writes logs
│       │   ├── data/
│       │   │   ├── KanaData.kt      ← hiragana & katakana, hardcoded
│       │   │   ├── KanjiRepository.kt  ← loads kanji decks from files
│       │   │   └── VocabRepository.kt  ← loads vocab decks from files
│       │   ├── model/
│       │   │   └── KanjiCard.kt     ← one card to rule them all
│       │   └── ui/
│       │       ├── screens/
│       │       │   ├── FlashcardScreen.kt  ← the actual card-flipping
│       │       │   └── MainMenuScreen.kt   ← menus, deck pickers, mode select
│       │       └── theme/
│       │           ├── Color.kt     ← color definitions
│       │           └── Theme.kt     ← light/dark Material 3 themes
│       └── res/
│           └── raw/                 ← all the deck data files
│               ├── n5_kanji.json    ← N5 kanji (JSON)
│               ├── n4_kanji.txt     ← N4 kanji
│               ├── n3_kanji.txt     ← N3 kanji
│               ├── n2_kanji.txt     ← N2 kanji
│               ├── n5_vocab.txt     ← N5 vocabulary
│               ├── n4_vocab.txt     ← N4 vocabulary
│               ├── n3_vocab.txt     ← N3 vocabulary
│               └── n2_vocab.txt     ← N2 vocabulary
├── build.gradle.kts                 ← root project config
├── settings.gradle.kts              ← module setup
├── gradle.properties
├── gradlew / gradlew.bat            ← Gradle wrapper (no global Gradle needed)
└── local.properties                 ← your local SDK path (gitignored)
```

---

## How it works under the hood

1. **Startup** — The crash handler gets installed first thing. If the last session ended badly, you get a crash report screen instead of a silent black hole. If everything's fine, you land on the main menu.

2. **Picking a deck** — Tap Kanji or Vocab, then pick a JLPT level (or Kana for the syllabaries). The app loads the deck on a background thread so the UI doesn't freeze up.

3. **Review mode** — Before you start, you pick: review everything, or just the cards you haven't seen yet? Your progress per deck is saved in `SharedPreferences`.

4. **The actual flashcards** — One card at a time. Tap or swipe to flip it and reveal the answer. Navigate with next/previous buttons.

5. **Theme switching** — There's a toggle on the main screen. It flips between light and dark Material
---

## How to build it

### What you need
- **Android Studio** (any relatively recent version)
- **JDK 17**
- Android SDK 34

### Steps

**Command line (if you're that kind of person):**
```bash
git clone https://github.com/merwinhp/KanjiFlashcards.git KanjiFlashcards
cd KanjiFlashcards

# Make sure local.properties has your SDK path:
# sdk.dir=/home/you/Android/Sdk

./gradlew assembleDebug
```

The APK will be sitting at `app/build/outputs/apk/debug/app-debug.apk`.

**Android Studio (the easier way):**
1. Open the project folder.
2. Let Gradle do its sync thing.
3. Hit the green Run button with a device or emulator connected.

### Install directly
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## The data files

Deck data lives in `app/src/main/res/raw/`:

- **n5_kanji.json** — proper JSON with fields for the kanji character, on/kun readings, meanings.
- **n4–n2 kanji** — plain text files, one entry per line, parsed by `KanjiRepository`.
- **vocab files** — also plain text, structured as `word|reading|meaning` per line, handled by `VocabRepository`.
- **Kana** — not from files at all, just hardcoded in `KanaData.kt`.

---

Happy studying! がんばって！📚
