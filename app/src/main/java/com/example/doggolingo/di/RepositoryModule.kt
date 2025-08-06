package com.example.doggolingo.di

import com.example.doggolingo.data.DoggoRepositoryImpl
import com.example.doggolingo.domain.DoggoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideDoggoRepository(impl: DoggoRepositoryImpl): DoggoRepository
}