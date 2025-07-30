package com.example.ynspo.ui.screen.upload

import android.net.Uri
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.R
import com.example.ynspo.data.repository.UserPinsRepository
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.toInspirationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadPinState(
    val imageUri: Uri? = null,
    val description: String = "",
    val hashtags: String = "",
    val keywords: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val uploadedPin: InspirationItem? = null
)

@HiltViewModel
class UploadPinViewModel @Inject constructor(
    private val userPinsRepository: UserPinsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(UploadPinState())
    val state: StateFlow<UploadPinState> = _state.asStateFlow()

    fun setImageUri(uri: Uri?) {
        _state.value = _state.value.copy(imageUri = uri)
    }

    fun setDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun setHashtags(hashtags: String) {
        _state.value = _state.value.copy(hashtags = hashtags)
    }

    fun setKeywords(keywords: String) {
        _state.value = _state.value.copy(keywords = keywords)
    }

    fun uploadPin(
        userId: String,
        userName: String?,
        userPhotoUrl: String?
    ) {
        val currentState = _state.value
        
        Log.d("UploadPinViewModel", "Iniciando upload de pin...")
        
        // Validaciones
        if (currentState.imageUri == null) {
            Log.e("UploadPinViewModel", "Error: No hay imagen seleccionada")
            _state.value = currentState.copy(
                errorMessage = context.getString(R.string.upload_error_no_image)
            )
            return
        }

        if (currentState.description.isBlank()) {
            Log.e("UploadPinViewModel", "Error: No hay descripción")
            _state.value = currentState.copy(
                errorMessage = context.getString(R.string.upload_error_no_description)
            )
            return
        }

        // Limpiar error anterior
        _state.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            isSuccess = false,
            uploadedPin = null
        )

        viewModelScope.launch {
            try {
                Log.d("UploadPinViewModel", "Iniciando proceso de upload...")
                
                // Convertir hashtags y keywords a listas
                val hashtagsList = currentState.hashtags
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                val keywordsList = currentState.keywords
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                Log.d("UploadPinViewModel", "Llamando a userPinsRepository.uploadUserPin...")
                val result = userPinsRepository.uploadUserPin(
                    imageUri = currentState.imageUri!!,
                    description = currentState.description,
                    hashtags = hashtagsList,
                    keywords = keywordsList,
                    userId = userId,
                    userName = userName,
                    userPhotoUrl = userPhotoUrl
                )

                if (result.isSuccess) {
                    val userPin = result.getOrThrow()
                    val inspirationItem = userPin.toInspirationItem()
                    
                    Log.d("UploadPinViewModel", "Upload exitoso! Pin ID: ${userPin.id}")
                    _state.value = currentState.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null,
                        uploadedPin = inspirationItem
                    )
                } else {
                    val error = result.exceptionOrNull()?.message ?: context.getString(R.string.upload_error_unknown)
                    Log.e("UploadPinViewModel", "Error en upload: $error")
                    _state.value = currentState.copy(
                        isLoading = false,
                        errorMessage = error
                    )
                }
            } catch (e: Exception) {
                Log.e("UploadPinViewModel", "Excepción en upload: ${e.message}", e)
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: context.getString(R.string.upload_error_upload)
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun resetState() {
        _state.value = UploadPinState()
    }

    fun clearSuccess() {
        _state.value = _state.value.copy(isSuccess = false, uploadedPin = null)
    }
} 