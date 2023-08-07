package com.example.lofiapp.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            route = "home_screen?bp={bp}?bt={bt}",
            arguments = listOf(
                navArgument("bp") { defaultValue = "" },
                navArgument("bt") { defaultValue = "" }
            )
        ) { entry ->
            //
            var bp = entry.arguments?.getString("bp").toString()
            var bt = entry.arguments?.getString("bt").toString()
            var new_bt = entry.savedStateHandle.get<String>("new_bt").toString()
            Log.d("new_bp", "new_bt = " + new_bt + " null = " + !new_bt.equals("null"))
            if (!(new_bt.isNullOrEmpty()) && !new_bt.equals("null")) {
                bp = entry.savedStateHandle.get<String>("new_bp").toString()
                bt = entry.savedStateHandle.get<String>("new_bt").toString()
                Log.d("new_bp", "navBack: bp = " + bp + " bt = " + new_bt)
            }
            HomeScreen(
                navController = navController,
                bp,
                bt
            )
        }

        // search screen
        composable(
            route = "search_screen?bp={bp}?bt={bt}",
            arguments = listOf(
                navArgument("bp") { defaultValue = "" },
                navArgument("bt") { defaultValue = "" }
            )
        ) { backStackEntry ->

            Log.d(
                "video playing",
                "nav: " + backStackEntry.arguments?.getString("bt").toString() + " " + backStackEntry.arguments?.getString("bp").toString()
            )

            SearchScreen(
                navController = navController,
                backStackEntry.arguments?.getString("bp").toString(),
                backStackEntry.arguments?.getString("bt").toString()
            )
        }

        // video screen
        composable(
            route = "video_screen/{video_id}",
            arguments = listOf(
                navArgument("video_id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            VideoScreen(
                navController = navController,
                video_id = backStackEntry.arguments?.getString("video_id").toString())
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

