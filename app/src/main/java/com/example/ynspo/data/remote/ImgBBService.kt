package com.example.ynspo.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Singleton

@Singleton
class ImgBBService constructor(
    private val context: Context
) {
    
    private val client = OkHttpClient()
    private val IMGBB_API_KEY = "d21c0c835ed85fe753cad75891126fba"
    
    // Formatos soportados por ImgBB
    private val supportedFormats = listOf("image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp")
    
    suspend fun uploadImage(imageUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d("ImgBBService", "Iniciando upload a ImgBB...")
            Log.d("ImgBBService", "ImageUri: $imageUri")
            
            // Detectar el tipo MIME de la imagen
            val originalMimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
            Log.d("ImgBBService", "MIME type original: $originalMimeType")
            
            // Convertir Uri a File
            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                Log.e("ImgBBService", "Error: No se pudo abrir el inputStream del Uri")
                return@withContext Result.failure(Exception("No se pudo abrir la imagen"))
            }
            
            val file: File
            val mimeType: String
            
            // Si el formato no es soportado, convertir a JPEG
            if (!supportedFormats.contains(originalMimeType)) {
                Log.d("ImgBBService", "Formato no soportado, convirtiendo a JPEG...")
                file = convertToJpeg(inputStream)
                mimeType = "image/jpeg"
            } else {
                // Usar formato original
                file = File(context.cacheDir, "temp_image.jpg")
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                mimeType = originalMimeType
            }
            
            Log.d("ImgBBService", "Archivo temporal creado: ${file.absolutePath}")
            Log.d("ImgBBService", "Tamaño del archivo: ${file.length()} bytes")
            Log.d("ImgBBService", "MIME type final: $mimeType")
            
            if (!file.exists() || file.length() == 0L) {
                Log.e("ImgBBService", "Error: El archivo temporal no existe o está vacío")
                return@withContext Result.failure(Exception("Archivo temporal inválido"))
            }
            
            // Crear request multipart
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", IMGBB_API_KEY)
                .addFormDataPart("image", file.name, file.asRequestBody(mimeType.toMediaType()))
                .build()
            
            val request = Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build()
            
            Log.d("ImgBBService", "Enviando request a ImgBB...")
            Log.d("ImgBBService", "API Key: $IMGBB_API_KEY")
            Log.d("ImgBBService", "MIME type enviado: $mimeType")
            
            val response = client.newCall(request).execute()
            
            Log.d("ImgBBService", "Respuesta recibida: ${response.code} - ${response.message}")
            
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("ImgBBService", "Respuesta de ImgBB: $responseBody")
                
                // Parsear respuesta JSON (simplificado)
                val imageUrl = extractImageUrl(responseBody)
                if (imageUrl != null) {
                    Log.d("ImgBBService", "URL de imagen obtenida: $imageUrl")
                    Result.success(imageUrl)
                } else {
                    Log.e("ImgBBService", "No se pudo extraer URL de la respuesta")
                    Log.e("ImgBBService", "Respuesta completa: $responseBody")
                    Result.failure(Exception("No se pudo extraer URL de imagen"))
                }
            } else {
                val errorBody = response.body?.string()
                Log.e("ImgBBService", "Error en respuesta: ${response.code} - ${response.message}")
                Log.e("ImgBBService", "Error body: $errorBody")
                Result.failure(Exception("Error al subir imagen: ${response.code}"))
            }
            
        } catch (e: Exception) {
            Log.e("ImgBBService", "Error en upload: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    private fun convertToJpeg(inputStream: java.io.InputStream): File {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val file = File(context.cacheDir, "temp_image.jpg")
        
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }
        
        Log.d("ImgBBService", "Imagen convertida a JPEG exitosamente")
        return file
    }
    
    private fun extractImageUrl(responseBody: String?): String? {
        // Parseo simple de JSON para extraer la URL
        return try {
            val urlPattern = "\"url\":\"([^\"]+)\"".toRegex()
            val match = urlPattern.find(responseBody ?: "")
            val url = match?.groupValues?.get(1)
            
            // Limpiar caracteres de escape de la URL
            url?.replace("\\/", "/")
        } catch (e: Exception) {
            Log.e("ImgBBService", "Error parseando respuesta: ${e.message}")
            null
        }
    }
} 