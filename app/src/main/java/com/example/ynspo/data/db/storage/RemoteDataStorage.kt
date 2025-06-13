package com.example.ynspo.data.db.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ynspo.data.db.Friend
import com.example.ynspo.data.db.FriendDao


@Database(entities = [Friend::class], version = 1)
abstract class LearningAndroidDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile
        private var INSTANCE: LearningAndroidDatabase? = null

        fun getDatabase(context: Context): LearningAndroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LearningAndroidDatabase::class.java,
                    "learning_android_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}