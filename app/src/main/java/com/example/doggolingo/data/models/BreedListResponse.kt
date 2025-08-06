package com.example.doggolingo.data.models

data class BreedListResponse(
    val message: Map<String, List<String>>,
    val status: String
)