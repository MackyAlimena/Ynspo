package com.example.ynspo.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.db.dao.UserProfileDao
import com.example.ynspo.data.db.entity.UserProfileEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val userProfileDao: UserProfileDao
) : ViewModel() {

    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadThemePreference()
    }

    private fun loadThemePreference() {
        viewModelScope.launch {
            try {
                val userProfile = userProfileDao.getUserProfile()
                _isDarkMode.value = userProfile?.darkModeEnabled ?: false
            } catch (e: Exception) {
                _isDarkMode.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val newDarkMode = !_isDarkMode.value
            _isDarkMode.value = newDarkMode
            
            try {
                val userProfile = userProfileDao.getUserProfile()
                if (userProfile != null) {
                    userProfileDao.updateDarkModePreference(userProfile.id, newDarkMode)
                } else {
                    // Crear perfil básico si no existe
                    val newProfile = UserProfileEntity(
                        name = "Usuario",
                        photoUrl = null,
                        bio = "Bienvenido a Ynspo",
                        darkModeEnabled = newDarkMode
                    )
                    userProfileDao.insertUserProfile(newProfile)
                }
            } catch (e: Exception) {
                // Si falla, revertir el cambio
                _isDarkMode.value = !newDarkMode
            }
        }
    }
} 