package utils

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

fun saveGame(context: Context, currentGame: Game, filename: String? = "test.json") {
    val jsonString = Json.encodeToString(currentGame)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}

fun loadGame(context: Context, filename: String? = "Test.json"): Game? {
    return try {
        val json = context.openFileInput(filename).bufferedReader().use { it.readText() }
        Json.decodeFromString<Game>(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}