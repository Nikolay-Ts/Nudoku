package com.sonnenstahl.nukodu.utils


import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate

/**
 * Given the user, it converts all of their stuff to the csv file
 */
fun userToCsv(user: User): String {
    val sb = StringBuilder()

    sb.appendLine("Difficulty,Wins,Tries,Best Time,Worst Time,Average Time")

    listOf(
        "Easy" to user.easy,
        "Medium" to user.medium,
        "Hard" to user.hard,
        "Expert" to user.expert
    ).forEach { (label, data) ->
        sb.appendLine("$label,${data.wins},${data.tries},${data.bestTime},${data.worstTime},${data.avgTime}")
    }

    sb.appendLine()
    sb.appendLine("Game Completion Dates")

    user.gameCompletionDates.forEach { date ->
        sb.appendLine(date.toString())
    }

    return sb.toString()
}


/**
 * converts this to a csv to then be chosen by the user where to put
 */
fun writeCsvToUri(context: Context, uri: Uri, csv: String) {
    context.contentResolver.openOutputStream(uri)?.use { output ->
        output.write(csv.toByteArray())
    }
}


/**
 * if you want to save the user data as a csv somewhere in their storage, call this
 */
@Composable
fun ExportCsvButton(user: User, context: Context) {
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri: Uri? ->
            uri?.let {
                writeCsvToUri(context, it, userToCsv(user))
            }
        }
    )

    OutlinedButton(onClick = {
        createFileLauncher.launch("user_stats.csv")
    }) {
        Text("Export as CSV")
    }
}