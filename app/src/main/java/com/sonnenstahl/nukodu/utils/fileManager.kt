package com.sonnenstahl.nukodu.utils

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

const val CURRENT_GAME_FN: String = "current_game.json";
const val USER_FN: String = "user.json"

fun saveGame(context: Context, currentGame: Game, filename: String? = CURRENT_GAME_FN) {
    val jsonString = Json.encodeToString(currentGame)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }

}

/**
 * returns null if there was a problem accessing the file i
 * i.e file is corrupted or wrong filename was given
 */
fun loadGame(context: Context, filename: String? = CURRENT_GAME_FN): Game? {
    return try {
        val json = context.openFileInput(filename).bufferedReader().use { it.readText() }
        val game = Json.decodeFromString<Game>(json)
        if (game.nudokuGrid.all { row -> row.all { it.number == 0 } }) {
            null
        } else game
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun saveUser(context: Context, user: User, filename: String? = USER_FN) {
    val jsonString = Json.encodeToString(user)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}

/**
 * returns null if there was a problem accessing the file i
 * i.e file is corrupted or wrong filename was given
 */
fun loadUser(context: Context, filename: String? = CURRENT_GAME_FN): User? {
    return try {
        val json = context.openFileInput(filename).bufferedReader().use { it.readText() }
        Json.decodeFromString<User>(json)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


/**
 * deletes a file and returns true if it was deleted, false if not
 */
fun deleteFile(context: Context, filename: String? = CURRENT_GAME_FN) = context.deleteFile(filename)