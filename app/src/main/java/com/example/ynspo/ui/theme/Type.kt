package com.example.ynspo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.Font as GoogleFontType

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val poppinsFont = GoogleFont("Poppins")

val YnspoFontFamily = FontFamily(
    GoogleFontType(googleFont = poppinsFont, fontProvider = fontProvider)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = YnspoFontFamily,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = YnspoFontFamily,
        fontSize = 24.sp
    )

)
