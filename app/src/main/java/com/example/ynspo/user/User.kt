package com.example.ynspo.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun User() {
    val viewModel = hiltViewModel<UserViewModel>()
    val userData = viewModel.userData.collectAsStateWithLifecycle()

    if (userData.value == null) {
        GoogleLoginButton(
            onClick = viewModel::launchCredentialManager
        )
    } else {
        Column {
            AsyncImage(
                model = userData.value?.photoUrl,
                contentDescription = "",
                modifier = Modifier.size(40.dp),
            )
            Text(
                userData.value?.displayName ?: ""
            )
            Text(
                userData.value?.email ?: ""
            )

            Button(onClick = { viewModel.signOut() }) {
                Text("Sign out")
            }
        }
    }
}