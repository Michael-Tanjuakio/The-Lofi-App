package com.example.lofiapp.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.screens.EditPlaylistScreen
import com.example.lofiapp.screens.HomeScreen
import com.example.lofiapp.screens.SearchScreen
import com.example.lofiapp.screens.VideoScreen
import com.example.lofiapp.screens.PlaylistScreen
import com.example.lofiapp.screens.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.SplashScreen.route
    ) {

        // splash screen
        composable(route = ScreenRoutes.SplashScreen.route) {
            SplashScreen(navController)
        }

        // home screen
        composable(
            route = "home_screen/{video_title}/{video_id}",
            arguments = listOf(
                navArgument("video_title") { type = NavType.StringType },
                navArgument("video_id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val video_title = backStackEntry.arguments?.getString("video_title").toString()
            val video_id = backStackEntry.arguments?.getString("video_id").toString()
            HomeScreen(navController = navController, video_title = video_title, video_id = video_id)
        }

        // search screen
        composable(route = ScreenRoutes.SearchScreen.route) {
            SearchScreen(navController = navController)
        }

        // video screen
        composable(
            route = "video_screen/{video_title}/{video_id}",
            arguments = listOf(
                navArgument("video_title") { type = NavType.StringType },
                navArgument("video_id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val video_title = backStackEntry.arguments?.getString("video_title").toString()
            val video_id = backStackEntry.arguments?.getString("video_id").toString()
            VideoScreen(navController = navController, video_title = video_title, video_id = video_id)
        }

        // playlist screen
        composable(
            route = "playlist_screen/{playlist_name}",
            arguments = listOf(
                navArgument("playlist_name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            var playlist_name = backStackEntry.arguments?.getString("playlist_name").toString()
            if (playlist_name.equals("new_playlist")) {
                playlist_name = "New Playlist #"
            }
            PlaylistScreen(navController = navController, playlist_name = playlist_name)
        }

        // edit playlist screen
        composable(route = ScreenRoutes.EditPlaylistScreen.route) {
            EditPlaylistScreen(navController = navController)
        }
    }
}

