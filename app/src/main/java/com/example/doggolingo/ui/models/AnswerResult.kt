package com.example.doggolingo.ui.models

data class AnswerResult(
    val state: AnswerState,
    val choice: String
)

enum class AnswerState {
    NONE,       // user has not answered
    CORRECT,    // user answered correctly
    WRONG       // user answered incorrectly
}