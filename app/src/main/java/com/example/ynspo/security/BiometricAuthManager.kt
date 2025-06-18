package com.example.ynspo.security

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class BiometricAuthManager @Inject constructor() {

    fun canAuthenticate(context: Context): Int {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }

    fun authenticate(context: Context, onError: (String) -> Unit, onSuccess: () -> Unit, onFail: () -> Unit) {
        // Verificar si la autenticación biométrica está disponible
        val canAuthenticate = canAuthenticate(context)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Continuar con la autenticación
                startBiometricAuth(context, onError, onSuccess, onFail)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onError("No biometric hardware available")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onError("Biometric hardware is unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError("No biometric credentials enrolled")
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                onError("Security update required")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                onError("Biometric authentication not supported")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                onError("Biometric status unknown")
            }
            else -> {
                onError("Biometric authentication not available")
            }
        }
    }

    private fun startBiometricAuth(context: Context, onError: (String) -> Unit, onSuccess: () -> Unit, onFail: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val errorMessage = when (errorCode) {
                        BiometricPrompt.ERROR_HW_NOT_PRESENT -> "No biometric hardware available"
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> "Biometric hardware is unavailable"
                        BiometricPrompt.ERROR_LOCKOUT -> "Too many failed attempts. Try again later"
                        BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> "Too many failed attempts. Biometric authentication is permanently disabled"
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> "Authentication cancelled"
                        BiometricPrompt.ERROR_NO_BIOMETRICS -> "No biometric credentials enrolled"
                        BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> "No device credentials available"
                        BiometricPrompt.ERROR_NO_SPACE -> "Insufficient storage"
                        BiometricPrompt.ERROR_TIMEOUT -> "Authentication timeout"
                        BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> "Unable to process authentication"
                        BiometricPrompt.ERROR_USER_CANCELED -> "Authentication cancelled by user"
                        BiometricPrompt.ERROR_VENDOR -> "Vendor-specific error"
                        else -> "Authentication error: $errString"
                    }
                    onError(errorMessage)
                }

                @RequiresApi(Build.VERSION_CODES.R)
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFail()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .setTitle("Autenticación Biométrica")
            .setSubtitle("Usa tu huella digital o reconocimiento facial")
            .setDescription("Para acceder a tus boards, debes autenticarte")
            .setConfirmationRequired(false)
            .build()
        
        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            onError("Error starting biometric authentication: ${e.message}")
        }
    }
}