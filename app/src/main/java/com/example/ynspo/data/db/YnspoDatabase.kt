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
import com.example.ynspo.data.db.dao.BoardPhotoDao
import com.example.ynspo.data.db.dao.PhotoDao
import com.example.ynspo.data.db.dao.UserProfileDao
import com.example.ynspo.data.db.entity.BoardEntity
import com.example.ynspo.data.db.entity.BoardPhotosCrossRef
import com.example.ynspo.data.db.entity.PhotoEntity
import com.example.ynspo.data.db.entity.UserHobbyEntity
import com.example.ynspo.data.db.entity.UserProfileEntity

@Database(
    entities = [
        BoardEntity::class,
        PhotoEntity::class,
        BoardPhotosCrossRef::class,
        UserProfileEntity::class,
        UserHobbyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class YnspoDatabase : RoomDatabase() {

    abstract fun boardDao(): BoardDao
    abstract fun photoDao(): PhotoDao
    abstract fun boardPhotoDao(): BoardPhotoDao
    abstract fun userProfileDao(): UserProfileDao

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
