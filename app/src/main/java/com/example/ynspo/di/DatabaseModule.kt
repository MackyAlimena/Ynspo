package com.example.ynspo.di

import android.content.Context
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.dao.FirebaseUserDao
import com.example.ynspo.data.db.dao.UserProfileDao
import com.example.ynspo.data.db.dao.UserPinDao
import com.example.ynspo.data.repository.BoardsMixedRepository
import com.example.ynspo.data.repository.UserProfileRepository
import com.example.ynspo.data.repository.UserPinsRepository
import com.example.ynspo.data.repository.CombinedPinsRepository
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
    fun provideUserProfileRepository(database: YnspoDatabase): UserProfileRepository {
        return UserProfileRepository(database)
    }

    @Provides
    @Singleton
    fun provideFirebaseUserDao(database: YnspoDatabase): FirebaseUserDao {
        return database.firebaseUserDao()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: YnspoDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideUserPinDao(database: YnspoDatabase): UserPinDao {
        return database.userPinDao()
    }

    @Provides
    @Singleton
    fun provideBoardItemDao(database: YnspoDatabase): com.example.ynspo.data.db.dao.BoardItemDao {
        return database.boardItemDao()
    }

    @Provides
    @Singleton
    fun provideUserPinsRepository(
        userPinsDataSource: com.example.ynspo.data.remote.UserPinsDataSource,
        database: YnspoDatabase
    ): UserPinsRepository {
        return UserPinsRepository(userPinsDataSource, database)
    }

    @Provides
    @Singleton
    fun provideCombinedPinsRepository(
        unsplashRepository: com.example.ynspo.data.repository.UnsplashRepository,
        userPinsRepository: UserPinsRepository
    ): CombinedPinsRepository {
        return CombinedPinsRepository(unsplashRepository, userPinsRepository)
    }

    @Provides
    @Singleton
    fun provideBoardsMixedRepository(database: YnspoDatabase): BoardsMixedRepository {
        return BoardsMixedRepository(database)
    }
}
