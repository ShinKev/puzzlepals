package com.freelancekc.puzzlepals.di

import com.freelancekc.puzzlepals.data.repository.PuzzleRepositoryImpl
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindPuzzleRepository(
        puzzleRepositoryImpl: PuzzleRepositoryImpl
    ): PuzzleRepository
}
