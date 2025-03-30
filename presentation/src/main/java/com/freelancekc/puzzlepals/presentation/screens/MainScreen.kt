package com.freelancekc.puzzlepals.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.freelancekc.puzzlepals.presentation.navigation.NavRoute
import com.freelancekc.puzzlepals.presentation.screens.creation.CreationScreen
import com.freelancekc.puzzlepals.presentation.screens.feed.FeedAndSearchScreen
import com.freelancekc.puzzlepals.presentation.screens.home.HomeScreen
import com.freelancekc.puzzlepals.presentation.screens.puzzle.PuzzleScreen
import com.freelancekc.puzzlepals.presentation.screens.profile.ProfileScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                NavRoute.bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon!!, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoute.Home.route) {
                HomeScreen(
                    onNavigateToPuzzle = { puzzleId ->
                        navController.navigate(NavRoute.Puzzle.createRoute(puzzleId))
                    }
                )
            }
            composable(NavRoute.Browse.route) {
                FeedAndSearchScreen()
            }
            composable(NavRoute.Create.route) {
                CreationScreen()
            }
            composable(NavRoute.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = NavRoute.Puzzle("").route,
                arguments = listOf(
                    navArgument("puzzleId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) { backStackEntry ->
                PuzzleScreen(backStackEntry = backStackEntry)
            }
        }
    }
} 