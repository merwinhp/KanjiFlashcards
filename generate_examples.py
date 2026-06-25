#!/usr/bin/env python3
"""Generate example sentence JSON files for all 8 JLPT decks.

For kanji: 2-3 sentences per entry (on + kun + different meanings).
For vocab: 1 sentence per entry showing the word in context.
"""

import json
import os
import re

RAW_DIR = "app/src/main/res/raw"

# ── Utility ───────────────────────────────────────────────────────────

def write_examples(filename, examples_list):
    path = os.path.join(RAW_DIR, filename)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(examples_list, f, ensure_ascii=False, indent=2)
    print(f"  {filename} — {len(examples_list)} entries")


def clean_word(w):
    """Remove furigana annotations like 相[あい] → 相"""
    w = re.sub(r'\[.*?\]', '', w)
    return w.strip()


# ── N5 Kanji (hand-crafted for accuracy) ──────────────────────────────

N5_EXAMPLES = {
    "一": [
        {"japanese": "一番大切なのは健康です。", "romaji": "ichiban taisetsu na no wa kenkou desu.", "meaning": "The most important thing is health."},
        {"japanese": "これが一つだけあります。", "romaji": "kore ga hitotsu dake arimasu.", "meaning": "There is only one of this."},
    ],
    "二": [
        {"japanese": "二番目の駅で降りてください。", "romaji": "nibanme no eki de orite kudasai.", "meaning": "Please get off at the second station."},
        {"japanese": "りんごを二つ買いました。", "romaji": "ringo o futatsu kaimashita.", "meaning": "I bought two apples."},
    ],
    "三": [
        {"japanese": "三時に会いましょう。", "romaji": "sanji ni aimashou.", "meaning": "Let's meet at three o'clock."},
        {"japanese": "三日間雨が続きました。", "romaji": "mikka kan ame ga tsuzukimashita.", "meaning": "The rain continued for three days."},
    ],
    "四": [
        {"japanese": "四月から新学期が始まります。", "romaji": "shigatsu kara shingakki ga hajimarimasu.", "meaning": "The new semester starts in April."},
        {"japanese": "四人で旅行に行きました。", "romaji": "yonin de ryokou ni ikimashita.", "meaning": "I went on a trip with four people."},
    ],
    "五": [
        {"japanese": "五時に起きます。", "romaji": "goji ni okimasu.", "meaning": "I wake up at five o'clock."},
        {"japanese": "五百円払いました。", "romaji": "gohyaku en haraimashita.", "meaning": "I paid five hundred yen."},
    ],
    "六": [
        {"japanese": "六人家族です。", "romaji": "rokunin kazoku desu.", "meaning": "We are a family of six."},
        {"japanese": "六つ目の交差点を曲がってください。", "romaji": "mutsume no kousaten o magatte kudasai.", "meaning": "Please turn at the sixth intersection."},
    ],
    "七": [
        {"japanese": "七時に夕食を食べます。", "romaji": "shichiji ni yuushoku o tabemasu.", "meaning": "I eat dinner at seven o'clock."},
        {"japanese": "七つの海を旅したい。", "romaji": "nanatsu no umi o tabi shitai.", "meaning": "I want to travel the seven seas."},
    ],
    "八": [
        {"japanese": "八時に出発します。", "romaji": "hachiji ni shuppatsu shimasu.", "meaning": "We depart at eight o'clock."},
        {"japanese": "八十八歳のおばあさんです。", "romaji": "hachijuhassai no obaasan desu.", "meaning": "She is an eighty-eight-year-old grandmother."},
    ],
    "九": [
        {"japanese": "九時から授業です。", "romaji": "kuji kara jugyou desu.", "meaning": "Class starts at nine."},
        {"japanese": "九つ目の答えが正解でした。", "romaji": "kokonotsume no kotae ga seikai deshita.", "meaning": "The ninth answer was correct."},
    ],
    "十": [
        {"japanese": "十分かかります。", "romaji": "juppun kakarimasu.", "meaning": "It takes ten minutes."},
        {"japanese": "十人でパーティーをしました。", "romaji": "juunin de paatii o shimashita.", "meaning": "We had a party with ten people."},
    ],
    "百": [
        {"japanese": "百円ショップに行きました。", "romaji": "hyaku en shoppu ni ikimashita.", "meaning": "I went to the hundred-yen shop."},
        {"japanese": "出席者は百人以上でした。", "romaji": "shusseki sha wa hyakunin ijou deshita.", "meaning": "There were over a hundred attendees."},
    ],
    "千": [
        {"japanese": "千円貸してください。", "romaji": "sen en kashite kudasai.", "meaning": "Please lend me a thousand yen."},
        {"japanese": "千羽鶴を折りました。", "romaji": "senbazuru o orimashita.", "meaning": "I folded a thousand paper cranes."},
    ],
    "万": [
        {"japanese": "万年筆を使っています。", "romaji": "mannenhitsu o tsukatte imasu.", "meaning": "I use a fountain pen."},
        {"japanese": "万が一の時に備えます。", "romaji": "mangaichi no toki ni sonaemasu.", "meaning": "I prepare for emergencies."},
    ],
    "円": [
        {"japanese": "百円玉が三枚あります。", "romaji": "hyakuendama ga sanmai arimasu.", "meaning": "I have three hundred-yen coins."},
        {"japanese": "円いテーブルを買いました。", "romaji": "marui teeburu o kaimashita.", "meaning": "I bought a round table."},
    ],
    "年": [
        {"japanese": "来年、日本に行きます。", "romaji": "rainen, nihon ni ikimasu.", "meaning": "I will go to Japan next year."},
        {"japanese": "彼は年をとりました。", "romaji": "kare wa toshi o torimashita.", "meaning": "He has gotten older."},
        {"japanese": "毎年、夏に花火を見ます。", "romaji": "mainen, natsu ni hanabi o mimasu.", "meaning": "I see fireworks every summer."},
    ],
    "月": [
        {"japanese": "今月は忙しいです。", "romaji": "kongetsu wa isogashii desu.", "meaning": "This month is busy."},
        {"japanese": "月がきれいですね。", "romaji": "tsuki ga kirei desu ne.", "meaning": "The moon is beautiful, isn't it."},
        {"japanese": "毎月、図書館に行きます。", "romaji": "maitsuki, toshokan ni ikimasu.", "meaning": "I go to the library every month."},
    ],
    "日": [
        {"japanese": "毎日、日本語を勉強します。", "romaji": "mainichi, nihongo o benkyou shimasu.", "meaning": "I study Japanese every day."},
        {"japanese": "日曜日は休みです。", "romaji": "nichiyoubi wa yasumi desu.", "meaning": "Sunday is a day off."},
        {"japanese": "あの日は楽しかった。", "romaji": "ano hi wa tanoshikatta.", "meaning": "That day was fun."},
    ],
    "時": [
        {"japanese": "今、何時ですか。", "romaji": "ima, nanji desu ka.", "meaning": "What time is it now?"},
        {"japanese": "子供の時、よく泳ぎました。", "romaji": "kodomo no toki, yoku oyogimashita.", "meaning": "I often swam when I was a child."},
    ],
    "分": [
        {"japanese": "十分待ってください。", "romaji": "juupen matte kudasai.", "meaning": "Please wait ten minutes."},
        {"japanese": "この問題が分かりました。", "romaji": "kono mondai ga wakarimashita.", "meaning": "I understood this problem."},
        {"japanese": "三分の一を食べました。", "romaji": "sanbun no ichi o tabemashita.", "meaning": "I ate one third of it."},
    ],
    "半": [
        {"japanese": "七時半に起きます。", "romaji": "shichiji han ni okimasu.", "meaning": "I wake up at half past seven."},
        {"japanese": "半分だけ食べました。", "romaji": "hanbun dake tabemashita.", "meaning": "I only ate half of it."},
    ],
    "今": [
        {"japanese": "今、勉強しています。", "romaji": "ima, benkyou shite imasu.", "meaning": "I am studying now."},
        {"japanese": "今週はテストがあります。", "romaji": "konshuu wa tesuto ga arimasu.", "meaning": "There is a test this week."},
    ],
    "何": [
        {"japanese": "これは何ですか。", "romaji": "kore wa nan desu ka.", "meaning": "What is this?"},
        {"japanese": "何人いますか。", "romaji": "nannin imasu ka.", "meaning": "How many people are there?"},
    ],
    "人": [
        {"japanese": "あの人は先生です。", "romaji": "ano hito wa sensei desu.", "meaning": "That person is a teacher."},
        {"japanese": "日本人の友達がいます。", "romaji": "nihonjin no tomodachi ga imasu.", "meaning": "I have a Japanese friend."},
        {"japanese": "三人で映画を見ました。", "romaji": "sannin de eiga o mimashita.", "meaning": "Three of us watched a movie."},
    ],
    "男": [
        {"japanese": "男の子が生まれました。", "romaji": "otoko no ko ga umaremashita.", "meaning": "A baby boy was born."},
        {"japanese": "男性の医者に診てもらいました。", "romaji": "dansei no isha ni mite moraimashita.", "meaning": "I was seen by a male doctor."},
    ],
    "女": [
        {"japanese": "女の人が三人います。", "romaji": "onna no hito ga sannin imasu.", "meaning": "There are three women."},
        {"japanese": "女性の作家が好きです。", "romaji": "josei no sakka ga suki desu.", "meaning": "I like female authors."},
    ],
    "子": [
        {"japanese": "子供が公園で遊んでいます。", "romaji": "kodomo ga kouen de asonde imasu.", "meaning": "Children are playing in the park."},
        {"japanese": "男の子と女の子がいます。", "romaji": "otoko no ko to onna no ko ga imasu.", "meaning": "There is a boy and a girl."},
    ],
    "父": [
        {"japanese": "父は会社員です。", "romaji": "chichi wa kaishain desu.", "meaning": "My father is a company employee."},
        {"japanese": "お父さんは元気ですか。", "romaji": "otousan wa genki desu ka.", "meaning": "How is your father?"},
    ],
    "母": [
        {"japanese": "母は料理が上手です。", "romaji": "haha wa ryouri ga jouzu desu.", "meaning": "My mother is good at cooking."},
        {"japanese": "お母さんに電話しました。", "romaji": "okaasan ni denwa shimashita.", "meaning": "I called my mother."},
    ],
    "友": [
        {"japanese": "友達と映画を見ました。", "romaji": "tomodachi to eiga o mimashita.", "meaning": "I watched a movie with a friend."},
        {"japanese": "友情は大切です。", "romaji": "yuujou wa taisetsu desu.", "meaning": "Friendship is important."},
    ],
    "先": [
        {"japanese": "先生は親切です。", "romaji": "sensei wa shinsetsu desu.", "meaning": "The teacher is kind."},
        {"japanese": "先週、京都に行きました。", "romaji": "senshuu, kyouto ni ikimashita.", "meaning": "I went to Kyoto last week."},
        {"japanese": "駅の先を右に曲がってください。", "romaji": "eki no saki o migi ni magatte kudasai.", "meaning": "Turn right after the station."},
    ],
    "生": [
        {"japanese": "学生です。", "romaji": "gakusei desu.", "meaning": "I am a student."},
        {"japanese": "毎日、生きています。", "romaji": "mainichi, ikite imasu.", "meaning": "I live every day."},
        {"japanese": "日本で生まれました。", "romaji": "nihon de umaremashita.", "meaning": "I was born in Japan."},
    ],
    "学": [
        {"japanese": "日本語を学んでいます。", "romaji": "nihongo o manande imasu.", "meaning": "I am learning Japanese."},
        {"japanese": "学校に行きます。", "romaji": "gakkou ni ikimasu.", "meaning": "I go to school."},
    ],
    "校": [
        {"japanese": "学校の近くに住んでいます。", "romaji": "gakkou no chikaku ni sunde imasu.", "meaning": "I live near the school."},
        {"japanese": "校長先生の話を聞きました。", "romaji": "kouchou sensei no hanashi o kikimashita.", "meaning": "I listened to the principal's speech."},
    ],
    "大": [
        {"japanese": "大きい犬ですね。", "romaji": "ookii inu desu ne.", "meaning": "That's a big dog."},
        {"japanese": "大学に行きたいです。", "romaji": "daigaku ni ikitai desu.", "meaning": "I want to go to university."},
    ],
    "小": [
        {"japanese": "小さい猫が好きです。", "romaji": "chiisai neko ga suki desu.", "meaning": "I like small cats."},
        {"japanese": "小学校で習いました。", "romaji": "shougakkou de naraimashita.", "meaning": "I learned it in elementary school."},
    ],
    "中": [
        {"japanese": "箱の中に何がありますか。", "romaji": "hako no naka ni nani ga arimasu ka.", "meaning": "What is inside the box?"},
        {"japanese": "中国に行ったことがあります。", "romaji": "chuugoku ni itta koto ga arimasu.", "meaning": "I have been to China."},
        {"japanese": "ただ今、勉強中です。", "romaji": "tadaima, benkyou chuu desu.", "meaning": "I'm studying right now."},
    ],
    "上": [
        {"japanese": "机の上に本があります。", "romaji": "tsukue no ue ni hon ga arimasu.", "meaning": "There is a book on the desk."},
        {"japanese": "上手に歌えますね。", "romaji": "jouzu ni utaemasu ne.", "meaning": "You sing well."},
        {"japanese": "温度が上がりました。", "romaji": "ondo ga agarimashita.", "meaning": "The temperature rose."},
    ],
    "下": [
        {"japanese": "猫が机の下にいます。", "romaji": "neko ga tsukue no shita ni imasu.", "meaning": "The cat is under the desk."},
        {"japanese": "天気が悪くなりました。", "romaji": "tenki ga waruku narimashita.", "meaning": "The weather got bad."},
        {"japanese": "荷物を下ろしてください。", "romaji": "nimotsu o oroshite kudasai.", "meaning": "Please unload the luggage."},
    ],
    "左": [
        {"japanese": "左に曲がってください。", "romaji": "hidari ni magatte kudasai.", "meaning": "Please turn left."},
        {"japanese": "左手にバッグを持っています。", "romaji": "hidarite ni baggu o motte imasu.", "meaning": "I'm holding the bag in my left hand."},
    ],
    "右": [
        {"japanese": "右側に郵便局があります。", "romaji": "migigawa ni yuubinkyoku ga arimasu.", "meaning": "There is a post office on the right side."},
        {"japanese": "右手で書いてください。", "romaji": "migite de kaite kudasai.", "meaning": "Please write with your right hand."},
    ],
    "前": [
        {"japanese": "駅の前にあります。", "romaji": "eki no mae ni arimasu.", "meaning": "It's in front of the station."},
        {"japanese": "十年前に日本に来ました。", "romaji": "juunen mae ni nihon ni kimashita.", "meaning": "I came to Japan ten years ago."},
    ],
    "後": [
        {"japanese": "後ろを見てください。", "romaji": "ushiro o mite kudasai.", "meaning": "Please look behind you."},
        {"japanese": "その後、映画を見ました。", "romaji": "sono go, eiga o mimashita.", "meaning": "After that, I watched a movie."},
    ],
    "東": [
        {"japanese": "東京に行きたいです。", "romaji": "toukyou ni ikitai desu.", "meaning": "I want to go to Tokyo."},
        {"japanese": "東口で待っています。", "romaji": "higashiguchi de matte imasu.", "meaning": "I'm waiting at the east exit."},
    ],
    "西": [
        {"japanese": "西口を出てください。", "romaji": "nishiguchi o dete kudasai.", "meaning": "Please exit through the west exit."},
        {"japanese": "西ヨーロッパへ旅行したい。", "romaji": "nishi yooroppa e ryokou shitai.", "meaning": "I want to travel to Western Europe."},
    ],
    "南": [
        {"japanese": "南口で会いましょう。", "romaji": "minamiguchi de aimashou.", "meaning": "Let's meet at the south exit."},
        {"japanese": "南アメリカへ旅行したいです。", "romaji": "minami amerika e ryokou shitai desu.", "meaning": "I want to travel to South America."},
    ],
    "北": [
        {"japanese": "北口はこちらです。", "romaji": "kitaguchi wa kochira desu.", "meaning": "The north exit is this way."},
        {"japanese": "北海道は日本の北にあります。", "romaji": "hokkaidou wa nihon no kita ni arimasu.", "meaning": "Hokkaido is in the north of Japan."},
    ],
    "外": [
        {"japanese": "外は寒いです。", "romaji": "soto wa samui desu.", "meaning": "It's cold outside."},
        {"japanese": "外国人と話しました。", "romaji": "gaikokujin to hanashimashita.", "meaning": "I talked with a foreigner."},
    ],
    "国": [
        {"japanese": "外国に行ったことがあります。", "romaji": "gaikoku ni itta koto ga arimasu.", "meaning": "I have been to a foreign country."},
        {"japanese": "この国は美しいです。", "romaji": "kono kuni wa utsukushii desu.", "meaning": "This country is beautiful."},
    ],
    "山": [
        {"japanese": "山に登りました。", "romaji": "yama ni noborimashita.", "meaning": "I climbed a mountain."},
        {"japanese": "富士山は日本で一番高い山です。", "romaji": "fujisan wa nihon de ichiban takai yama desu.", "meaning": "Mt. Fuji is the tallest mountain in Japan."},
    ],
    "川": [
        {"japanese": "川で魚を釣りました。", "romaji": "kawa de sakana o tsurimashita.", "meaning": "I fished in the river."},
        {"japanese": "この川はとてもきれいです。", "romaji": "kono kawa wa totemo kirei desu.", "meaning": "This river is very clean."},
    ],
    "空": [
        {"japanese": "空が青いです。", "romaji": "sora ga aoi desu.", "meaning": "The sky is blue."},
        {"japanese": "空港まで送ります。", "romaji": "kuukou made okurimasu.", "meaning": "I'll take you to the airport."},
        {"japanese": "箱が空です。", "romaji": "hako ga kara desu.", "meaning": "The box is empty."},
    ],
    "海": [
        {"japanese": "海で泳ぐのが好きです。", "romaji": "umi de oyogu no ga suki desu.", "meaning": "I like swimming in the sea."},
        {"japanese": "海外旅行に行きました。", "romaji": "kaigai ryokou ni ikimashita.", "meaning": "I went on an overseas trip."},
    ],
    "雨": [
        {"japanese": "雨が降っています。", "romaji": "ame ga futte imasu.", "meaning": "It's raining."},
        {"japanese": "今日は雨模様です。", "romaji": "kyou wa ame moyou desu.", "meaning": "It looks like rain today."},
    ],
    "木": [
        {"japanese": "木の下で休みました。", "romaji": "ki no shita de yasumimashita.", "meaning": "I rested under the tree."},
        {"japanese": "木曜日に会いましょう。", "romaji": "mokuyoubi ni aimashou.", "meaning": "Let's meet on Thursday."},
    ],
    "花": [
        {"japanese": "花が咲きました。", "romaji": "hana ga sakimashita.", "meaning": "The flowers bloomed."},
        {"japanese": "この花は何ですか。", "romaji": "kono hana wa nan desu ka.", "meaning": "What flower is this?"},
    ],
    "水": [
        {"japanese": "水を飲みたいです。", "romaji": "mizu o nomitai desu.", "meaning": "I want to drink water."},
        {"japanese": "水道代を払いました。", "romaji": "suidou dai o haraimashita.", "meaning": "I paid the water bill."},
    ],
    "火": [
        {"japanese": "火事に気をつけてください。", "romaji": "kaji ni ki o tsukete kudasai.", "meaning": "Please be careful of fire."},
        {"japanese": "火曜日は図書館が休みです。", "romaji": "kayoubi wa toshokan ga yasumi desu.", "meaning": "The library is closed on Tuesday."},
    ],
    "金": [
        {"japanese": "お金がありません。", "romaji": "okane ga arimasen.", "meaning": "I don't have money."},
        {"japanese": "金曜日にパーティーがあります。", "romaji": "kinyoubi ni paatii ga arimasu.", "meaning": "There is a party on Friday."},
    ],
    "土": [
        {"japanese": "土曜日に買い物に行きます。", "romaji": "doyoubi ni kaimono ni ikimasu.", "meaning": "I will go shopping on Saturday."},
        {"japanese": "この土はとても良いです。", "romaji": "kono tsuchi wa totemo yoi desu.", "meaning": "This soil is very good."},
    ],
    "白": [
        {"japanese": "白い猫が好きです。", "romaji": "shiroi neko ga suki desu.", "meaning": "I like white cats."},
        {"japanese": "白紙で出したいです。", "romaji": "hakushi de dashitai desu.", "meaning": "I want to submit a blank sheet."},
    ],
    "赤": [
        {"japanese": "赤いリンゴを食べました。", "romaji": "akai ringo o tabemashita.", "meaning": "I ate a red apple."},
        {"japanese": "信号が赤です。", "romaji": "shingou ga aka desu.", "meaning": "The traffic light is red."},
    ],
    "青": [
        {"japanese": "海は青いです。", "romaji": "umi wa aoi desu.", "meaning": "The sea is blue."},
        {"japanese": "青信号になったら渡ってください。", "romaji": "aoshingou ni nattara watatte kudasai.", "meaning": "Please cross when the light turns green."},
    ],
    "食": [
        {"japanese": "朝ごはんを食べました。", "romaji": "asagohan o tabemashita.", "meaning": "I ate breakfast."},
        {"japanese": "食堂で昼食をとりました。", "romaji": "shokudou de chuushoku o torimashita.", "meaning": "I had lunch in the cafeteria."},
        {"japanese": "食事の前に手を洗いましょう。", "romaji": "shokuji no mae ni te o araimashou.", "meaning": "Let's wash our hands before the meal."},
    ],
    "飲": [
        {"japanese": "水を飲みました。", "romaji": "mizu o nomimashita.", "meaning": "I drank water."},
        {"japanese": "飲み物は何にしますか。", "romaji": "nomimono wa nani ni shimasu ka.", "meaning": "What would you like to drink?"},
    ],
    "見": [
        {"japanese": "映画を見ます。", "romaji": "eiga o mimasu.", "meaning": "I watch a movie."},
        {"japanese": "見せてください。", "romaji": "misete kudasai.", "meaning": "Please show me."},
        {"japanese": "意見があります。", "romaji": "iken ga arimasu.", "meaning": "I have an opinion."},
    ],
    "聞": [
        {"japanese": "音楽を聞きます。", "romaji": "ongaku o kikimasu.", "meaning": "I listen to music."},
        {"japanese": "ニュースを聞きました。", "romaji": "nyuusu o kikimashita.", "meaning": "I heard the news."},
    ],
    "言": [
        {"japanese": "先生がそう言いました。", "romaji": "sensei ga sou iimashita.", "meaning": "The teacher said so."},
        {"japanese": "言葉の意味を調べました。", "romaji": "kotoba no imi o shirabemashita.", "meaning": "I looked up the meaning of the word."},
    ],
    "読": [
        {"japanese": "本を読むのが好きです。", "romaji": "hon o yomu no ga suki desu.", "meaning": "I like reading books."},
        {"japanese": "この漢字は何と読みますか。", "romaji": "kono kanji wa nan to yomimasu ka.", "meaning": "How do you read this kanji?"},
    ],
    "書": [
        {"japanese": "手紙を書きました。", "romaji": "tegami o kakimashita.", "meaning": "I wrote a letter."},
        {"japanese": "辞書を買いました。", "romaji": "jisho o kaimashita.", "meaning": "I bought a dictionary."},
    ],
    "話": [
        {"japanese": "日本語で話しましょう。", "romaji": "nihongo de hanashimashou.", "meaning": "Let's speak in Japanese."},
        {"japanese": "電話番号を教えてください。", "romaji": "denwa bangou o oshiete kudasai.", "meaning": "Please tell me your phone number."},
    ],
    "来": [
        {"japanese": "日本に来ました。", "romaji": "nihon ni kimashita.", "meaning": "I came to Japan."},
        {"japanese": "来週、試験があります。", "romaji": "raishuu, shiken ga arimasu.", "meaning": "There is an exam next week."},
    ],
    "行": [
        {"japanese": "学校に行きます。", "romaji": "gakkou ni ikimasu.", "meaning": "I go to school."},
        {"japanese": "銀行でお金を下ろしました。", "romaji": "ginkou de okane o oroshimashita.", "meaning": "I withdrew money at the bank."},
    ],
    "帰": [
        {"japanese": "家に帰ります。", "romaji": "ie ni kaerimasu.", "meaning": "I go home."},
        {"japanese": "本を図書館に返します。", "romaji": "hon o toshokan ni kaeshimasu.", "meaning": "I return the book to the library."},
    ],
    "出": [
        {"japanese": "外に出ます。", "romaji": "soto ni demasu.", "meaning": "I go outside."},
        {"japanese": "宿題を出しました。", "romaji": "shukudai o dashimashita.", "meaning": "I submitted my homework."},
        {"japanese": "出発は七時です。", "romaji": "shuppatsu wa shichiji desu.", "meaning": "Departure is at seven."},
    ],
    "入": [
        {"japanese": "部屋に入ります。", "romaji": "heya ni hairimasu.", "meaning": "I enter the room."},
        {"japanese": "入学式は四月です。", "romaji": "nyuugakushiki wa shigatsu desu.", "meaning": "The entrance ceremony is in April."},
    ],
    "休": [
        {"japanese": "日曜日は休みます。", "romaji": "nichiyoubi wa yasumimasu.", "meaning": "I rest on Sunday."},
        {"japanese": "休憩しましょう。", "romaji": "kyuukei shimashou.", "meaning": "Let's take a break."},
    ],
    "起": [
        {"japanese": "毎朝六時に起きます。", "romaji": "maiasa rokuji ni okimasu.", "meaning": "I wake up at six every morning."},
        {"japanese": "問題が起きました。", "romaji": "mondai ga okimashita.", "meaning": "A problem occurred."},
    ],
    "寝": [
        {"japanese": "昨夜、早く寝ました。", "romaji": "sakuya, hayaku nemashita.", "meaning": "I slept early last night."},
        {"japanese": "寝室は二階です。", "romaji": "shinshitsu wa nikai desu.", "meaning": "The bedroom is on the second floor."},
    ],
    "買": [
        {"japanese": "スーパーで野菜を買いました。", "romaji": "suupaa de yasai o kaimashita.", "meaning": "I bought vegetables at the supermarket."},
        {"japanese": "買い物に行きましょう。", "romaji": "kaimono ni ikimashou.", "meaning": "Let's go shopping."},
    ],
    "売": [
        {"japanese": "この店では切手を売っています。", "romaji": "kono mise de wa kitte o utte imasu.", "meaning": "This store sells stamps."},
        {"japanese": "その本はよく売れています。", "romaji": "sono hon wa yoku urete imasu.", "meaning": "That book is selling well."},
    ],
    "高": [
        {"japanese": "この山は高いです。", "romaji": "kono yama wa takai desu.", "meaning": "This mountain is tall."},
        {"japanese": "物価が高いですね。", "romaji": "bukka ga takai desu ne.", "meaning": "Prices are high, aren't they."},
    ],
    "安": [
        {"japanese": "この店は安いです。", "romaji": "kono mise wa yasui desu.", "meaning": "This store is cheap."},
        {"japanese": "安心してください。", "romaji": "anshin shite kudasai.", "meaning": "Please rest assured."},
    ],
    "新": [
        {"japanese": "新しい車を買いました。", "romaji": "atarashii kuruma o kaimashita.", "meaning": "I bought a new car."},
        {"japanese": "新年明けましておめでとう。", "romaji": "shinnen akemashite omedetou.", "meaning": "Happy New Year."},
    ],
    "古": [
        {"japanese": "この古い時計は価値があります。", "romaji": "kono furui tokei wa kachi ga arimasu.", "meaning": "This old clock is valuable."},
        {"japanese": "古着をリサイクルしましょう。", "romaji": "furugi o risaikuru shimashou.", "meaning": "Let's recycle old clothes."},
    ],
    "長": [
        {"japanese": "髪が長いです。", "romaji": "kami ga nagai desu.", "meaning": "My hair is long."},
        {"japanese": "校長先生にお会いしました。", "romaji": "kouchou sensei ni o ai shimashita.", "meaning": "I met with the principal."},
    ],
    "短": [
        {"japanese": "冬は日が短いです。", "romaji": "fuyu wa hi ga mijikai desu.", "meaning": "The days are short in winter."},
        {"japanese": "時間が短すぎます。", "romaji": "jikan ga mijika sugimasu.", "meaning": "The time is too short."},
    ],
    "多": [
        {"japanese": "人が多いですね。", "romaji": "hito ga ooi desu ne.", "meaning": "There are many people."},
        {"japanese": "多分、明日も雨です。", "romaji": "tabun, ashita mo ame desu.", "meaning": "Probably it will rain tomorrow too."},
    ],
    "少": [
        {"japanese": "お金が少ないです。", "romaji": "okane ga sukunai desu.", "meaning": "I have little money."},
        {"japanese": "少し待ってください。", "romaji": "sukoshi matte kudasai.", "meaning": "Please wait a moment."},
    ],
    "好": [
        {"japanese": "すしが好きです。", "romaji": "sushi ga suki desu.", "meaning": "I like sushi."},
        {"japanese": "この機会を好んで選びました。", "romaji": "kono kikai o konde erabimashita.", "meaning": "I chose this opportunity willingly."},
    ],
    "天": [
        {"japanese": "天気がいいですね。", "romaji": "tenki ga ii desu ne.", "meaning": "The weather is nice."},
        {"japanese": "天井に蛍光灯があります。", "romaji": "tenjou ni keikoutou ga arimasu.", "meaning": "There is a fluorescent light on the ceiling."},
    ],
    "気": [
        {"japanese": "気をつけてください。", "romaji": "ki o tsukete kudasai.", "meaning": "Please be careful."},
        {"japanese": "天気がいいですね。", "romaji": "tenki ga ii desu ne.", "meaning": "The weather is nice."},
    ],
    "電": [
        {"japanese": "電車で行きます。", "romaji": "densha de ikimasu.", "meaning": "I'll go by train."},
        {"japanese": "電気を消してください。", "romaji": "denki o keshite kudasai.", "meaning": "Please turn off the light."},
    ],
    "車": [
        {"japanese": "車で送ります。", "romaji": "kuruma de okurimasu.", "meaning": "I'll take you by car."},
        {"japanese": "電車が遅れました。", "romaji": "densha ga okuremashita.", "meaning": "The train was late."},
    ],
    "駅": [
        {"japanese": "駅で待っています。", "romaji": "eki de matte imasu.", "meaning": "I'm waiting at the station."},
        {"japanese": "東京駅は大きいです。", "romaji": "toukyou eki wa ookii desu.", "meaning": "Tokyo Station is big."},
    ],
    "道": [
        {"japanese": "この道をまっすぐ行ってください。", "romaji": "kono michi o massugu itte kudasai.", "meaning": "Please go straight down this road."},
        {"japanese": "道具を貸してください。", "romaji": "dougu o kashite kudasai.", "meaning": "Please lend me the tool."},
    ],
    "毎": [
        {"japanese": "毎朝ジョギングをします。", "romaji": "maiasa jogingu o shimasu.", "meaning": "I jog every morning."},
        {"japanese": "毎日勉強しています。", "romaji": "mainichi benkyou shite imasu.", "meaning": "I study every day."},
    ],
    "週": [
        {"japanese": "週末に映画を見ます。", "romaji": "shuumatsu ni eiga o mimasu.", "meaning": "I watch a movie on the weekend."},
        {"japanese": "先週、風邪をひきました。", "romaji": "senshuu, kaze o hikimashita.", "meaning": "I caught a cold last week."},
    ],
    "間": [
        {"japanese": "駅と学校の間にあります。", "romaji": "eki to gakkou no aida ni arimasu.", "meaning": "It's between the station and the school."},
        {"japanese": "時間がありません。", "romaji": "jikan ga arimasen.", "meaning": "There's no time."},
    ],
    "門": [
        {"japanese": "門の前に立ちました。", "romaji": "mon no mae ni tachimashita.", "meaning": "I stood in front of the gate."},
        {"japanese": "専門学校に通っています。", "romaji": "senmon gakkou ni kayotte imasu.", "meaning": "I attend a vocational school."},
    ],
    "口": [
        {"japanese": "口を開けてください。", "romaji": "kuchi o akete kudasai.", "meaning": "Please open your mouth."},
        {"japanese": "入口はあちらです。", "romaji": "iriguchi wa achira desu.", "meaning": "The entrance is over there."},
    ],
    "目": [
        {"japanese": "目が痛いです。", "romaji": "me ga itai desu.", "meaning": "My eyes hurt."},
        {"japanese": "目的は何ですか。", "romaji": "mokuteki wa nan desu ka.", "meaning": "What is your purpose?"},
    ],
    "耳": [
        {"japanese": "耳が大きいです。", "romaji": "mimi ga ookii desu.", "meaning": "My ears are big."},
        {"japanese": "耳鼻科に行きました。", "romaji": "jibika ni ikimashita.", "meaning": "I went to the otolaryngologist."},
    ],
    "手": [
        {"japanese": "手を洗いましょう。", "romaji": "te o araimashou.", "meaning": "Let's wash our hands."},
        {"japanese": "切手を買いました。", "romaji": "kitte o kaimashita.", "meaning": "I bought stamps."},
    ],
    "足": [
        {"japanese": "足が疲れました。", "romaji": "ashi ga tsukaremashita.", "meaning": "My feet are tired."},
        {"japanese": "時間が足りません。", "romaji": "jikan ga tarimasen.", "meaning": "There isn't enough time."},
    ],
}

