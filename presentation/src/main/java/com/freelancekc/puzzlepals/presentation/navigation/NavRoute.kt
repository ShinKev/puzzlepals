package com.freelancekc.puzzlepals.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoute(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    data object Home : NavRoute(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    
    data object Browse : NavRoute(
        route = "browse",
        title = "Browse",
        icon = Icons.Default.Search
    )
    
    data object Create : NavRoute(
        route = "create",
        title = "Create",
        icon = Icons.Default.Create
    )
    
    data object Profile : NavRoute(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    data class Puzzle(val puzzleId: String) : NavRoute(
        route = "puzzle/{puzzleId}",
        title = "Puzzle"
    ) {
        companion object {
            fun createRoute(puzzleId: String) = "puzzle/$puzzleId"
        }
    }
    
    companion object {
        val bottomNavItems = listOf(
            Home,
            Browse,
            Create,
            Profile
        )
    }
} 