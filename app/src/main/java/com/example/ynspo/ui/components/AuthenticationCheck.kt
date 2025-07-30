package com.example.ynspo.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.security.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationCheckViewModel @Inject constructor(
    val authManager: AuthManager
) : ViewModel() {
    
    private val _isAuthenticated = MutableStateFlow(authManager.isAuthenticated())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    fun checkAuthentication() {
        viewModelScope.launch {
            _isAuthenticated.value = authManager.isAuthenticated()
        }
    }
    
    fun setAuthenticated() {
        authManager.setAuthenticated()
        _isAuthenticated.value = true
    }
    
    fun clearAuthentication() {
        authManager.clearAuthentication()
        _isAuthenticated.value = false
    }
}

@Composable
fun AuthenticationCheck(
    onAuthenticated: @Composable () -> Unit,
    onNotAuthenticated: @Composable () -> Unit,
    viewModel: AuthenticationCheckViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.checkAuthentication()
    }
    
    if (isAuthenticated) {
        onAuthenticated()
    } else {
        onNotAuthenticated()
    }
}
