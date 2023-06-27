package com.example.lofiapp.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.screens.EditPlaylistScreen
import com.example.lofiapp.screens.HomeScreen
import com.example.lofiapp.screens.SearchScreen
import com.example.lofiapp.screens.VideoScreen
import com.example.lofiapp.screens.PlaylistScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenRoutes.HomeScreen.route) {
        composable(route = ScreenRoutes.HomeScreen.route) { // home screen
            HomeScreen(
                stringResource(R.string.app_name),
                stringResource(R.string.Search),
                navController
            )
        }
        composable(route = ScreenRoutes.SearchScreen.route) {// search screen
            SearchScreen(navController = navController)
        }
        composable(route = ScreenRoutes.VideoScreen.route) { // video screen
            VideoScreen(navController = navController)
        }
        composable(route = ScreenRoutes.PlaylistScreen.route) {// Playlist screen
            PlaylistScreen(navController = navController)
        }
        composable(route = ScreenRoutes.EditPlaylistScreen.route) {// Edit playlist screen
            EditPlaylistScreen(navController = navController)
        }
    }
}

