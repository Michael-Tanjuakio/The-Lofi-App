package com.example.lofiapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.LofiappTheme
import com.example.lofiapp.ui.theme.flamenco_regular

@Composable
fun HomeScreen(title: String, search: String, navController: NavController) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = title, color = Color.White, fontFamily = flamenco_regular, fontSize = 32.sp)
            },
            backgroundColor = Color(0xFF24CAAC),
            actions = {
                IconButton(onClick = { navController.navigate(ScreenRoutes.SearchScreen.route)}) {
                    Icon(imageVector = MenuAction.Search.icon,
                        contentDescription = stringResource(MenuAction.Search.label),
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                            .padding(top = 5.dp)
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LofiappTheme {
//        HomeScreen(
//            stringResource(R.string.app_name),
//            stringResource(R.string.Search)
//        );
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