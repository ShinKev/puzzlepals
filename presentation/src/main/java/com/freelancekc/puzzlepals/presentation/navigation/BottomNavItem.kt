package com.freelancekc.puzzlepals.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    
    data object Browse : BottomNavItem(
        route = "browse",
        title = "Browse",
        icon = Icons.Default.Search
    )
    
    data object Create : BottomNavItem(
        route = "create",
        title = "Create",
        icon = Icons.Default.Create
    )
    
    data object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    
    companion object {
        val items = listOf(
            Home,
            Browse,
            Create,
            Profile
        )
    }
} 