# ── Kanji sentence templates for N4-N2 ────────────────────────────────

def make_kanji_sentence(kanji, reading_label):
    """Generate ~2 sentences for a given kanji using its reading context."""
    return [
        {
            "japanese": f"{kanji}という漢字の読み方を練習しています。",
            "romaji": f"{kanji} to iu kanji no yomikata o renshuu shite imasu.",
            "meaning": f"I'm practicing how to read the kanji {kanji}."
        },
        {
            "japanese": f"この文章には{kanji}が使われています。",
            "romaji": f"kono bunshou ni wa {kanji} ga tsukawarete imasu.",
            "meaning": f"The kanji {kanji} is used in this sentence."
        },
    ]


# ── Vocab sentence generation ─────────────────────────────────────────

def make_vocab_sentence(word, reading, meaning):
    w = clean_word(word)
    if "・" in w:
        w = w.split("・")[0].strip()
    meaning_short = meaning.split(";")[0].split("<br")[0].strip() if meaning else ""

    return [{
        "japanese": f"「{w}」の意味は「{meaning_short}」です。",
        "romaji": f"「{w}」no imi wa「{meaning_short}」desu.",
        "meaning": f"The meaning of {w} is {meaning_short}."
    }]


# ── Data readers ──────────────────────────────────────────────────────

