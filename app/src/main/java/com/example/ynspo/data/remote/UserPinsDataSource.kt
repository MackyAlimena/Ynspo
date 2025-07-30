package com.example.ynspo.data.remote

import android.net.Uri
import android.util.Log
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.model.UserPinUrls
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPinsDataSource @Inject constructor(
    private val imgBBService: ImgBBService
) {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val userPinsCollection = firestore.collection("user_pins")
    
    suspend fun uploadUserPin(
        imageUri: Uri,
        description: String?,
        hashtags: List<String>,
        keywords: List<String>,
        userId: String,
        userName: String?,
        userPhotoUrl: String?
    ): Result<UserPin> {
        return try {
            Log.d("UserPinsDataSource", "=== INICIANDO UPLOAD CON IMGBB ===")
            Log.d("UserPinsDataSource", "Description: $description")
            Log.d("UserPinsDataSource", "Keywords: $keywords")
            Log.d("UserPinsDataSource", "Hashtags: $hashtags")
            
            val pinId = UUID.randomUUID().toString()
            Log.d("UserPinsDataSource", "Pin ID generado: $pinId")
            
            // Subir imagen a ImgBB
            Log.d("UserPinsDataSource", "Subiendo imagen a ImgBB...")
            val imageUploadResult = imgBBService.uploadImage(imageUri)
            
            if (imageUploadResult.isSuccess) {
                val imageUrl = imageUploadResult.getOrThrow()
                Log.d("UserPinsDataSource", "Imagen subida exitosamente a ImgBB: $imageUrl")
                
                val urls = UserPinUrls(
                    small = imageUrl,
                    regular = imageUrl,
                    full = imageUrl
                )
                
                Log.d("UserPinsDataSource", "URLs configuradas: ${urls.small}")
                
                // Crear el UserPin
                val userPin = UserPin(
                    id = pinId,
                    description = description,
                    urls = urls,
                    userId = userId,
                    userName = userName,
                    userPhotoUrl = userPhotoUrl,
                    hashtags = hashtags,
                    keywords = keywords,
                    createdAt = System.currentTimeMillis(),
                    isUserPin = true
                )
                
                Log.d("UserPinsDataSource", "UserPin creado con ID: ${userPin.id}")
                
                // Guardar en Firestore
                Log.d("UserPinsDataSource", "Guardando en Firestore...")
                Log.d("UserPinsDataSource", "Collection path: ${userPinsCollection.path}")
                
                val pinData = mapOf(
                    "id" to userPin.id,
                    "description" to userPin.description,
                    "smallUrl" to userPin.urls.small,
                    "regularUrl" to userPin.urls.regular,
                    "fullUrl" to userPin.urls.full,
                    "userId" to userPin.userId,
                    "userName" to userPin.userName,
                    "userPhotoUrl" to userPin.userPhotoUrl,
                    "hashtags" to userPin.hashtags,
                    "keywords" to userPin.keywords,
                    "createdAt" to userPin.createdAt,
                    "isUserPin" to userPin.isUserPin
                )
                
                Log.d("UserPinsDataSource", "Pin data preparado: $pinData")
                
                userPinsCollection.document(pinId).set(pinData).await()
                Log.d("UserPinsDataSource", "Pin guardado exitosamente en Firestore")
                Log.d("UserPinsDataSource", "=== UPLOAD COMPLETADO ===")
                
                Result.success(userPin)
            } else {
                Log.e("UserPinsDataSource", "Error al subir imagen a ImgBB: ${imageUploadResult.exceptionOrNull()?.message}")
                Result.failure(imageUploadResult.exceptionOrNull() ?: Exception("Error desconocido al subir imagen"))
            }
            
        } catch (e: Exception) {
            Log.e("UserPinsDataSource", "=== ERROR EN UPLOAD ===")
            Log.e("UserPinsDataSource", "Error al subir pin: ${e.message}", e)
            Log.e("UserPinsDataSource", "Error stack trace: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }
    
    suspend fun getAllUserPins(): Result<List<UserPin>> {
        return try {
            val snapshot = userPinsCollection
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val userPins = snapshot.documents.mapNotNull { doc ->
                doc.toUserPin()
            }
            
            Result.success(userPins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchUserPins(query: String): Result<List<UserPin>> {
        return try {
            val searchQuery = query.lowercase().trim()
            if (searchQuery.isEmpty()) {
                return Result.success(emptyList())
            }

            // Realizar múltiples búsquedas para cubrir diferentes campos
            val allResults = mutableListOf<UserPin>()
            
            // 1. Buscar por keywords (array contains)
            try {
                val keywordsSnapshot = userPinsCollection
                    .whereArrayContains("keywords", searchQuery)
                    .get()
                    .await()
                
                keywordsSnapshot.documents.mapNotNull { doc ->
                    doc.toUserPin()
                }.let { allResults.addAll(it) }
            } catch (e: Exception) {
                // Ignorar errores de búsqueda específica
            }
            
            // 2. Buscar por hashtags (array contains)
            try {
                val hashtagsSnapshot = userPinsCollection
                    .whereArrayContains("hashtags", searchQuery)
                    .get()
                    .await()
                
                hashtagsSnapshot.documents.mapNotNull { doc ->
                    doc.toUserPin()
                }.let { allResults.addAll(it) }
            } catch (e: Exception) {
                // Ignorar errores de búsqueda específica
            }
            
            // 3. Buscar por descripción (texto completo)
            try {
                val descriptionSnapshot = userPinsCollection
                    .whereGreaterThanOrEqualTo("description", searchQuery)
                    .whereLessThanOrEqualTo("description", searchQuery + '\uf8ff')
                    .get()
                    .await()
                
                descriptionSnapshot.documents.mapNotNull { doc ->
                    doc.toUserPin()
                }.let { allResults.addAll(it) }
            } catch (e: Exception) {
                // Ignorar errores de búsqueda específica
            }
            
            // Eliminar duplicados y ordenar por fecha de creación
            val uniqueResults = allResults
                .distinctBy { it.id }
                .sortedByDescending { it.createdAt }
            
            Result.success(uniqueResults)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserPinsByUserId(userId: String): Result<List<UserPin>> {
        return try {
            val snapshot = userPinsCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val userPins = snapshot.documents.mapNotNull { doc ->
                doc.toUserPin()
            }
            
            Result.success(userPins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecentUserPins(limit: Int = 20): Result<List<UserPin>> {
        return try {
            val snapshot = userPinsCollection
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            val userPins = snapshot.documents.mapNotNull { doc ->
                doc.toUserPin()
            }
            
            Result.success(userPins)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteUserPin(pinId: String): Result<Unit> {
        return try {
            // Eliminar de Firestore
            userPinsCollection.document(pinId).delete().await()
            
            // Eliminar imagen de Storage
            // No hay eliminación de imagen de ImgBB en este punto, ya que las URLs son permanentes.
            // Si se necesita eliminar la imagen original, se necesitaría el ID de la imagen en ImgBB.
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteAllUserPins(): Result<Unit> {
        return try {
            Log.d("UserPinsDataSource", "🗑️ Iniciando borrado de todos los UserPins de Firestore")
            
            // Obtener todos los documentos
            val snapshot = userPinsCollection.get().await()
            Log.d("UserPinsDataSource", "📊 Documentos encontrados en Firestore: ${snapshot.documents.size}")
            
            // Borrar todos los documentos en batch
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            
            // Ejecutar el batch
            batch.commit().await()
            Log.d("UserPinsDataSource", "✅ Todos los UserPins borrados de Firestore")
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserPinsDataSource", "❌ Error borrando todos los UserPins: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    //Convierte un documento de Firestore a UserPin
    private fun com.google.firebase.firestore.DocumentSnapshot.toUserPin(): UserPin? {
        return try {
            UserPin(
                id = getString("id") ?: return null,
                description = getString("description"),
                urls = UserPinUrls(
                    small = getString("smallUrl") ?: return null,
                    regular = getString("regularUrl") ?: return null,
                    full = getString("fullUrl") ?: return null
                ),
                userId = getString("userId") ?: return null,
                userName = getString("userName"),
                userPhotoUrl = getString("userPhotoUrl"),
                hashtags = (get("hashtags") as? List<String>) ?: emptyList(),
                keywords = (get("keywords") as? List<String>) ?: emptyList(),
                createdAt = getLong("createdAt") ?: System.currentTimeMillis(),
                isUserPin = true
            )
        } catch (e: Exception) {
            null
        }
    }
} 