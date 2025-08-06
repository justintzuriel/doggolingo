package com.example.doggolingo.ui.models

data class AnswerResult(
    val state: AnswerState,
    val choice: String
)

enum class AnswerState {
    NONE, CORRECT, WRONG
}

enum class NavigationEvent(val route: String) {
    TITLE("title"),
    LOADING("loading"),
    QUIZ("quiz"),
    RESULT("result");
}