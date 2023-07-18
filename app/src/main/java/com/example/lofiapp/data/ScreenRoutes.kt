package com.example.lofiapp.data

sealed class ScreenRoutes(val route: String) {

    object SplashScreen : ScreenRoutes("splash_screen")
    object HomeScreen : ScreenRoutes("home_screen")
    object SearchScreen : ScreenRoutes("search_screen")
    object VideoScreen: ScreenRoutes("video_screen/{video_id}")
    object PlaylistScreen: ScreenRoutes("playlist_screen")
    object EditPlaylistScreen: ScreenRoutes("edit_playlist_screen")


}
