package com.freelancekc.puzzlepals.data.di

import com.freelancekc.puzzlepals.data.remote.PuzzleApiService
import com.freelancekc.puzzlepals.data.remote.PuzzleRemoteDataSource
import com.freelancekc.puzzlepals.data.remote.PuzzleRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.puzzlepals.com/") // If I had a real API, I would put the base URL here
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePuzzleApiService(retrofit: Retrofit): PuzzleApiService {
        return retrofit.create(PuzzleApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePuzzleRemoteDataSource(
        apiService: PuzzleApiService
    ): PuzzleRemoteDataSource {
        return PuzzleRemoteDataSourceImpl(apiService)
    }
} 