def read_n5_kanji_list():
    path = os.path.join(RAW_DIR, "n5_kanji.json")
    with open(path, "r", encoding="utf-8") as f:
        return [entry["kanji"] for entry in json.load(f)]


def read_tsv_kanji(path, col=1, header_check="kanji"):
    entries = []
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            parts = line.split("\t")
            if len(parts) <= col:
                continue
            k = parts[col].strip()
            if k == header_check:
                continue
            if k:
                entries.append(k)
    return entries


def read_vocab_tsv(path, word_col, reading_col, meaning_col):
    entries = []
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            cols = line.split("\t")
            word = cols[word_col].strip() if len(cols) > word_col else ""
            reading = cols[reading_col].strip() if len(cols) > reading_col else ""
            meaning = cols[meaning_col].strip() if len(cols) > meaning_col else ""
            if word:
                entries.append((word, reading, meaning))
    return entries


# ── Generator functions ───────────────────────────────────────────────

def gen_n5_kanji():
    print("N5 kanji...")
    chars = read_n5_kanji_list()
    data = [N5_EXAMPLES.get(c, make_kanji_sentence(c, "")) for c in chars]
    write_examples("n5_kanji_examples.json", data)


def gen_n4_kanji():
    print("N4 kanji...")
    chars = read_tsv_kanji(os.path.join(RAW_DIR, "n4_kanji.txt"))
    data = [make_kanji_sentence(c, "") for c in chars]
    write_examples("n4_kanji_examples.json", data)


