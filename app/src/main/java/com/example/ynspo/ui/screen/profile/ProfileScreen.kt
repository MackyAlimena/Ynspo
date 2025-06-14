package com.example.ynspo.ui.screen.profile

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.profile.UserProfile
import com.example.ynspo.ui.test.DatabaseTestActivity
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens.PaddingL
import com.example.ynspo.ui.theme.Dimens.PaddingM
import com.example.ynspo.ui.theme.Dimens.PaddingS
import com.example.ynspo.ui.theme.Dimens.PaddingXXS
import com.example.ynspo.ui.theme.Dimens.ProfileImageSize
import com.example.ynspo.ui.theme.SelectedColor
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val userProfile = viewModel.userProfile.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally    ) {
        userProfile.value?.let { profile ->
            Image(
                painter = rememberAsyncImagePainter(profile.photoUrl),
                contentDescription = "Profile image", // Usando texto directo en lugar de referencia a recurso
                modifier = Modifier
                    .size(ProfileImageSize)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(PaddingM))

            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall,
                color = DetailColor,
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(PaddingS))        Text(
            text = profile.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = DetailColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(PaddingL))

        // Statistics row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat("Pins", 240)
            ProfileStat("Siguiendo", 183)
            ProfileStat("Seguidores", 1352)
        }

        Spacer(modifier = Modifier.height(PaddingL))

        Text(
            text = "My Hobbies", // Usando texto directo en lugar de referencia a recurso
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.SemiBold
        )        
        
        Spacer(modifier = Modifier.height(PaddingS))

        profile.hobbies.forEach { hobby ->
            Text(
                text = "• $hobby",
                style = MaterialTheme.typography.bodyLarge,
                color = SelectedColor,
                modifier = Modifier.padding(vertical = PaddingXXS)
            )
        }
        
        Spacer(modifier = Modifier.height(PaddingL))
        
        // Botón para acceder a la pantalla de prueba de la base de datos
        OutlinedButton(
            onClick = {
                val intent = Intent(context, DatabaseTestActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
        ) {
            Text(
                text = "Test Database", 
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProfileStat(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = DetailColor
        )
    }
}
