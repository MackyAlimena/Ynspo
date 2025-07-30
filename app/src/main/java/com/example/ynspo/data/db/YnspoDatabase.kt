package com.example.ynspo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.ynspo.data.db.dao.BoardDao
import com.example.ynspo.data.db.dao.PhotoDao
import com.example.ynspo.data.db.dao.UserProfileDao
import com.example.ynspo.data.db.dao.FirebaseUserDao
import com.example.ynspo.data.db.dao.UserPinDao
import com.example.ynspo.data.db.dao.BoardItemDao
import com.example.ynspo.data.db.entity.BoardEntity
import com.example.ynspo.data.db.entity.PhotoEntity
import com.example.ynspo.data.db.entity.UserHobbyEntity
import com.example.ynspo.data.db.entity.UserProfileEntity
import com.example.ynspo.data.db.entity.FirebaseUserEntity
import com.example.ynspo.data.db.entity.UserPinEntity
import com.example.ynspo.data.db.entity.BoardItemEntity

@Database(
    entities = [
        BoardEntity::class,
        PhotoEntity::class,
        UserProfileEntity::class,
        UserHobbyEntity::class,
        FirebaseUserEntity::class,
        UserPinEntity::class,
        BoardItemEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class YnspoDatabase : RoomDatabase() {

    abstract fun boardDao(): BoardDao
    abstract fun photoDao(): PhotoDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun firebaseUserDao(): FirebaseUserDao
    abstract fun userPinDao(): UserPinDao
    abstract fun boardItemDao(): BoardItemDao

    companion object {
        @Volatile
        private var INSTANCE: YnspoDatabase? = null

        fun getDatabase(context: Context): YnspoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YnspoDatabase::class.java,
                    "ynspo_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    // Lanzar una corrutina para prepopular la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        prepopulateDatabase(database)
                    }
                }
            }
        }
        
        private suspend fun prepopulateDatabase(database: YnspoDatabase) {
            // Insertar tableros por defecto
            val boardDao = database.boardDao()
            val defaultBoards = listOf(
                BoardEntity(name = "Handmade Decor"),
                BoardEntity(name = "Knitting Ideas"),
                BoardEntity(name = "Summer Vibes")
            )
            defaultBoards.forEach { board ->
                boardDao.insertBoard(board)
            }
        }
    }
}
