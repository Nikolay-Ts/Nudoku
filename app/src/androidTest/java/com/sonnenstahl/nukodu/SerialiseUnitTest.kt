package com.sonnenstahl.nukodu

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import com.sonnenstahl.nukodu.utils.Difficulty
import com.sonnenstahl.nukodu.utils.Game
import com.sonnenstahl.nukodu.utils.GameState
import com.sonnenstahl.nukodu.utils.Pos
import com.sonnenstahl.nukodu.utils.Tile
import com.sonnenstahl.nukodu.utils.loadGame
import com.sonnenstahl.nukodu.utils.saveGame
import java.io.File

@RunWith(AndroidJUnit4::class)
class GameSerializationTest {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    @Test
    fun testGameSerializationAndSave() {

        val filename = "test_game_save.json"

        // Arrange: Create a test Game object
        val grid = Array(9) { row ->
            Array(9) { col ->
                Tile(
                    number = 0,
                    cell = Pos(row, col),
                    highlight = false,
                    isCompleted = false
                )
            }
        }

        val originalGame = Game(
            difficulty = Difficulty.EASY,
            NudokuGrid = grid,
            errors = 1,
            gamState = GameState.RUNNING,
            time = 123
        )

        // Act: Save to storage
        saveGame(context, originalGame, filename)

        // Assert: Load and check integrity
        val loadedGame = loadGame(context, filename)



        assertNotEquals(loadedGame, null)
        assertEquals(originalGame.difficulty, loadedGame?.difficulty)
        assertEquals(originalGame.errors, loadedGame?.errors)
        assertEquals(originalGame.gamState, loadedGame?.gamState)
        assertEquals(originalGame.time, loadedGame?.time)
        assertEquals(originalGame.NudokuGrid.size, loadedGame?.NudokuGrid?.size ?: 0)
        assertEquals(originalGame.NudokuGrid[0][0].cell,
            loadedGame?.NudokuGrid?.get(0)?.get(0)?.cell ?:0
        )

        // Cleanup
        File(context.filesDir, filename).delete()
    }

    @Test
    fun readEmptyFile() {
        val game = loadGame(context, "MeowMeow")

        assertEquals(game, null)
    }
}