package com.example.ynspo.di

import com.example.ynspo.security.BiometricAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideBiometricAuthManager(): BiometricAuthManager {
        return BiometricAuthManager()
    }
}
