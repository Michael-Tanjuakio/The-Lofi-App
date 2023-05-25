package com.example.lofiapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.lofiapp.R
import com.example.lofiapp.ui.theme.LofiappTheme
import com.example.lofiapp.ui.theme.flamenco_regular

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LofiappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    displayScreen(
                        stringResource(R.string.app_name),
                        stringResource(R.string.Search)
                    )
                }
            }
        }
    }
}

@Composable
fun displayScreen(title: String, search: String) {
    var text by remember { mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = title, color = Color.White, fontFamily = flamenco_regular, fontSize = 32.sp)
            },
            backgroundColor = Color(0xFF24CAAC)
        )


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LofiappTheme {
        displayScreen(
            stringResource(R.string.app_name),
            stringResource(R.string.Search)
        );
    }
}

/*
        TextField(
            placeholder = {
                Text(text = search,
                    fontFamily = flamenco_regular,
                    fontSize = 16.sp)
                          },
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 48.dp),
            singleLine = true
        )
*/