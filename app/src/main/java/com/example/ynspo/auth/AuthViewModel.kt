package com.example.ynspo.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.ynspo.security.BiometricAuthManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userData = MutableStateFlow(auth.currentUser)
    val userData = _userData.asStateFlow()

    fun authenticate(context: Context) {
        biometricAuthManager.authenticate(
            context,
            onError = { errorMessage ->
                _isAuthenticated.value = false
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            },
            onSuccess = {
                _isAuthenticated.value = true
                Toast.makeText(context, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
            },
            onFail = {
                _isAuthenticated.value = false
                Toast.makeText(context, "Autenticación fallida, intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        )
    }
}