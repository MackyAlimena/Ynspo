package com.example.ynspo.notification

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val viewModel = hiltViewModel<ScheduleNotificationViewModel>()
    
    var customTitle by remember { mutableStateOf("") }
    var customMessage by remember { mutableStateOf("") }
    var hourOfDay by remember { mutableStateOf("8") }
    var minuteValue by remember { mutableStateOf("0") }
    
    // Estados para los popups
    var showInstantSuccess by remember { mutableStateOf(false) }
    var showDailySuccess by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    // Popup de éxito para notificación instantánea
    if (showInstantSuccess) {
        SuccessDialog(
            title = "¡Notificación programada!",
            message = "Tu notificación será enviada en 3 segundos",
            onDismiss = { 
                showInstantSuccess = false
                // Limpiar campos
                customTitle = ""
                customMessage = ""
            }
        )
    }

    // Popup de éxito para notificación diaria
    if (showDailySuccess) {
        SuccessDialog(
            title = "¡Recordatorio programado!",
            message = "Tu recordatorio diario se activará a las ${hourOfDay.padStart(2, '0')}:${minuteValue.padStart(2, '0')}",
            onDismiss = { 
                showDailySuccess = false
                // Limpiar campos
                hourOfDay = "8"
                minuteValue = "0"
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuración de notificaciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = Dimens.PaddingL)
        )
        
        if (!postNotificationPermission.status.isGranted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingM),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(Dimens.PaddingM)) {
                    Text(
                        text = "⚠️ Permiso requerido",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Se necesita permiso para enviar notificaciones",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Button(
                        onClick = { postNotificationPermission.launchPermissionRequest() },
                        modifier = Modifier.padding(top = Dimens.PaddingM),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Solicitar permiso", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        // Notificación inmediata
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingM),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = "🔔 Notificación instantánea", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = Dimens.PaddingM)
                )
                
                OutlinedTextField(
                    value = customTitle,
                    onValueChange = { customTitle = it },
                    label = { 
                        Text(
                            "Título personalizado (opcional)", 
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ) 
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PaddingS)
                )
                
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { 
                        Text(
                            "Mensaje personalizado (opcional)", 
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ) 
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PaddingS)
                )
                
                Button(
                    onClick = {
                        viewModel.scheduleNotification(
                            title = if (customTitle.isNotEmpty()) customTitle else "Inspiración creativa",
                            message = if (customMessage.isNotEmpty()) customMessage else "¡No dejes que se te escape la creatividad! Revisa tus tableros en Ynspo."
                        )
                        showInstantSuccess = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PaddingM)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Programar notificación en 3 segundos",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        // Notificación diaria
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingM),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = "⏰ Recordatorio diario", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = Dimens.PaddingM)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PaddingS),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Hora:",
                        modifier = Modifier.padding(end = Dimens.PaddingM),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    OutlinedTextField(
                        value = hourOfDay,
                        onValueChange = { if (it.isEmpty() || (it.toIntOrNull() != null && it.toInt() in 0..23)) hourOfDay = it },
                        label = { 
                            Text(
                                "Hora", 
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            ) 
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        ":",
                        modifier = Modifier.padding(horizontal = Dimens.PaddingS),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    
                    OutlinedTextField(
                        value = minuteValue,
                        onValueChange = { if (it.isEmpty() || (it.toIntOrNull() != null && it.toInt() in 0..59)) minuteValue = it },
                        label = { 
                            Text(
                                "Min", 
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            ) 
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Button(
                    onClick = {
                        val hour = hourOfDay.toIntOrNull() ?: 8
                        val minute = minuteValue.toIntOrNull() ?: 0
                        viewModel.scheduleDaily(
                            hourOfDay = hour,
                            minute = minute
                        )
                        showDailySuccess = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PaddingM)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Programar recordatorio diario",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }
    
    // Auto-cerrar después de 3 segundos
    LaunchedEffect(showDialog) {
        if (showDialog) {
            delay(3000)
            showDialog = false
            onDismiss()
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false; onDismiss() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ícono de éxito
                    Text(
                        text = "✅",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Button(
                        onClick = { showDialog = false; onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Entendido", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}