package com.app.quizandroidapp.model


data class Question(
    val text: String,
    val imageResource: Int,
    val options: List<String>,
    val correctAnswer: String
)