package com.example.ynspo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ynspo.R
import com.example.ynspo.ui.theme.BottomBarPurple
import com.example.ynspo.ui.theme.BottomBarPurpleDark
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val isDarkTheme = isSystemInDarkTheme()
    
    NavigationBar(
        containerColor = if (isDarkTheme) BottomBarPurpleDark else BottomBarPurple,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.home_label)) },
            selected = currentDestination == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.background,
                unselectedIconColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                unselectedTextColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text(stringResource(R.string.boards_label)) },
            selected = currentDestination == "boards" || currentDestination == "boards_content" || currentDestination?.startsWith("boardDetail") == true,
            onClick = { navController.navigate("boards") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.background,
                unselectedIconColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                unselectedTextColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            label = { Text(stringResource(R.string.upload_label)) },
            selected = currentDestination == "uploadPin",
            onClick = { navController.navigate("uploadPin") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.background,
                unselectedIconColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                unselectedTextColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text(stringResource(R.string.notifications_label)) },
            selected = currentDestination == "notifications",
            onClick = { navController.navigate("notifications") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.background,
                unselectedIconColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                unselectedTextColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text(stringResource(R.string.profile_label)) },
            selected = currentDestination == "profile",
            onClick = { navController.navigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.background,
                unselectedIconColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                unselectedTextColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
            )
        )
    }
}