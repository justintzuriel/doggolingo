package com.example.doggolingo.data

import android.content.Context
import coil.ImageLoader
import com.example.doggolingo.data.models.BreedListResponse
import com.example.doggolingo.data.models.RandomImageResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import kotlin.test.assertEquals

class DoggoRepositoryImplTest {

     lateinit var repositoryImpl: DoggoRepositoryImpl
     val breedSet = setOf("b1", "b2", "b3", "b4")

     class FakeDoggoApi : DoggoApi {

         override suspend fun getAllBreeds(): Response<BreedListResponse> {
             val breedMap = mapOf<String, List<String>>(
                 "b1" to emptyList(),
                 "b2" to emptyList(),
                 "b3" to emptyList(),
                 "b4" to emptyList()
             )
             return Response.success(BreedListResponse(breedMap, ""))
         }

         override suspend fun getRandomImage(breed: String): Response<RandomImageResponse> {
             return Response.success(RandomImageResponse("testUrl", ""))
         }
     }

     @OptIn(ExperimentalCoroutinesApi::class)
     @BeforeEach
     fun setUp() {
         Dispatchers.setMain(StandardTestDispatcher())
         val context: Context = mockk()
         val imageLoader: ImageLoader = mockk()
         coEvery { imageLoader.execute(any()) } returns mockk()
         repositoryImpl = DoggoRepositoryImpl(context, FakeDoggoApi(), imageLoader)
     }

    @Test
    fun `getAllBreeds returns breeds as set`() = runTest {
        assertEquals(breedSet, repositoryImpl.getAllBreeds())
    }

    @Test
    fun `loadQuestions returns valid questions with unique choices`() = runTest {
        val questions = repositoryImpl.loadQuestions(breedSet)
        assertEquals(5, questions.size)

        questions.forEach {
            val (url, choices, answer) = it
            assertEquals("testUrl", url)
            assertEquals(4, choices.size)
            assert(answer in choices)
        }
    }
}