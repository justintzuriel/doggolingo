package com.example.doggolingo.data

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.doggolingo.domain.DoggoRepository
import com.example.doggolingo.domain.models.Question
import javax.inject.Inject

class DoggoRepositoryImpl @Inject constructor(
    private val doggoApi: DoggoApi,
    private val imageLoader: ImageLoader
) : DoggoRepository {

    override suspend fun getAllBreeds(): Set<String> {
        val response = doggoApi.getAllBreeds()
        return if (response.isSuccessful) {
            response.body()?.message?.keys ?: setOf()
        } else {
            setOf()
        }
    }

    override suspend fun loadQuestions(context: Context, breeds: Set<String>): List<Question> {
        val questions = mutableListOf<Question>()
        for (i in 0 until 5) {
            val choiceBreeds = breeds.shuffled().take(4)
            val answerBreed = choiceBreeds.random()
            val response = doggoApi.getRandomImage(answerBreed)
            if (response.isSuccessful) {
                response.body()?.message?.let {
                    questions.add(Question(i, it, choiceBreeds, answerBreed))
                    val request = ImageRequest.Builder(context)
                        .data(it)
                        .build()
                    imageLoader.enqueue(request) // preload image
                }
            }
        }
        return questions
    }
}