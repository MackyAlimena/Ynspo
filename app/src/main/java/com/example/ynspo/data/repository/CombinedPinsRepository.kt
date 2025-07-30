package com.example.ynspo.data.repository

import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.model.toInspirationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CombinedPinsRepository @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    private val userPinsRepository: UserPinsRepository
) {
    
    suspend fun getCombinedPinsForYou(): List<InspirationItem> = withContext(Dispatchers.IO) {
        try {
            // Obtener pines de Unsplash con una query aleatoria
            val unsplashQuery = listOf("crafts", "diy", "handmade", "art", "creative", "decoration").random()
            val unsplashResponse = unsplashRepository.searchPhotos(unsplashQuery, 1, 15)
            val unsplashPins = unsplashResponse.results.map { it.toInspirationItem() }
            
            android.util.Log.d("CombinedPinsRepository", "Unsplash pins obtenidos: ${unsplashPins.size}")
            
            // Obtener pines de usuario recientes
            val userPinsResult = userPinsRepository.getRecentUserPins(10)
            val userPins = if (userPinsResult.isSuccess) {
                val pins = userPinsResult.getOrThrow().map { it.toInspirationItem() }
                android.util.Log.d("CombinedPinsRepository", "User pins obtenidos: ${pins.size}")
                pins.forEach { pin ->
                    android.util.Log.d("CombinedPinsRepository", "User pin: ${pin.id}, URLs: ${pin.urls.small}")
                }
                pins
            } else {
                android.util.Log.e("CombinedPinsRepository", "Error obteniendo user pins: ${userPinsResult.exceptionOrNull()?.message}")
                emptyList()
            }
            
            // Combinar y mezclar ambos tipos
            val combinedPins = unsplashPins + userPins
            val shuffledPins = combinedPins.shuffled() // Mezclar para variedad
            
            android.util.Log.d("CombinedPinsRepository", "Total pins combinados: ${shuffledPins.size}")
            shuffledPins
        } catch (e: Exception) {
            android.util.Log.e("CombinedPinsRepository", "Error en getCombinedPinsForYou: ${e.message}", e)
            // En caso de error muestra solo pines de Unsplash
            try {
                val unsplashResponse = unsplashRepository.searchPhotos("crafts", 1, 20)
                unsplashResponse.results.map { it.toInspirationItem() }
            } catch (e2: Exception) {
                android.util.Log.e("CombinedPinsRepository", "Error fallback: ${e2.message}", e2)
                emptyList()
            }
        }
    }
    
    suspend fun searchCombinedPins(query: String): List<InspirationItem> = withContext(Dispatchers.IO) {
        try {
            // Buscar en Unsplash
            val unsplashResponse = unsplashRepository.searchPhotos(query, 1, 15)
            val unsplashPins = unsplashResponse.results.map { it.toInspirationItem() }
            
            // Buscar en pines de usuario
            val userPinsResult = userPinsRepository.searchUserPins(query)
            val userPins = if (userPinsResult.isSuccess) {
                userPinsResult.getOrThrow().map { it.toInspirationItem() }
            } else {
                emptyList()
            }
            
            // Combinar y ordenar por relevancia (pines de usuario primero)
            val combinedPins = userPins + unsplashPins
            combinedPins
        } catch (e: Exception) {
            // En caso de error muestra solo pines de Unsplash
            try {
                val unsplashResponse = unsplashRepository.searchPhotos(query, 1, 20)
                unsplashResponse.results.map { it.toInspirationItem() }
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }
    
    suspend fun getCombinedPins(limit: Int = 20): List<InspirationItem> = withContext(Dispatchers.IO) {
        try {
            // Obtener pines de Unsplash con límite específico
            val unsplashResponse = unsplashRepository.searchPhotos("crafts", 1, limit / 2)
            val unsplashPins = unsplashResponse.results.map { it.toInspirationItem() }
            
            // Obtener pines de usuario
            val userPinsResult = userPinsRepository.getRecentUserPins(limit / 2)
            val userPins = if (userPinsResult.isSuccess) {
                userPinsResult.getOrThrow().map { it.toInspirationItem() }
            } else {
                emptyList()
            }
            
            // Combinar y mezclar
            val combinedPins = unsplashPins + userPins
            combinedPins.shuffled().take(limit)
        } catch (e: Exception) {
            emptyList()
        }
    }
} 