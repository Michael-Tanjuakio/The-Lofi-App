package com.example.lofiapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

data class videoDisplay(var videoTitle: String)

@Composable
fun HomeScreen(title: String, search: String, navController: NavController) {

    val list = listOf("1", "1", "1", "1") // placeholder
    val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img: String =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var text by remember { mutableStateOf("") }

    // Whole UI
    val scrollState = rememberScrollState() // vertically scrollable

    Scaffold(
        topBar = {    // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = Color.White,
                        fontFamily = flamenco_regular,
                        fontSize = 32.sp
                    )
                },
                backgroundColor = Color(0xFF24CAAC),
                actions = {
                    // Search Button
                    IconButton(onClick = { navController.navigate(ScreenRoutes.SearchScreen.route) }) {
                        Icon(
                            imageVector = MenuAction.Search.icon,
                            contentDescription = stringResource(MenuAction.Search.label),
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(top = 5.dp)
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                // Recommended Text
                Row(modifier = Modifier.padding(top = 45.dp, start = 30.dp)) {
                    Text(
                        text = "Recommended",
                        color = Color(0xFF24CAAC),
                        modifier = Modifier,
                        fontSize = 21.sp,
                        fontFamily = montserrat_bold
                    )
                    Image( // Symbol
                        painter = painterResource(id = R.drawable.video_library_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(34.dp),
                        colorFilter = ColorFilter.tint(color = Color(0xFF5686E1))
                    )
                }

                // Recommended Videos Display (horz. scroll)
                LazyRow(modifier = Modifier.padding(start = 13.dp, top = 6.dp)) {
                    items(items = list, itemContent = { item ->
                        Column(modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(12, 12, 5, 5))
                            .clickable {
                                navController.navigate(ScreenRoutes.VideoScreen.route)
                            }
                        ) { // Video Display
                            // {
                            AsyncImage( // Video thumbnail
                                model = fullsize_path_img,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 220.dp, height = 134.dp)
                                    .clip(RoundedCornerShape(12))
                            )
                            Text( // Video name
                                text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                                maxLines = 2,
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(IntrinsicSize.Max),
                                fontSize = 16.sp,
                                fontFamily = montserrat_light
                            )
                        }
                    })
                }

                // Playlists Text
                Row(modifier = Modifier.padding(top = 30.dp, start = 29.dp)) {
                    Text(
                        text = "Playlists",
                        color = Color(0xFF24CAAC),
                        modifier = Modifier,
                        fontSize = 21.sp,
                        fontFamily = montserrat_bold
                    )
                    Image( // Symbol
                        painter = painterResource(id = R.drawable.playlist_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(34.dp),
                        colorFilter = ColorFilter.tint(color = Color(0xFF5686E1))
                    )
                }

                // Playlists display (horz. scroll)
                LazyRow(modifier = Modifier.padding(start = 13.dp, top = 6.dp)) {
                    items(items = list, itemContent = { item ->
                        Column(
                            modifier = Modifier
                                            .padding(start = 16.dp)
                                            .clip(RoundedCornerShape(12, 12, 5, 5))
                                            .clickable{
                                                navController.navigate(ScreenRoutes.PlaylistScreen.route)
                                            }
                        ) {
                            Box() {
                                AsyncImage( // Video Thumbnail
                                    model = fullsize_path_img,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 220.dp, height = 134.dp)
                                        .clip(RoundedCornerShape(12))
                                )
                                Box( // Transparent background
                                    modifier = Modifier
                                        .size(width = 105.dp, height = 134.dp)
                                        .clip(RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp))
                                        .background(Color(0xFF404040).copy(alpha = 0.6f))
                                        .align(alignment = Alignment.TopEnd)
                                ) {
                                    Text( // Number of videos in playlist
                                        text = "5",
                                        modifier = Modifier
                                            .align(alignment = Alignment.Center),
                                        color = Color.White,
                                        fontFamily = montserrat_light
                                    )
                                }
                            }
                            Text( // Playlist Name
                                text = "city pop playlist",
                                maxLines = 2,
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(IntrinsicSize.Max),
                                fontSize = 16.sp,
                                fontFamily = montserrat_light
                            )
                        }
                    })
                }

                // Random video button
                Canvas(
                    modifier = Modifier
                        .size(size = 70.dp)
                        .padding(top = 93.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate(ScreenRoutes.VideoScreen.route)
                        }
                ) {
                    drawCircle( // green circle background
                        color = Color(0xFF24CAAC),
                        radius = 40.dp.toPx()
                    )
                }
                Image( // shuffle symbol
                    painter = painterResource(id = R.drawable.shuffle_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                        .align(Alignment.CenterHorizontally),
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        },
        bottomBar = {
            // Bottom bar (Displays what video is played)
            BottomNavigation(
                modifier = Modifier
                                .clickable {
                                    navController.navigate(ScreenRoutes.VideoScreen.route)
                                },
                backgroundColor = Color(0xFF3392EA)
            ) {
                Row() { // wrap in row to avoid default spacing
                    AsyncImage( // video thumbnail
                        model = fullsize_path_img,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 6.dp)
                            .size(width = 65.dp, height = 43.dp)
                            .clip(RoundedCornerShape(12))
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 140.dp, height = 53.dp)
                            .padding(top = 3.dp)
                    ) {
                        Text( // video name
                            text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                            fontFamily = montserrat_bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxSize(),
                            fontSize = 10.sp
                        )
                    }
                }
                Image( // play icon (note: make this a button)
                    painter = painterResource(R.drawable.play_circle_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 16.dp)
                        .size(45.dp)
                        .clip(RoundedCornerShape(75, 75, 75, 75))
                        .clickable {
                            navController.navigate(ScreenRoutes.VideoScreen.route)
                        },
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }
    )

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