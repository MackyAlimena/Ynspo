package com.example.ynspo.data.repository

import android.net.Uri
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.mapper.UserPinMapper
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.remote.UserPinsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPinsRepository @Inject constructor(
    private val userPinsDataSource: UserPinsDataSource,
    private val database: YnspoDatabase
) {
    
    //Sube un pin de usuario a Firebase y lo guarda localmente
    suspend fun uploadUserPin(
        imageUri: Uri,
        description: String?,
        hashtags: List<String>,
        keywords: List<String>,
        userId: String,
        userName: String?,
        userPhotoUrl: String?
    ): Result<UserPin> = withContext(Dispatchers.IO) {
        try {
            // Subir a Firebase
            val result = userPinsDataSource.uploadUserPin(
                imageUri = imageUri,
                description = description,
                hashtags = hashtags,
                keywords = keywords,
                userId = userId,
                userName = userName,
                userPhotoUrl = userPhotoUrl
            )
            
            if (result.isSuccess) {
                val userPin = result.getOrThrow()
                
                // Guardar localmente en Room
                val entity = UserPinMapper.toEntity(userPin)
                database.userPinDao().insertUserPin(entity)
                
                Result.success(userPin)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllUserPins(): Result<List<UserPin>> = withContext(Dispatchers.IO) {
        try {
            // Obtener de Room primero (caché local)
            val localPins = database.userPinDao().getAllUserPins().value ?: emptyList()
            val localUserPins = UserPinMapper.fromEntities(localPins)
            
            // Sincronizar con Firebase en background
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val firebaseResult = userPinsDataSource.getAllUserPins()
                    if (firebaseResult.isSuccess) {
                        val firebasePins = firebaseResult.getOrThrow()
                        
                        // Actualizar Room con los datos más recientes
                        val entities = UserPinMapper.toEntities(firebasePins)
                        database.userPinDao().insertUserPins(entities)
                    }
                } catch (e: Exception) {
                    println("Error sincronizando con Firebase: ${e.message}")
                }
            }
            
            Result.success(localUserPins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchUserPins(query: String): Result<List<UserPin>> = withContext(Dispatchers.IO) {
        try {
            // Buscar localmente primero
            val localResults = database.userPinDao().searchUserPins(query)
            val localUserPins = UserPinMapper.fromEntities(localResults)
            
            // También buscar en Firebase
            val firebaseResult = userPinsDataSource.searchUserPins(query)
            if (firebaseResult.isSuccess) {
                val firebasePins = firebaseResult.getOrThrow()
                
                // Combinar resultados (eliminar duplicados por ID)
                val combinedPins = (localUserPins + firebasePins)
                    .distinctBy { it.id }
                    .sortedByDescending { it.createdAt }
                
                // Actualizar Room con nuevos resultados
                val entities = UserPinMapper.toEntities(firebasePins)
                database.userPinDao().insertUserPins(entities)
                
                Result.success(combinedPins)
            } else {
                Result.success(localUserPins)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecentUserPins(limit: Int = 20): Result<List<UserPin>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("UserPinsRepository", "Obteniendo pins recientes, límite: $limit")
            
            // Obtener de Room
            val localPins = database.userPinDao().getRecentUserPins(limit)
            val localUserPins = UserPinMapper.fromEntities(localPins)
            
            android.util.Log.d("UserPinsRepository", "Pins locales obtenidos: ${localUserPins.size}")
            localUserPins.forEach { pin ->
                android.util.Log.d("UserPinsRepository", "Pin local: ${pin.id}, URLs: ${pin.urls.small}")
            }
            
            // Sincronizar con Firebase en background
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    android.util.Log.d("UserPinsRepository", "Sincronizando con Firebase...")
                    val firebaseResult = userPinsDataSource.getRecentUserPins(limit)
                    if (firebaseResult.isSuccess) {
                        val firebasePins = firebaseResult.getOrThrow()
                        android.util.Log.d("UserPinsRepository", "Pins de Firebase obtenidos: ${firebasePins.size}")
                        firebasePins.forEach { pin ->
                            android.util.Log.d("UserPinsRepository", "Pin Firebase: ${pin.id}, URLs: ${pin.urls.small}")
                        }
                        val entities = UserPinMapper.toEntities(firebasePins)
                        database.userPinDao().insertUserPins(entities)
                        android.util.Log.d("UserPinsRepository", "Pins de Firebase guardados en Room")
                    } else {
                        android.util.Log.e("UserPinsRepository", "Error obteniendo pins de Firebase: ${firebaseResult.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    android.util.Log.e("UserPinsRepository", "Error sincronizando pines recientes: ${e.message}", e)
                }
            }
            
            Result.success(localUserPins)
        } catch (e: Exception) {
            android.util.Log.e("UserPinsRepository", "Error en getRecentUserPins: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    suspend fun getUserPinsByUserId(userId: String): Result<List<UserPin>> = withContext(Dispatchers.IO) {
        try {
            val firebaseResult = userPinsDataSource.getUserPinsByUserId(userId)
            if (firebaseResult.isSuccess) {
                val firebasePins = firebaseResult.getOrThrow()
                
                // Guardar en Room
                val entities = UserPinMapper.toEntities(firebasePins)
                database.userPinDao().insertUserPins(entities)
                
                Result.success(firebasePins)
            } else {
                Result.failure(firebaseResult.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteUserPin(pinId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Eliminar de Firebase
            val firebaseResult = userPinsDataSource.deleteUserPin(pinId)
            
            if (firebaseResult.isSuccess) {
                // Eliminar de Room
                val entity = database.userPinDao().getUserPinById(pinId)
                entity?.let {
                    database.userPinDao().deleteUserPin(it)
                }
                
                Result.success(Unit)
            } else {
                Result.failure(firebaseResult.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    //Obtiene pines de usuario desde Room (para uso offline)
    suspend fun getLocalUserPins(): List<UserPin> = withContext(Dispatchers.IO) {
        try {
            val localPins = database.userPinDao().getAllUserPins().value ?: emptyList()
            UserPinMapper.fromEntities(localPins)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Borrar todos los UserPins (tanto de Room como de Firestore)
    suspend fun deleteAllUserPins(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("UserPinsRepository", "Iniciando borrado de todos los UserPins")
            
            // 1. Obtener todos los UserPins de Room
            val localPins = database.userPinDao().getAllUserPins().value ?: emptyList()
            android.util.Log.d("UserPinsRepository", "UserPins en Room: ${localPins.size}")
            
            // 2. Borrar de Firestore
            val firebaseResult = userPinsDataSource.deleteAllUserPins()
            if (firebaseResult.isSuccess) {
                android.util.Log.d("UserPinsRepository", "UserPins borrados de Firestore")
            } else {
                android.util.Log.e("UserPinsRepository", "Error borrando de Firestore: ${firebaseResult.exceptionOrNull()?.message}")
            }
            
            // 3. Borrar de Room
            database.userPinDao().deleteAllUserPins()
            android.util.Log.d("UserPinsRepository", "UserPins borrados de Room")
            
            // 4. Borrar también de board_items donde aparezcan
            localPins.forEach { pin ->
                database.boardItemDao().deleteBoardItemsByItemId(pin.id, "user_pin")
                android.util.Log.d("UserPinsRepository", "Relaciones de board_items borradas para: ${pin.id}")
            }
            
            android.util.Log.d("UserPinsRepository", "Borrado completo de todos los UserPins")
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("UserPinsRepository", "Error en deleteAllUserPins: ${e.message}", e)
            Result.failure(e)
        }
    }
} 