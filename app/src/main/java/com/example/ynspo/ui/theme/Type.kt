package com.example.ynspo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.ynspo.R
import androidx.compose.ui.text.googlefonts.Font as GoogleFontType

/**
 * Font configuration for the app
 */
val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@Composable
fun getPoppinsFont(): GoogleFont {
    return GoogleFont(stringResource(id = R.string.font_family_poppins))
}

@Composable
fun getYnspoFontFamily(): FontFamily {
    val poppinsFont = getPoppinsFont()
    return FontFamily(
        GoogleFontType(googleFont = poppinsFont, fontProvider = fontProvider)
    )
}

@Composable
fun getTypography(): Typography {
    val ynspoFontFamily = getYnspoFontFamily()
    
    return Typography(
        // Display styles
        displayLarge = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 28.sp
        ),
        
        // Headline styles
        headlineLarge = TextStyle(
            fontFamily = ynspoFontFamily, 
            fontSize = 24.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 22.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 20.sp
        ),
        
        // Title styles
        titleLarge = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 18.sp
        ),
        titleMedium = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 16.sp
        ),
        
        // Body styles
        bodyLarge = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 14.sp
        ),
        
        // Label styles
        labelLarge = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = ynspoFontFamily,
            fontSize = 10.sp
        )
    )
}
