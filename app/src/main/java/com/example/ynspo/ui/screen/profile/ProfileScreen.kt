package com.example.ynspo.ui.screen.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.profile.UserProfile
import com.example.ynspo.user.GoogleLoginButton
import com.example.ynspo.R
import com.example.ynspo.ui.theme.Dimens
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
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            // Indicador de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
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
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        Text(
            text = "Inicia sesión para ver tu perfil, crear boards y guardar tus inspiraciones favoritas",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingL))
        
        GoogleLoginButton(
            onClick = onLoginClick
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
    val authViewModel: com.example.ynspo.ui.components.AuthenticationCheckViewModel = hiltViewModel()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Profile header
        ProfileHeader(
            userProfile = userProfile,
            firebaseUser = firebaseUser,
            persistedUser = persistedUser
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingXXL))
        
        // Theme toggle
        ThemeToggleSection(themeViewModel = themeViewModel)
        
        Spacer(modifier = Modifier.height(Dimens.PaddingXXL))
        
        // Sign out buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingM)
        ) {
            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión de Google")
            }
            
            OutlinedButton(
                onClick = { authViewModel.clearAuthentication() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión biométrica")
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    userProfile: UserProfile?,
    firebaseUser: com.google.firebase.auth.FirebaseUser?,
    persistedUser: com.example.ynspo.data.db.entity.FirebaseUserEntity?
) {
    // Usar datos de Firebase si está disponible, sino usar datos persistidos
    val displayName = firebaseUser?.displayName ?: persistedUser?.displayName ?: "Usuario"
    val photoUrl = firebaseUser?.photoUrl?.toString() ?: persistedUser?.photoUrl
    val email = firebaseUser?.email ?: persistedUser?.email

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
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        
        email?.let { userEmail ->
            Spacer(modifier = Modifier.height(Dimens.PaddingXS))
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        Text(
            text = profile.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
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
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        profile.hobbies.forEach { hobby ->
            Text(
                text = "• $hobby",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = Dimens.PaddingXXS)
            )
        }
    }
}

@Composable
private fun ThemeToggleSection(themeViewModel: ThemeViewModel) {
    val isDarkMode by themeViewModel.isDarkMode
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationS)
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
}

@Composable
fun ProfileStat(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}
