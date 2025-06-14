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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

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

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Configuración de notificaciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DetailColor,
            modifier = Modifier.padding(bottom = Dimens.PaddingL)
        )
        
        if (!postNotificationPermission.status.isGranted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingM),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3CD)
                )
            ) {
                Column(modifier = Modifier.padding(Dimens.PaddingM)) {
                    Text(
                        text = "Permiso requerido",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                    Text(
                        text = "Se necesita permiso para enviar notificaciones",
                        color = Color(0xFF856404)
                    )
                    Button(
                        onClick = { postNotificationPermission.launchPermissionRequest() },
                        modifier = Modifier.padding(top = Dimens.PaddingM),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC107)
                        )
                    ) {
                        Text("Solicitar permiso")
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
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = "Notificación instantánea", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = Dimens.PaddingM)
                )
                
                OutlinedTextField(
                    value = customTitle,
                    onValueChange = { customTitle = it },
                    label = { Text("Título personalizado (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PaddingS)
                )
                
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { Text("Mensaje personalizado (opcional)") },
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PaddingM)
                ) {
                    Text("Programar notificación en 3 segundos")
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        // Notificación diaria
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingM),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = "Recordatorio diario", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = Dimens.PaddingM)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.PaddingS),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hora:", modifier = Modifier.padding(end = Dimens.PaddingM))
                    
                    OutlinedTextField(
                        value = hourOfDay,
                        onValueChange = { if (it.isEmpty() || (it.toIntOrNull() != null && it.toInt() in 0..23)) hourOfDay = it },
                        label = { Text("Hora") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(":", modifier = Modifier.padding(horizontal = Dimens.PaddingS))
                    
                    OutlinedTextField(
                        value = minuteValue,
                        onValueChange = { if (it.isEmpty() || (it.toIntOrNull() != null && it.toInt() in 0..59)) minuteValue = it },
                        label = { Text("Minuto") },
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PaddingM)
                ) {
                    Text("Programar recordatorio diario")
                }
            }
        }
    }
}