package com.example.doggolingo.ui.models

enum class NavigationEvent(val route: String) {
    TITLE("title"),
    LOADING("loading"),
    QUIZ("quiz"),
    RESULT("result");
}