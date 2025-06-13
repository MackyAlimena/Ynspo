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
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(

    primary = DetailColor,
    onPrimary = Color.White,
    primaryContainer = DetailColor,

    secondary = SelectedColor,
    onSecondary = Color.White,
    secondaryContainer = SelectedColor,

    background = BackgroundColor,
    surface = BackgroundColor,
    onBackground = DetailColor,
    onSurface = DetailColor

    /*primary = AntiqueRuby80,
    onPrimary = Color.White,
    primaryContainer = AntiqueRuby40,

    secondary = ParrotPink80,
    onSecondary = Color.White,
    secondaryContainer = ParrotPink40,

    background = QueenPink80,
    surface = QueenPink80,
    onBackground = FrenchPuce80,
    onSurface = FrenchPuce80
            */

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
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
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

