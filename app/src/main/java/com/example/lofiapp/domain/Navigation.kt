package com.example.lofiapp.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.screens.HomeScreen
import com.example.lofiapp.screens.SearchScreen

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
    }
}