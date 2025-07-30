package com.example.ynspo.ui.screen.pins

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ynspo.R
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.toInspirationItem
import com.example.ynspo.notification.ScheduleNotificationViewModel
import com.example.ynspo.ui.components.dialog.SaveToBoardDialog
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.theme.Dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinDetailScreen(
    photo: UnsplashPhoto,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    notificationViewModel: ScheduleNotificationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    // Convertir UnsplashPhoto a InspirationItem para usar la función genérica
    val inspirationItem = photo.toInspirationItem()
    PinDetailScreen(inspirationItem, navController, boardsViewModel, notificationViewModel, onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinDetailScreen(
    inspirationItem: InspirationItem,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    notificationViewModel: ScheduleNotificationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    var showSaveToBoardDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    
    // Función para compartir la imagen
    val sharePhoto = {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¡Mira esta inspiración! ${inspirationItem.urls.regular}")
            putExtra(Intent.EXTRA_SUBJECT, "Inspiración desde Ynspo")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir inspiración"))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen principal con overlay para botones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.PinDetailMinHeight, max = Dimens.PinDetailMaxHeight)
            ) {
                // Imagen principal
                AsyncImage(
                    model = inspirationItem.urls.regular,
                    contentDescription = inspirationItem.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = Dimens.CornerRadiusXL, bottomEnd = Dimens.CornerRadiusXL))
                )
                
                // Gradient overlay más oscuro y dramático
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.05f),
                                    Color.Black.copy(alpha = 0.15f),
                                    Color.Black.copy(alpha = 0.3f)
                                ),
                                startY = 100f,
                                endY = 600f
                            ),
                            shape = RoundedCornerShape(bottomStart = Dimens.CornerRadiusXL, bottomEnd = Dimens.CornerRadiusXL)
                        )
                )
                
                // Back button
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Dimens.PaddingL)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // Contenido principal
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                // Descripción del pin
                PinDescriptionSection(inspirationItem = inspirationItem)
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                // Botones de acción modernos
                ModernActionButtons(
                    onFavoriteClick = { showSaveToBoardDialog = true },
                    onShareClick = sharePhoto,
                    onReminderClick = { showReminderDialog = true }
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                // Sección de recordatorio mejorada
                EnhancedReminderSection(
                    onReminderClick = { showReminderDialog = true }
                )
                
                // Spacer para scroll bottom
                Spacer(modifier = Modifier.height(Dimens.BottomBarPadding))
            }
        }
    }
    
    // Save to board dialog
    if (showSaveToBoardDialog) {
        SaveToBoardDialog(
            inspirationItem = inspirationItem,
            boardsViewModel = boardsViewModel,
            onDismiss = { showSaveToBoardDialog = false }
        )
    }
    
    // Reminder dialog
    if (showReminderDialog) {
        ModernReminderDialog(
            inspirationItem = inspirationItem,
            notificationViewModel = notificationViewModel,
            onDismiss = { showReminderDialog = false }
        )
    }
}

@Composable
fun PinDescriptionSection(
    photo: UnsplashPhoto,
    modifier: Modifier = Modifier
) {
    // Convertir UnsplashPhoto a InspirationItem para usar la función genérica
    val inspirationItem = photo.toInspirationItem()
    PinDescriptionSection(inspirationItem, modifier)
}

@Composable
fun PinDescriptionSection(
    inspirationItem: InspirationItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusM)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingL)
        ) {
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Dimens.PaddingS)
                )
            
            Text(
                text = inspirationItem.description ?: "Sin descripción disponible",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun ModernActionButtons(
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
    ) {
        // Botón guardar en board
        ElevatedButton(
            onClick = onFavoriteClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = RoundedCornerShape(Dimens.CornerRadiusM)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.padding(end = Dimens.PaddingS)
            )
            Text(
                "Guardar",
                fontWeight = FontWeight.Medium
            )
        }
        
        // Botón compartir
        OutlinedButton(
            onClick = onShareClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(Dimens.CornerRadiusM),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.padding(end = Dimens.PaddingS)
            )
            Text(
                "Compartir",
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EnhancedReminderSection(
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusL)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono decorativo
            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeL)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.IconSizeM)
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens.PaddingM))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "¿Recordatorio?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Text(
                    text = "Te enviaremos una notificación mañana para que no olvides esta inspiración",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens.PaddingM))
            
            // Botón de acción
            FilledTonalButton(
                onClick = onReminderClick,
                shape = RoundedCornerShape(Dimens.CornerRadiusS),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    "Activar",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ModernReminderDialog(
    photo: UnsplashPhoto,
    notificationViewModel: ScheduleNotificationViewModel,
    onDismiss: () -> Unit
) {
    // Convertir UnsplashPhoto a InspirationItem para usar la función genérica
    val inspirationItem = photo.toInspirationItem()
    ModernReminderDialog(inspirationItem, notificationViewModel, onDismiss)
}

@Composable
fun ModernReminderDialog(
    inspirationItem: InspirationItem,
    notificationViewModel: ScheduleNotificationViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.IconSizeM)
            )
        },
        title = {
            Text(
                text = "Configurar Recordatorio",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                Text(
                    text = "¿Cuándo te gustaría que te recordemos esta inspiración?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingM))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(Dimens.CornerRadiusS)
                ) {
                    Text(
                        text = "💡 Consejo: Los recordatorios te ayudan a no olvidar proyectos creativos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(Dimens.PaddingM)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    notificationViewModel.scheduleNotification(
                        delayInSeconds = 86400, // 24 horas
                        title = "Recordatorio de inspiración",
                        message = "¡No olvides revisar tu inspiración!"
                    )
                    onDismiss()
                }
            ) {
                Text(
                    "Mañana",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancelar",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        shape = RoundedCornerShape(Dimens.CornerRadiusXL)
    )
}
