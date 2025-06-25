package com.example.ynspo.ui.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.repository.UserProfileRepository
import com.example.ynspo.ui.profile.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : ViewModel() {

    // Obtenemos el perfil de usuario desde el repositorio con Room
    val userProfile: LiveData<UserProfile?> = repository.userProfile    // Creamos un perfil por defecto en caso de que no exista
    init {
        // Observar el LiveData y crear un perfil por defecto si es necesario
        viewModelScope.launch {
            // Esperar un poco para dar tiempo a que la base de datos se inicialice
            kotlinx.coroutines.delay(500)
            
            // Comprobar si hay un perfil existente
            val currentProfile = userProfile.value
            if (currentProfile == null) {
                saveProfile(
                    UserProfile(
                        name = "Juan Cherry Blossom",
                        photoUrl = "https://i.pravatar.cc/150?img=3",
                        bio = "Lover of crafts, nature & code :computer::herb:",
                        hobbies = listOf("Painting", "Knitting", "Embroidery", "Photography")
                    )
                )
            }
        }
    }

    // Función para guardar/actualizar el perfil
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    // Función para actualizar un hobby específico
    fun updateHobbies(newHobbies: List<String>) {
        viewModelScope.launch {
            userProfile.value?.let { currentProfile ->
                val updatedProfile = currentProfile.copy(hobbies = newHobbies)
                repository.saveUserProfile(updatedProfile)
            }
        }
    }
}
