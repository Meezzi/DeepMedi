package com.meezzi.deepmedi.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.meezzi.deepmedi.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    private const val LOGIN_URL = "https://iot.deep-medi.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    @Provides
    @Singleton
    @Named("LoginRetrofit")
    fun provideLoginRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("LoginApiService")
    fun provideLoginApiService(@Named("LoginRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
