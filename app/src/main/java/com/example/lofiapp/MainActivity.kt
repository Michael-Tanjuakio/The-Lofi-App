package com.example.lofiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lofiapp.domain.Navigation
import com.example.lofiapp.screens.HomeScreen
import com.example.lofiapp.ui.theme.LofiappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { // when mainActivity is created (user opens app)
        super.onCreate(savedInstanceState) // original function (super keyword)
        setContent {
            LofiappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Navigation() // what is called
                }
            }
        }
    }
}


