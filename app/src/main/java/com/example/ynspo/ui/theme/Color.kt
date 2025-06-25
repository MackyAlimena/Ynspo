package com.example.ynspo.ui.theme

import androidx.compose.ui.graphics.Color

// Light Theme Colors - Mucho mejor contraste
val LightPrimary = Color(0xFF7B4397)           // Lila fuerte y vibrante
val LightPrimaryVariant = Color(0xFF9B59B6)    // Lila más saturado
val LightSecondary = Color(0xFFD5006D)         // Rosa fuerte como acento

val LightBackground = Color(0xFFF8F6FF)        // Fondo muy sutil violeta
val LightSurface = Color(0xFFFFFFFF)           // Blanco puro para cards
val LightSurfaceVariant = Color(0xFFF0EAFF)    // Superficie con tinte violeta

val LightOnBackground = Color(0xFF2D1B47)       // Texto muy oscuro con tinte violeta
val LightOnSurface = Color(0xFF1A0B2E)          // Texto aún más oscuro para mejor contraste
val LightOnSurfaceVariant = Color(0xFF5A4570)   // Texto secundario con buen contraste

val LightOutline = Color(0xFFB8A7D9)           // Bordes suaves pero visibles
val LightSelection = Color(0xFFE8D5F2)         // Selección sutil

// Dark Theme Colors - Dramático y contrastante
val DarkBackgroundColor = Color(0xFF0A0A0F)        // Casi negro puro
val DarkSurfaceColor = Color(0xFF141419)           // Gris muy oscuro para cards
val DarkSurfaceVariant = Color(0xFF1E1E25)         // Para elementos elevados

val DarkPrimary = Color(0xFFFF6B9D)               // Rosa vibrante
val DarkPrimaryVariant = Color(0xFFE91E63)        // Rosa más intenso
val DarkSecondary = Color(0xFFBB86FC)             // Violeta suave como acento secundario

val DarkOnBackground = Color(0xFFF5F5F7)          // Blanco casi puro para máximo contraste
val DarkOnSurface = Color(0xFFE8E8EA)             // Gris muy claro para texto secundario
val DarkOnSurfaceVariant = Color(0xFFB3B3B8)     // Gris medio para texto terciario

val DarkSelection = Color(0xFF2A2A35)             // Para elementos seleccionados
val DarkSelectionLight = Color(0xFF3A3A45)       // Para hover states
val DarkDivider = Color(0xFF2E2E38)               // Para divisores

// Colores de estado
val DarkError = Color(0xFFFF5252)
val DarkWarning = Color(0xFFFFAB00)
val DarkSuccess = Color(0xFF4CAF50)

// Bottom Bar Colors
val BottomBarPurple = Color(0xFF9B6AB7)       // Violeta para light mode
val BottomBarPurpleDark = Color(0xFF0F1419)   // Azul-gris muy oscuro para dark mode

// Compatibilidad con nombres anteriores (DEPRECATED - usar MaterialTheme.colorScheme)
@Deprecated("Use MaterialTheme.colorScheme.primary instead")
val DetailColor = LightPrimary

@Deprecated("Use MaterialTheme.colorScheme.background instead")
val BackgroundColor = LightBackground

@Deprecated("Use MaterialTheme.colorScheme.secondaryContainer instead")
val SelectedColor = LightSelection

