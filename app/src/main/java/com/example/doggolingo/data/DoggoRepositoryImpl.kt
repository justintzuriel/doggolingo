package com.example.doggolingo.data

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.doggolingo.domain.DoggoRepository
import com.example.doggolingo.domain.models.Question
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DoggoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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

    override suspend fun loadQuestions(breeds: Set<String>): List<Question> = coroutineScope {
        val questions = (0 until 5).map {
            async {
                val choiceBreeds = breeds.shuffled().take(4)
                val answerBreed = choiceBreeds.random()
                val response = doggoApi.getRandomImage(answerBreed)
                if (response.isSuccessful) {
                    response.body()?.message?.let {
                        val request = ImageRequest.Builder(context)
                            .data(it)
                            .build()
                        imageLoader.execute(request) // preload all images
                        Question(it, choiceBreeds, answerBreed)
                    }
                } else {
                    null
                }
            }
        }

        questions.awaitAll().filterNotNull()
    }
}