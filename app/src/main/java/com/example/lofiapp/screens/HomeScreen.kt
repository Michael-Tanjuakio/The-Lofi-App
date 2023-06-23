package com.example.lofiapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.LofiappTheme
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light

data class videoDisplay(var videoTitle:String)

@Composable
fun HomeScreen(title: String, search: String, navController: NavController) {

    val list = listOf("1","1","1","1")

    val video_id = "jfKfPfyJRdk"
    val fullsize_path_img:String = "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // Display video thumbnail

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
                IconButton(onClick = { navController.navigate(ScreenRoutes.SearchScreen.route)}) { // change this
                    Icon(imageVector = MenuAction.Search.icon,
                        contentDescription = stringResource(MenuAction.Search.label),
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                            .padding(top = 5.dp)
                    )
                }
            }
        )

        Text(   text = "Recommended",
                color = Color(0xFF24CAAC),
                modifier = Modifier.padding(top = 20.dp, start = 29.dp),
                fontSize = 26.sp,
                fontFamily = montserrat_bold
        )

        LazyRow(modifier = Modifier.padding(start = 13.dp)) {
            items(items = list, itemContent = { item ->
                Column() {
                    AsyncImage(
                        model = fullsize_path_img,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(width = 220.dp, height = 134.dp)
                            .clip(RoundedCornerShape(12))
                    )
                    Text(
                        text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                        maxLines = 2,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .width(220.dp)
                            .height(IntrinsicSize.Max),
                        fontSize = 16.sp,
                        fontFamily = montserrat_light
                    )
                }
            })
        }

        Text(   text = "Playlists",
                color = Color(0xFF24CAAC),
                modifier = Modifier.padding(top = 20.dp, start = 29.dp),
                fontSize = 26.sp,
                fontFamily = montserrat_bold
        )

        LazyRow(modifier = Modifier.padding(start = 13.dp)) {
            items(items = list, itemContent = { item ->
                Column() {
                    AsyncImage(
                        model = fullsize_path_img,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(width = 220.dp, height = 134.dp)
                            .clip(RoundedCornerShape(12))
                    )
                    Text(
                        text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                        maxLines = 2,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .width(220.dp)
                            .height(IntrinsicSize.Max),
                        fontSize = 16.sp,
                        fontFamily = montserrat_light
                    )
                }
            })
        }
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