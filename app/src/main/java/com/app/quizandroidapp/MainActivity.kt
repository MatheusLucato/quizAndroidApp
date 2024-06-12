package com.app.quizandroidapp

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.app.quizandroidapp.database.AppDatabase
import com.app.quizandroidapp.screens.LeaderboardScreen
import com.app.quizandroidapp.screens.MainMenuScreen
import com.app.quizandroidapp.screens.QuizScreen
import com.app.quizandroidapp.ui.theme.QuizAndroidAppTheme

class MainActivity : ComponentActivity() {
    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "quiz").build()
            launch(Dispatchers.Main) {
                setupContent()
            }
        }
    }

    private fun setupContent() {
        setContent {
            QuizAndroidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mainMenu") {
                        composable("mainMenu") {
                            MainMenuScreen(navController)
                        }
                        composable("quiz/{userName}") { backStackEntry ->
                            QuizScreen(navController, database!!, backStackEntry.arguments?.getString("userName") ?: "")
                        }
                        composable("leaderboard") {
                            LeaderboardScreen(database!!)
                        }
                    }
                }
            }
        }
    }
}
