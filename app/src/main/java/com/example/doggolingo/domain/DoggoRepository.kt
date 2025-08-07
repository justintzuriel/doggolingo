package com.example.doggolingo.domain

import com.example.doggolingo.domain.models.Question

/**
 * Repository responsible for generating quiz data
 */
interface DoggoRepository {

    /**
     * Gets all dog breeds as a set
     */
    suspend fun getAllBreeds(): Set<String>

    /**
     * Gets questions and preload images
     */
    suspend fun loadQuestions(breeds: Set<String>): List<Question>
}