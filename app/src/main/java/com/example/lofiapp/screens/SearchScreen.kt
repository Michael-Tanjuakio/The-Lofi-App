package com.example.lofiapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular

@Composable
fun SearchScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name), color = Color.White, fontFamily = flamenco_regular, fontSize = 32.sp)
            },
            backgroundColor = Color(0xFFCAB924)
        )
        Text(text = "Search Page")
    }
}