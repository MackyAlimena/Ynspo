package com.example.ynspo.ui.screen.pins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.notification.ScheduleNotificationViewModel
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.components.dialog.SaveToBoardDialog
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor

@Composable
fun PinDetailScreen(
    photo: UnsplashPhoto,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    notificationViewModel: ScheduleNotificationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    var showSaveToBoardDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Display the pin image
        PinImage(photo = photo)
        
        // Display pin actions (favorite, share)
        PinActions(
            onFavoriteClick = { showSaveToBoardDialog = true },
            onShareClick = { /* Share functionality */ }
        )
        
        // Display pin description
        PinDescription(photo = photo)
        
        // Añadir opción de recordatorio
        ReminderSection(
            onReminderClick = { showReminderDialog = true },
            modifier = Modifier.padding(horizontal = Dimens.PaddingL)
        )
    }
    
    // Save to board dialog
    if (showSaveToBoardDialog) {
        SaveToBoardDialog(
            photo = photo,
            boardsViewModel = boardsViewModel,
            onDismiss = { showSaveToBoardDialog = false }
        )
    }
    
    // Reminder dialog
    if (showReminderDialog) {
        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("Programar recordatorio") },
            text = { 
                Text("¿Quieres recibir una notificación mañana para inspirarte con este diseño?") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Programar recordatorio para mañana
                        notificationViewModel.scheduleNotification(
                            delayInSeconds = 24 * 60 * 60, // 24 horas
                            title = "Inspiración creativa",
                            message = "Recuerda revisar el pin '${photo.description ?: ""}' que guardaste ayer"
                        )
                        showReminderDialog = false
                    }
                ) {
                    Text("Sí, recordármelo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReminderDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ReminderSection(
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = SelectedColor.copy(alpha = 0.2f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingL)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "¿Quieres un recordatorio?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DetailColor
                )
                
                Text(
                    text = "Te notificaremos mañana para que te inspire este diseño",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DetailColor.copy(alpha = 0.8f)
                )
            }
            
            IconButton(
                onClick = onReminderClick,
                modifier = Modifier
                    .padding(start = Dimens.PaddingM)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Programar recordatorio",
                    tint = DetailColor
                )
            }
        }
    }
}
