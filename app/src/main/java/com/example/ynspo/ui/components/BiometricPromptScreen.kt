package com.example.ynspo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.auth.AuthStatusViewModel
import com.example.ynspo.auth.AuthViewModel
import com.example.ynspo.ui.theme.BackgroundColor

@Composable
fun BiometricPromptScreen(
    onSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    authStatusViewModel: AuthStatusViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
    
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            authStatusViewModel.setAuthenticated(true)
            onSuccess()
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Autenticación Biométrica Requerida",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Para acceder a tus boards, debes autenticarte usando tu huella digital o reconocimiento facial.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
              Button(
                onClick = { authViewModel.authenticate(context) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Autenticarse")
            }
        }
    }
}
