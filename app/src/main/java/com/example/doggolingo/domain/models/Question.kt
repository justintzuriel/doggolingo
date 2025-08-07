package com.example.doggolingo.domain.models

data class Question(
    val imageUrl: String,
    val choices: List<String>,
    val answer: String
)