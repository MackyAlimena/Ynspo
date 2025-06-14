package com.example.ynspo.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
@Singleton
class AuthStatusViewModel @Inject constructor() : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun setAuthenticated(value: Boolean) {
        _isAuthenticated.value = value
    }
}
