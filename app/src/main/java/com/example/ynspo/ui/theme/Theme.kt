package com.example.ynspo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    // Colores principales
    primary = DarkPrimary,
    onPrimary = Color.White,
    primaryContainer = DarkPrimaryVariant,
    onPrimaryContainer = Color.White,
    
    // Colores secundarios  
    secondary = DarkSecondary,
    onSecondary = Color.Black,
    secondaryContainer = DarkSelection,
    onSecondaryContainer = DarkOnSurface,
    
    // Fondos y superficies
    background = DarkBackgroundColor,
    onBackground = DarkOnBackground,
    surface = DarkSurfaceColor,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    
    // Colores de contorno y división
    outline = DarkDivider,
    outlineVariant = DarkSelection,
    
    // Colores de estado
    error = DarkError,
    onError = Color.White,
    errorContainer = DarkError,
    onErrorContainer = Color.White,
    
    // Otros colores del sistema
    surfaceTint = DarkPrimary,
    inverseSurface = DarkOnBackground,
    inverseOnSurface = DarkBackgroundColor,
    inversePrimary = DarkPrimaryVariant
)

private val LightColorScheme = lightColorScheme(
    // Colores principales
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryVariant,
    onPrimaryContainer = Color.White,
    
    // Colores secundarios
    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = LightSelection,
    onSecondaryContainer = LightOnSurface,
    
    // Fondos y superficies
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    
    // Colores de contorno y división
    outline = LightOutline,
    outlineVariant = LightSelection,
    
    // Colores de estado
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    // Otros colores del sistema
    surfaceTint = LightPrimary,
    inverseSurface = LightOnSurface,
    inverseOnSurface = LightSurface,
    inversePrimary = LightPrimaryVariant
)

@Composable
fun YnspoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit)
{
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = content
    )
}

