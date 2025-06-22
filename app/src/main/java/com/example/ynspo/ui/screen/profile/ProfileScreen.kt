package com.example.ynspo.ui.screen.profile

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.profile.UserProfile
import com.example.ynspo.user.GoogleLoginButton
import com.example.ynspo.R
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.ui.theme.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileScreen(
    viewModel: ProfileWithAuthViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
) {
    val userProfile by viewModel.userProfile.observeAsState()
    val firebaseUser by viewModel.firebaseUser.collectAsStateWithLifecycle()
    val persistedUser by viewModel.persistedUser.observeAsState()
    val isAuthenticated by viewModel.isAuthenticated.observeAsState(false)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    val context = LocalContext.current

    // Mostrar error si existe
    errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Aquí podrías mostrar un Snackbar o Toast
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            // Indicador de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (!isAuthenticated) {
            // Pantalla de login
            LoginSection(
                onLoginClick = { viewModel.launchCredentialManager() },
                errorMessage = errorMessage
            )
        } else {
            // Pantalla de perfil autenticado
            AuthenticatedProfileContent(
                userProfile = userProfile,
                firebaseUser = firebaseUser,
                persistedUser = persistedUser,
                onSignOut = { viewModel.signOut() },
                themeViewModel = themeViewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
private fun LoginSection(
    onLoginClick: () -> Unit,
    errorMessage: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "¡Bienvenido a Ynspo!",
            style = MaterialTheme.typography.headlineMedium,
            color = DetailColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        Text(
            text = "Inicia sesión para ver tu perfil, crear boards y guardar tus inspiraciones favoritas",
            style = MaterialTheme.typography.bodyLarge,
            color = DetailColor,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingL))
        
        GoogleLoginButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        )
        
        errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(Dimens.PaddingM))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuthenticatedProfileContent(
    userProfile: UserProfile?,
    firebaseUser: com.google.firebase.auth.FirebaseUser?,
    persistedUser: com.example.ynspo.data.db.entity.FirebaseUserEntity?,
    onSignOut: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    // Usar datos de Firebase si está disponible, sino usar datos persistidos
    val displayName = firebaseUser?.displayName ?: persistedUser?.displayName ?: "Usuario"
    val photoUrl = firebaseUser?.photoUrl?.toString() ?: persistedUser?.photoUrl
    val email = firebaseUser?.email ?: persistedUser?.email
    val isDarkMode by themeViewModel.isDarkMode

    userProfile?.let { profile ->
        Image(
            painter = rememberAsyncImagePainter(photoUrl ?: profile.photoUrl),
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(Dimens.ProfileImageSize)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingM))

        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineSmall,
            color = DetailColor,
            fontWeight = FontWeight.Bold
        )
        
        email?.let { userEmail ->
            Spacer(modifier = Modifier.height(Dimens.PaddingXS))
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = DetailColor.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        Text(
            text = profile.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = DetailColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        // Statistics row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat("Pins", 240)
            ProfileStat("Siguiendo", 183)
            ProfileStat("Seguidores", 1352)
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        Text(
            text = stringResource(R.string.profile_hobbies),
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        profile.hobbies.forEach { hobby ->
            Text(
                text = "• $hobby",
                style = MaterialTheme.typography.bodyLarge,
                color = SelectedColor,
                modifier = Modifier.padding(vertical = Dimens.PaddingXXS)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingL))
        
        // Toggle Dark Mode
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingM),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Modo Oscuro",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (isDarkMode) "Activado" else "Desactivado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { themeViewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.PaddingL))
        
        // Botón de logout
        OutlinedButton(
            onClick = onSignOut,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
            )
        ) {
            Text("Cerrar Sesión")
        }
    } ?: run {
        // Mostrar mientras se carga el perfil
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(Dimens.PaddingM))
            Text(
                text = "Cargando perfil...",
                style = MaterialTheme.typography.bodyMedium,
                color = DetailColor
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
