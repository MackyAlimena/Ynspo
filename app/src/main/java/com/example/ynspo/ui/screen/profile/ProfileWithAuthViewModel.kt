package com.example.ynspo.ui.screen.profile

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.map
import com.example.ynspo.R
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.entity.FirebaseUserEntity
import com.example.ynspo.data.repository.UserProfileRepository
import com.example.ynspo.ui.profile.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ProfileWithAuthViewModel"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class ProfileWithAuthViewModel @Inject constructor(
    private val database: YnspoDatabase,
    private val userProfileRepository: UserProfileRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    // Estados de autenticación
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Usuario actual de Firebase
    private val _firebaseUser = MutableStateFlow(auth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    // Usuario persistido en Room (último logueado)
    val persistedUser: LiveData<FirebaseUserEntity?> = database.firebaseUserDao().getLastLoggedUser()

    // Perfil del usuario desde Room
    val userProfile: LiveData<UserProfile?> = userProfileRepository.userProfile

    // Estado combinado: si tiene firebase user o persisted user
    val isAuthenticated: LiveData<Boolean> = persistedUser.map { persistedUser ->
        // Primero verificar Firebase Auth, luego usuario persistido
        val hasFirebaseUser = auth.currentUser != null
        val hasPersistedUser = persistedUser != null
        
        Log.d(TAG, "Auth status - Firebase: $hasFirebaseUser, Persisted: $hasPersistedUser")
        
        // Si encontramos cualquier usuario, dejar de cargar inmediatamente
        _isLoading.value = false
        
        hasFirebaseUser || hasPersistedUser
    }

    init {
        // Inicializar verificando ambos estados
        viewModelScope.launch {
            // Esperar un poco para que Room se inicialice
            kotlinx.coroutines.delay(500)
            
            auth.currentUser?.let { firebaseUser ->
                _firebaseUser.value = firebaseUser
                Log.d(TAG, "Usuario de Firebase encontrado al inicializar: ${firebaseUser.displayName}")
                // Persistir usuario en Room
                saveFirebaseUserToRoom(firebaseUser)
                createDefaultProfileIfNeeded(firebaseUser)
            }
            
            // Si no hay usuario de Firebase, verificar si hay usuario persistido directamente
            if (auth.currentUser == null) {
                Log.d(TAG, "No hay usuario de Firebase, verificando usuario persistido...")
                try {
                    val lastUser = database.firebaseUserDao().getLastLoggedUserDirect()
                    if (lastUser != null) {
                        Log.d(TAG, "Usuario persistido encontrado: ${lastUser.displayName}")
                        createDefaultProfileIfNeeded(lastUser)
                    } else {
                        Log.d(TAG, "No hay usuario persistido")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error verificando usuario persistido: ${e.message}")
                }
            }
            
            // Dejar de cargar después de verificar todo
            _isLoading.value = false
        }
    }

    fun launchCredentialManager() {
        _isLoading.value = true
        _errorMessage.value = null

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.google_server_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                _isLoading.value = false
                _errorMessage.value = "Error de autenticación: ${e.localizedMessage}"
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            _isLoading.value = false
            _errorMessage.value = "Credencial no válida"
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    _firebaseUser.value = user
                    
                    // Persistir usuario en Room y crear perfil
                    user?.let { firebaseUser ->
                        viewModelScope.launch {
                            saveFirebaseUserToRoom(firebaseUser)
                            createDefaultProfileIfNeeded(firebaseUser)
                        }
                    }
                } else {
                    _firebaseUser.value = null
                    _errorMessage.value = "Error al iniciar sesión: ${task.exception?.localizedMessage}"
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private suspend fun saveFirebaseUserToRoom(firebaseUser: FirebaseUser) {
        try {
            val userEntity = FirebaseUserEntity(
                uid = firebaseUser.uid,
                email = firebaseUser.email,
                displayName = firebaseUser.displayName,
                photoUrl = firebaseUser.photoUrl?.toString(),
                isEmailVerified = firebaseUser.isEmailVerified,
                lastLoginTime = System.currentTimeMillis(),
                creationTime = firebaseUser.metadata?.creationTimestamp ?: System.currentTimeMillis()
            )
            database.firebaseUserDao().insertUser(userEntity)
            Log.d(TAG, "Usuario persistido en Room: ${firebaseUser.displayName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al persistir usuario en Room: ${e.message}")
        }
    }

    private suspend fun createDefaultProfileIfNeeded(firebaseUser: FirebaseUser) {
        try {
            // Verificar si ya existe un perfil usando Room directamente
            val existingProfile = database.userProfileDao().getUserProfile()
            if (existingProfile == null) {
                // Crear entidad de perfil directamente
                val newProfileEntity = com.example.ynspo.data.db.entity.UserProfileEntity(
                    name = firebaseUser.displayName ?: "Usuario",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    bio = "¡Hola! Soy ${firebaseUser.displayName ?: "un nuevo usuario"}",
                    darkModeEnabled = false
                )
                database.userProfileDao().insertUserProfile(newProfileEntity)
                
                // Crear hobbies por defecto
                val defaultHobbies = listOf("Fotografía", "Arte", "Inspiración")
                defaultHobbies.forEach { hobby ->
                    val hobbyEntity = com.example.ynspo.data.db.entity.UserHobbyEntity(
                        userId = "user_profile",
                        hobby = hobby
                    )
                    database.userProfileDao().insertHobby(hobbyEntity)
                }
                
                Log.d(TAG, "Perfil creado automáticamente para: ${firebaseUser.displayName}")
            } else {
                Log.d(TAG, "El usuario ya tiene un perfil existente")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al crear perfil por defecto: ${e.message}")
        }
    }

    fun signOut() {
        _isLoading.value = true
        
        // Firebase sign out
        auth.signOut()
        _firebaseUser.value = null

        viewModelScope.launch {
            try {
                // Limpiar credenciales del Credential Manager
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                
                // Limpiar usuarios persistidos en Room
                database.firebaseUserDao().deleteAllUsers()
                
                _isLoading.value = false
                Log.d(TAG, "Usuario deslogueado exitosamente")
            } catch (e: ClearCredentialException) {
                _isLoading.value = false
                _errorMessage.value = "Error al cerrar sesión: ${e.localizedMessage}"
                Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // Función para actualizar el perfil
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            userProfileRepository.saveUserProfile(profile)
        }
    }

    // Nueva función para manejar usuarios persistidos
    private suspend fun createDefaultProfileIfNeeded(persistedUser: FirebaseUserEntity) {
        try {
            val existingProfile = database.userProfileDao().getUserProfile()
            if (existingProfile == null) {
                val newProfileEntity = com.example.ynspo.data.db.entity.UserProfileEntity(
                    name = persistedUser.displayName ?: "Usuario",
                    photoUrl = persistedUser.photoUrl,
                    bio = "¡Hola! Soy ${persistedUser.displayName ?: "un nuevo usuario"}",
                    darkModeEnabled = false
                )
                database.userProfileDao().insertUserProfile(newProfileEntity)
                
                val defaultHobbies = listOf("Fotografía", "Arte", "Inspiración")
                defaultHobbies.forEach { hobby ->
                    val hobbyEntity = com.example.ynspo.data.db.entity.UserHobbyEntity(
                        userId = "user_profile",
                        hobby = hobby
                    )
                    database.userProfileDao().insertHobby(hobbyEntity)
                }
                
                Log.d(TAG, "Perfil creado para usuario persistido: ${persistedUser.displayName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al crear perfil para usuario persistido: ${e.message}")
        }
    }
} 