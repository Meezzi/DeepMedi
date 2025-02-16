package com.meezzi.deepmedi.di

import com.meezzi.deepmedi.data.api.ApiService
import com.meezzi.deepmedi.data.repository.UploadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUploadRepository(
        @Named("UploadApiService") uploadApiService: ApiService,
        @Named("LoginApiService") loginApiService: ApiService
    ): UploadRepository {
        return UploadRepository(uploadApiService, loginApiService)
    }
}