package com.example.ynspo.security

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_IS_AUTHENTICATED = "is_authenticated"
        private const val KEY_AUTH_TIMESTAMP = "auth_timestamp"
        private const val AUTH_SESSION_DURATION = 30 * 60 * 1000L // 30 minutos
    }
    
    fun isAuthenticated(): Boolean {
        val isAuth = prefs.getBoolean(KEY_IS_AUTHENTICATED, false)
        val authTimestamp = prefs.getLong(KEY_AUTH_TIMESTAMP, 0L)
        val currentTime = System.currentTimeMillis()
        
        // Verificar si la autenticación no ha expirado
        return isAuth && (currentTime - authTimestamp) < AUTH_SESSION_DURATION
    }
    
    fun setAuthenticated() {
        prefs.edit()
            .putBoolean(KEY_IS_AUTHENTICATED, true)
            .putLong(KEY_AUTH_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }
    
    fun clearAuthentication() {
        prefs.edit()
            .putBoolean(KEY_IS_AUTHENTICATED, false)
            .putLong(KEY_AUTH_TIMESTAMP, 0L)
            .apply()
    }
    
    fun getAuthSessionDuration(): Long = AUTH_SESSION_DURATION
} 