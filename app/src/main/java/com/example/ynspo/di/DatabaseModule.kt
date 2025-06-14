package com.example.ynspo.di

import android.content.Context
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.repository.BoardsRoomRepository
import com.example.ynspo.data.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideYnspoDatabase(@ApplicationContext context: Context): YnspoDatabase {
        return YnspoDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBoardsRoomRepository(database: YnspoDatabase): BoardsRoomRepository {
        return BoardsRoomRepository(database)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(database: YnspoDatabase): UserProfileRepository {
        return UserProfileRepository(database)
    }
}
