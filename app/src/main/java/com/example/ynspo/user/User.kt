package com.example.ynspo.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ynspo.ui.theme.Dimens

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun User(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userData = userViewModel.userData.collectAsStateWithLifecycle()

    if (userData.value == null) {
        Button(onClick = { userViewModel.launchCredentialManager() }) {
            Text("Sign in")
        }
    } else {
        Column {
            AsyncImage(
                model = userData.value?.photoUrl,
                contentDescription = "User Avatar",
                modifier = Modifier.size(Dimens.UserAvatarSize)
            )
            Text(
                text = userData.value?.displayName ?: "Unknown User"
            )
        }
    }
}