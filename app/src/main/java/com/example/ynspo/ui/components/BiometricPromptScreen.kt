package com.example.ynspo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.ynspo.security.BiometricAuthManager
import com.example.ynspo.ui.theme.Dimens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiometricAuthViewModel @Inject constructor(
    val biometricAuthManager: BiometricAuthManager
) : ViewModel()

@Composable
fun BiometricPromptScreen(
    onAuthenticationSuccess: () -> Unit,
    onAuthenticationError: (String) -> Unit,
    onCancel: () -> Unit,
    viewModel: BiometricAuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showError by remember { mutableStateOf<String?>(null) }
    var hasStartedAuth by remember { mutableStateOf(false) }

    // Iniciar autenticación automáticamente cuando se carga la pantalla
    LaunchedEffect(Unit) {
        if (!hasStartedAuth) {
            hasStartedAuth = true
            viewModel.biometricAuthManager.authenticate(
                context = context,
                onSuccess = onAuthenticationSuccess,
                onError = { error ->
                    showError = error
                    onAuthenticationError(error)
                },
                onFail = {
                    showError = "Autenticación fallida. Inténtalo de nuevo."
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Autenticación",
            modifier = Modifier.size(Dimens.BiometricIconSize),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingXXL))
        
        Text(
            text = "Autenticación requerida",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingL))
        
        Text(
            text = "Usa tu huella dactilar, reconocimiento facial o PIN/patrón del dispositivo para continuar",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        
        showError?.let { error ->
            Spacer(modifier = Modifier.height(Dimens.PaddingL))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(Dimens.PaddingM),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.PaddingXXL))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
        ) {
            OutlinedButton(
                onClick = {
                    hasStartedAuth = false
                    showError = null
                    viewModel.biometricAuthManager.authenticate(
                        context = context,
                        onSuccess = onAuthenticationSuccess,
                        onError = { error ->
                            showError = error
                            onAuthenticationError(error)
                        },
                        onFail = {
                            showError = "Autenticación fallida. Inténtalo de nuevo."
                        }
                    )
                }
            ) {
                Text("Reintentar")
            }
            
            OutlinedButton(
                onClick = onCancel
            ) {
                Text("Cancelar")
            }
        }
    }
}
