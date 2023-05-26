package com.example.lofiapp.data

sealed class ScreenRoutes(val route: String) {
    object HomeScreen : ScreenRoutes("home_screen")
    object SearchScreen : ScreenRoutes("search_screen")
    object VideoScreen: ScreenRoutes("video_screen")
    object PlaylistScreen: ScreenRoutes("playlist_screen")
}
