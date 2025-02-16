package com.meezzi.deepmedi.di

import android.content.Context
import com.meezzi.deepmedi.domain.camera.CameraService
import com.meezzi.deepmedi.presentation.ui.camera.CameraServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCameraService(
        @ApplicationContext context: Context
    ): CameraService {
        return CameraServiceImpl(context)
    }
}