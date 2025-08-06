package com.example.doggolingo.data

import com.example.doggolingo.data.models.BreedListResponse
import com.example.doggolingo.data.models.RandomImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DoggoApi {

    @GET("/api/breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedListResponse>

    @GET("/api/breed/{breed}/images/random")
    suspend fun getRandomImage(@Path("breed") breed: String): Response<RandomImageResponse>
}