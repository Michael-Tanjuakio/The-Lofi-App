package com.example.lofiapp.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.lofiapp.screens.single_playlist
import com.example.lofiapp.screens.youtubeVideo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

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
            route = "home_screen" +
                    "?bottomBar_pic={bottomBar_pic}" +
                    "?bottomBar_title={bottomBar_title}",
            arguments = listOf(
                navArgument("bottomBar_pic") { defaultValue = "" },
                navArgument("bottomBar_title") { defaultValue = "" }
            )
        ) { entry ->
            // Initialize variables (no video playing)
            var bottomBar_pic = entry.arguments?.getString("bottomBar_pic").toString()
            var bottomBar_title = entry.arguments?.getString("bottomBar_title").toString()

            // Navigating Back with data from a screen
            if (!(entry.savedStateHandle.get<String>("new_bottomBar_title")
                    .toString()).equals("null")
            ) {
                bottomBar_pic = entry.savedStateHandle.get<String>("new_bottomBar_pic").toString()
                bottomBar_title =
                    entry.savedStateHandle.get<String>("new_bottomBar_title").toString()
                Log.d(
                    "new_bp",
                    "HomeScreen: navBack: bottomBar_pic = " + bottomBar_pic + " bt = " + bottomBar_title
                )
            }

            // Display screen
            HomeScreen(
                navController = navController,
                bottomBar_pic,
                bottomBar_title
            )
        }

        // search screen
        composable(
            route = "search_screen" +
                    "?bottomBar_pic={bottomBar_pic}" +
                    "&bottomBar_title={bottomBar_title}",
            arguments = listOf(
                navArgument("bottomBar_pic") { defaultValue = "" },
                navArgument("bottomBar_title") { defaultValue = "" }
            )
        ) { entry ->

            // No Video playing / Navforward
            var bottomBar_pic = entry.arguments?.getString("bottomBar_pic").toString()
            var bottomBar_title = entry.arguments?.getString("bottomBar_title").toString()
            Log.d(
                "new_bp",
                "SearchScreen: navNext: bottomBar_pic = " + entry.arguments?.getString("bottomBar_pic")
                    .toString() + " bt = " + bottomBar_title
            )

            // Navigating Back with data from a screen
            if (!(entry.savedStateHandle.get<String>("new_bottomBar_title")
                    .toString()).equals("null")
            ) {
                bottomBar_pic = entry.savedStateHandle.get<String>("new_bottomBar_pic").toString()
                bottomBar_title =
                    entry.savedStateHandle.get<String>("new_bottomBar_title").toString()
                Log.d(
                    "new_bp",
                    "SearchScreen: navBack: bottomBar_pic = " + bottomBar_pic + " bottomBar_title = " + bottomBar_title
                )
            }

            // Display search screen
            SearchScreen(
                navController = navController,
                bottomBar_pic,
                bottomBar_title
            )
        }

        // video screen
        composable(
            route = "video_screen" +
                    "/{bottomBar_pic}" +
                    "/{bottomBar_title}",
            arguments = listOf(
                navArgument("bottomBar_pic") { defaultValue = "" },
                navArgument("bottomBar_title") { defaultValue = "" }
            )
        ) { entry ->

            // Initialize variables (no video playing)
            var bottomBar_pic = entry.arguments?.getString("bottomBar_pic").toString()
            var bottomBar_title = entry.arguments?.getString("bottomBar_title").toString()

            // Navigating Back with data from a screen
            if (!(entry.savedStateHandle.get<String>("new_bottomBar_title")
                    .toString()).equals("null")
            ) {
                bottomBar_pic = entry.savedStateHandle.get<String>("new_bottomBar_pic").toString()
                bottomBar_title =
                    entry.savedStateHandle.get<String>("new_bottomBar_title").toString()
                Log.d(
                    "new_bp",
                    "navBack: bottomBar_pic = " + bottomBar_pic + " bt = " + bottomBar_title
                )
            }

            // Display video screen
            VideoScreen(
                navController = navController,
                bottomBar_pic,
                bottomBar_title
            )
        }

        // playlist screen
        composable(
            route = "playlist_screen" +
                    "/{playlist_id}" +
                    "?new_playlist={new_playlist}" +
                    "&bottomBar_pic={bottomBar_pic}" +
                    "&bottomBar_title={bottomBar_title}" +
                    "&playlist_count={playlist_count}",
            arguments = listOf(
                navArgument("playlist_id") { type = NavType.StringType },
                navArgument("new_playlist") { type = NavType.BoolType },
                navArgument("bottomBar_pic") { defaultValue = "" },
                navArgument("bottomBar_title") { defaultValue = "" },
                navArgument("playlist_count") { defaultValue = 0 },

                )
        ) { entry ->

            // No Video playing / Navforward
            var bottomBar_pic = entry.arguments?.getString("bottomBar_pic").toString()
            var bottomBar_title = entry.arguments?.getString("bottomBar_title").toString()
            var new_playlist = entry.arguments?.getBoolean("new_playlist")
            var playlist_id = entry.arguments?.getString("playlist_id")

            // Navigating Back with data from a screen
            if (
                !(entry.savedStateHandle.get<String>("new_bottomBar_title")
                    .toString()).equals("null")
            ) {
                bottomBar_pic = entry.savedStateHandle.get<String>("new_bottomBar_pic").toString()
                bottomBar_title =
                    entry.savedStateHandle.get<String>("new_bottomBar_title").toString()
                Log.d(
                    "new_bp",
                    "passing: PlaylistScreen: navBack: bottomBar_pic = " + bottomBar_pic + " bt = " + bottomBar_title
                )
            }
            if (
                !((entry.savedStateHandle.get<Boolean>("new_playlist"))
                    .toString().equals("null"))
            ) {
                new_playlist = entry.savedStateHandle.get<Boolean>("new_playlist")
                playlist_id = entry.savedStateHandle.get<String>("playlist_id")
            }

            // Create new playlist (boolean)
            if (new_playlist == true) {
                // get children in home screen (top) DONE
                // add playlist id DONE
                // add "add" button in home screen and search screen
                // change display for empty playlist DONE
                // add remove function
                // fix bottom bar for delete in playlist/edit playlist screen

                var i = entry.arguments!!.getInt("playlist_count")
                Log.d(
                    "new_bp",
                    "passing: playlist_count = " + i
                )

                // Create playlist: "New Playlist #i" and add to database
                LaunchedEffect(true) {
                    FirebaseDatabase.getInstance().getReference("playlists")
                        .child("Playlist " + i).setValue(
                            single_playlist(
                                "Playlist " + i, // path string
                                "New Playlist " + i, // title of playlist
                                0, // playlist video count
                                mutableListOf<youtubeVideo>() // list of videos
                            )
                        )
                }

                // Go to new playlist i
                PlaylistScreen(
                    navController = navController,
                    playlist_path = "Playlist " + i,
                    bottomBar_pic,
                    bottomBar_title
                )
            } else {

                // Go to playlist
                if (playlist_id != null) {
                    PlaylistScreen(
                        navController = navController,
                        playlist_path = playlist_id,
                        bottomBar_pic,
                        bottomBar_title
                    )
                }

            }
        }

        // edit playlist screen
        composable(
            route = "edit_playlist_screen" +
                    "/{playlist_path}" +
                    "?bottomBar_pic={bottomBar_pic}" +
                    "&bottomBar_title={bottomBar_title}",
            arguments = listOf(
                navArgument("playlist_path") { type = NavType.StringType },
                navArgument("bottomBar_pic") { defaultValue = "" },
                navArgument("bottomBar_title") { defaultValue = "" }
            )
        ) { entry ->

            // No Video playing / Navforward
            var bottomBar_pic = entry.arguments?.getString("bottomBar_pic").toString()
            var bottomBar_title = entry.arguments?.getString("bottomBar_title").toString()

            // Navigating Back with data from a screen
            if (!(entry.savedStateHandle.get<String>("new_bottomBar_title")
                    .toString()).equals("null")
            ) {
                bottomBar_pic = entry.savedStateHandle.get<String>("new_bottomBar_pic").toString()
                bottomBar_title =
                    entry.savedStateHandle.get<String>("new_bottomBar_title").toString()
            }

            // Get the playlist
            var addVideo by remember { mutableStateOf(single_playlist()) }
            LaunchedEffect(true) {
            }


            // Go to edit playlist screen
            EditPlaylistScreen(
                navController = navController,
                playlist_path = entry.arguments?.getString("playlist_path").toString(),
                bottomBar_pic,
                bottomBar_title,
            )
        }
    }
}

