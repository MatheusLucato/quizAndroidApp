package com.app.quizandroidapp.screens

import android.media.MediaDrm.LogMessage
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.quizandroidapp.R
import com.app.quizandroidapp.database.AppDatabase
import com.app.quizandroidapp.database.Leaderboard
import com.app.quizandroidapp.model.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuizScreen(navController: NavController, database: AppDatabase, username: String) {
    val questions = remember {
        listOf(
            Question("Qual a capital da França?", R.drawable.franca, listOf("Paris", "Roma", "Berlim", "Madrid"), "Paris"),
            Question("Qual a capital da Itália?", R.drawable.italia, listOf("Paris", "Roma", "Berlim", "Madrid"), "Roma"),
            Question("Qual a capital da Alemanha?", R.drawable.alemanha, listOf("Paris", "Roma", "Berlim", "Madrid"), "Berlim"),
            Question("Qual a capital da Espanha?", R.drawable.espanha, listOf("Paris", "Roma", "Berlim", "Madrid"), "Madrid"),
            Question("Qual a capital do Brasil?", R.drawable.brasil, listOf("Brasília", "Rio de Janeiro", "São Paulo", "Salvador"), "Brasília"),
            Question("Qual a capital do Japão?", R.drawable.japao, listOf("Tóquio", "Kyoto", "Osaka", "Hiroshima"), "Tóquio"),
            Question("Qual a capital da Rússia?", R.drawable.russia, listOf("Moscou", "São Petersburgo", "Novosibirsk", "Yekaterinburg"), "Moscou"),
            Question("Qual a capital da Índia?", R.drawable.india, listOf("Nova Deli", "Mumbai", "Calcutá", "Bangalore"), "Nova Deli"),
            Question("Qual a capital do México?", R.drawable.mexico, listOf("Oslo", "Sydney", "Luxemburgo", "Cidade do México"), "Cidade do México"),
            Question("Qual a capital do Canadá?", R.drawable.canada, listOf("Ottawa", "Toronto", "Vancouver", "Montreal"), "Ottawa")
        ).shuffled()
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]
    val shuffledOptions = remember(currentQuestion) { currentQuestion.options.shuffled() }
    var answerState by remember { mutableStateOf<Map<String, Color>?>(null) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var currentScore by remember { mutableStateOf(0) }  // Track the player's score

    LaunchedEffect(currentQuestionIndex) {
        answerState = null
        selectedAnswer = null
    }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != null) {
            delay(1500)
            if (selectedAnswer == currentQuestion.correctAnswer) {
                currentScore += 10
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    saveScoreAndNavigate(database, username, currentScore, navController)
                }
            } else {
                saveScoreAndNavigate(database, username, currentScore, navController)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = currentQuestion.text, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        Image(painter = painterResource(id = currentQuestion.imageResource), contentDescription = "Image for question ${currentQuestion.text}", modifier = Modifier.fillMaxWidth().height(200.dp))
        Spacer(modifier = Modifier.height(24.dp))

        shuffledOptions.forEach { option ->
            Button(
                onClick = {
                    answerState = shuffledOptions.associateWith { if (it == currentQuestion.correctAnswer) Color.Green else Color.Red }
                    selectedAnswer = option
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = answerState?.get(option) ?: MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(option)
            }
        }

        Text(
            text = "Pontuação atual: $currentScore",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        )
    }
}

private fun saveScoreAndNavigate(database: AppDatabase, username: String, score: Int, navController: NavController) {
    CoroutineScope(Dispatchers.IO).launch {
        database.leaderboardDao().insert(Leaderboard(name = username, score = score))
        withContext(Dispatchers.Main) {
            navController.navigate("mainMenu")
        }
    }
}