def gen_n3_kanji():
    print("N3 kanji...")
    chars = read_tsv_kanji(os.path.join(RAW_DIR, "n3_kanji.txt"))
    data = [make_kanji_sentence(c, "") for c in chars]
    write_examples("n3_kanji_examples.json", data)


def gen_n2_kanji():
    print("N2 kanji...")
    chars = read_tsv_kanji(os.path.join(RAW_DIR, "n2_kanji.txt"), col=1, header_check="")
    data = [make_kanji_sentence(c, "") for c in chars]
    write_examples("n2_kanji_examples.json", data)


def gen_n5_vocab():
    print("N5 vocab...")
    entries = read_vocab_tsv(os.path.join(RAW_DIR, "n5_vocab.txt"), word_col=4, reading_col=5, meaning_col=9)
    data = [make_vocab_sentence(w, r, m) for w, r, m in entries]
    write_examples("n5_vocab_examples.json", data)


def gen_n4_vocab():
    print("N4 vocab...")
    entries = read_vocab_tsv(os.path.join(RAW_DIR, "n4_vocab.txt"), word_col=3, reading_col=1, meaning_col=2)
    data = [make_vocab_sentence(w, r, m) for w, r, m in entries]
    write_examples("n4_vocab_examples.json", data)


def gen_n3_vocab():
    print("N3 vocab...")
    entries = read_vocab_tsv(os.path.join(RAW_DIR, "n3_vocab.txt"), word_col=1, reading_col=3, meaning_col=2)
    data = [make_vocab_sentence(w, r, m) for w, r, m in entries]
    write_examples("n3_vocab_examples.json", data)


def gen_n2_vocab():
    print("N2 vocab...")
    entries = read_vocab_tsv(os.path.join(RAW_DIR, "n2_vocab.txt"), word_col=1, reading_col=2, meaning_col=3)
    data = [make_vocab_sentence(w, r, m) for w, r, m in entries]
    write_examples("n2_vocab_examples.json", data)


def main():
    print("=== Generating example sentence files ===\n")
    gen_n5_kanji()
    gen_n4_kanji()
    gen_n3_kanji()
    gen_n2_kanji()
    gen_n5_vocab()
    gen_n4_vocab()
    gen_n3_vocab()
    gen_n2_vocab()
    print("\nDone!")


if __name__ == "__main__":
    main()
