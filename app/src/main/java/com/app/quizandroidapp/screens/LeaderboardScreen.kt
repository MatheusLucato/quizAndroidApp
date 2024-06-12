package com.app.quizandroidapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.quizandroidapp.database.AppDatabase
import com.app.quizandroidapp.database.Leaderboard
import kotlinx.coroutines.launch

@Composable
fun LeaderboardScreen(database: AppDatabase) {
    val leaderboardEntries = remember { mutableStateOf<List<Leaderboard>?>(null) }

    LaunchedEffect(true) {
        leaderboardEntries.value = database.leaderboardDao().getTopLeaderboard()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "RANKING\nTOP 10 pontuações",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(24.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))


            leaderboardEntries.value?.forEachIndexed { index, leaderboard ->
                Text(
                    text = "${index + 1}. ${leaderboard.name} - ${leaderboard.score} Pontos",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
