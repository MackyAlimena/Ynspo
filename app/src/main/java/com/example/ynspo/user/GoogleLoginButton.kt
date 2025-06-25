package com.example.ynspo.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.ynspo.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.CornerRadiusS),
        border = BorderStroke(Dimens.StrokeWidthThin, MaterialTheme.colorScheme.outline),
        contentPadding = PaddingValues(horizontal = Dimens.PaddingL, vertical = Dimens.PaddingL)
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(Dimens.PaddingS))
        Text(
            text = "Iniciar sesión con Google",
            fontWeight = FontWeight.Medium
        )
    }
}
