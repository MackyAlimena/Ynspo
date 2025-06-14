package com.example.ynspo.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.auth.AuthStatusViewModel

@Composable
fun AuthenticationCheck(
    navController: NavController,
    authRoute: String = "boards",
    content: @Composable () -> Unit
) {
    val authStatusViewModel: AuthStatusViewModel = hiltViewModel()
    val isAuthenticated by authStatusViewModel.isAuthenticated.collectAsState()

    if (isAuthenticated) {
        content()
    } else {
        navController.navigate(authRoute)
    }
}
