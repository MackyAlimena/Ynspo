package com.example.ynspo.di

import android.content.Context
import com.example.ynspo.data.remote.UserPinsDataSource
import com.example.ynspo.data.remote.ImgBBService
import com.google.firebase.firestore.FirebaseFirestore

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }



    @Provides
    @Singleton
    fun provideImgBBService(@ApplicationContext context: Context): ImgBBService {
        return ImgBBService(context)
    }

    @Provides
    @Singleton
    fun provideUserPinsDataSource(imgBBService: ImgBBService): UserPinsDataSource {
        return UserPinsDataSource(imgBBService)
    }
} 