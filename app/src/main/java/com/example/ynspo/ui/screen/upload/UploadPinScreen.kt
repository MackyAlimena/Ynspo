package com.example.ynspo.ui.screen.upload

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.components.upload.ImageSelector
import com.example.ynspo.ui.components.upload.UploadPinForm
import com.example.ynspo.ui.components.upload.UploadButton
import com.example.ynspo.ui.components.dialog.SaveToBoardAfterUploadDialog
import com.example.ynspo.ui.screen.profile.ProfileWithAuthViewModel
import com.example.ynspo.ui.theme.Dimens
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.ynspo.R
import com.example.ynspo.ui.screen.boards.BoardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPinScreen(
    navController: NavController,
    uploadViewModel: UploadPinViewModel = hiltViewModel(),
    profileViewModel: ProfileWithAuthViewModel = hiltViewModel(),
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    val uploadState by uploadViewModel.state.collectAsStateWithLifecycle()
    val firebaseUser by profileViewModel.firebaseUser.collectAsStateWithLifecycle()
    val persistedUser by profileViewModel.persistedUser.observeAsState()
    
    val userId = firebaseUser?.uid ?: persistedUser?.uid ?: "anonymous"
    val userName = firebaseUser?.displayName ?: persistedUser?.displayName
    val userPhotoUrl = firebaseUser?.photoUrl?.toString() ?: persistedUser?.photoUrl
    
    val snackbarHostState = remember { SnackbarHostState() }
    var showSaveToBoardDialog by remember { mutableStateOf(false) }
    
    uploadState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            uploadViewModel.clearError()
        }
    }
    
    // Mostrar éxito y diálogo cuando el upload sea exitoso Y el pin esté disponible
    if (uploadState.isSuccess && uploadState.uploadedPin != null) {
        LaunchedEffect(uploadState.uploadedPin) {
            snackbarHostState.showSnackbar(
                message = "¡Pin subido exitosamente!",
                duration = SnackbarDuration.Long,
                actionLabel = "Ver"
            )
            // Mostrar diálogo para guardar en board
            showSaveToBoardDialog = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.upload_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.PaddingL)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.PaddingL))
            
            Text(
                text = stringResource(R.string.upload_share_inspiration),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = Dimens.PaddingM)
            )
            
            Text(
                text = stringResource(R.string.upload_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = Dimens.PaddingXL)
            )
            
            ImageSelector(
                imageUri = uploadState.imageUri,
                onImageSelected = { uri ->
                    uploadViewModel.setImageUri(uri)
                },
                modifier = Modifier.padding(bottom = Dimens.PaddingL)
            )
            
            UploadPinForm(
                description = uploadState.description,
                hashtags = uploadState.hashtags,
                keywords = uploadState.keywords,
                onDescriptionChange = { uploadViewModel.setDescription(it) },
                onHashtagsChange = { uploadViewModel.setHashtags(it) },
                onKeywordsChange = { uploadViewModel.setKeywords(it) },
                modifier = Modifier.padding(bottom = Dimens.PaddingL)
            )
            
            UploadButton(
                isLoading = uploadState.isLoading,
                isEnabled = uploadState.imageUri != null && uploadState.description.isNotBlank(),
                onClick = {
                    uploadViewModel.uploadPin(
                        userId = userId,
                        userName = userName,
                        userPhotoUrl = userPhotoUrl
                    )
                },
                modifier = Modifier
                    .padding(bottom = Dimens.PaddingXXL)
                    .padding(bottom = 80.dp) // Padding extra para evitar overlap con navbar
            )
        }
    }
    
    // Diálogo para guardar en board
    if (showSaveToBoardDialog && uploadState.uploadedPin != null) {
        SaveToBoardAfterUploadDialog(
            uploadedPin = uploadState.uploadedPin!!,
            boardsViewModel = boardsViewModel,
            onDismiss = { 
                showSaveToBoardDialog = false
                uploadViewModel.clearSuccess()
                navController.popBackStack()
            }
        )
    }
} 