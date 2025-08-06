package com.example.doggolingo.domain

import android.content.Context
import com.example.doggolingo.domain.models.Question

interface DoggoRepository {

    /**
     * Get all dog breeds as a set
     */
    suspend fun getAllBreeds(): Set<String>

    /**
     * Get questions and preload images
     */
    suspend fun loadQuestions(context: Context, breeds: Set<String>): List<Question>
}