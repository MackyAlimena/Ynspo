package com.example.ynspo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ynspo.R
import com.example.ynspo.ui.theme.Cardinal

data class BottomNavItem(val route: String, val icon: ImageVector, val labelRes: Int)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, R.string.home_label),
    //BottomNavItem("boards", Icons.Default.Dashboard, R.string.boards_label),
    BottomNavItem("profile", Icons.Default.Person, R.string.profile_label)
)

@Composable
fun BottomBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Cardinal) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                icon = { Icon(item.icon, contentDescription = stringResource(id = item.labelRes)) },
                label = { Text(stringResource(id = item.labelRes)) }
            )
        }
    }
}