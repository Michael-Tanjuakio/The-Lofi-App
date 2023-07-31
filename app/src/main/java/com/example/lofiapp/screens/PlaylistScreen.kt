package com.example.lofiapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light


@Composable
fun PlaylistScreen(navController: NavController, playlist_name: String) {

    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example

    var openDialog by remember { mutableStateOf(false) } // delete dialog popup

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                backgroundColor = Color(0xFF24CAAC),
                actions = {
                    // edit button
                    Image(
                        painter = painterResource(id = R.drawable.edit_note_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                navController.navigate(ScreenRoutes.EditPlaylistScreen.route)
                            }
                    )
                    // padding
                    Canvas(modifier = Modifier.size(size = 11.dp)) {
                        drawRect(
                            color = Color(
                                0xFF24CAAC
                            )
                        )
                    }
                    // delete button
                    Image(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                openDialog = true
                            }
                    )
                    // padding
                    Canvas(modifier = Modifier.size(size = 9.dp)) {
                        drawRect(
                            color = Color(
                                0xFF24CAAC
                            )
                        )
                    }
                }
            )
            // Top App Bar Components
            Row {
                Box(modifier = Modifier.padding(start = 5.dp, top = 3.dp)) { // Back button
                    IconButton(onClick = { navController.navigateUp() }) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = playlist_name,
                        fontFamily = montserrat_bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 5.dp, top = 2.dp)
                            .size(width = 150.dp, height = 50.dp),
                        fontSize = 15.sp
                        //.border(border = BorderStroke(2.dp, Color.Red)),
                    )
                }

            }
        },
        content = { padding ->
            // Searched Videos Display (vertical. scroll)
            LazyColumn(modifier = Modifier.padding(top = 20.dp, bottom = 55.dp)) {
                items(items = list, itemContent = { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(12, 12, 5, 5))
                            .align(CenterHorizontally)
                            .clickable {
                                navController.navigate(ScreenRoutes.VideoScreen.route)
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(start = 0.dp, bottom = 10.dp)
                                    .clip(RoundedCornerShape(12, 12, 5, 5))
                                    .clickable {
                                        navController.navigate(ScreenRoutes.VideoScreen.route)
                                    }
                            ) { // Video Display
                                AsyncImage( // Video thumbnail
                                    model = fullsize_path_img,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 140.dp, height = 87.dp)
                                        .clip(RoundedCornerShape(12))
                                        .align(CenterVertically)
                                        .clickable {
                                            navController.navigate(ScreenRoutes.VideoScreen.route)
                                        }
                                )
                                Text( // Video name
                                    text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                                    maxLines = 4,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .width(130.dp)
                                        .height(80.dp)
                                        .align(CenterVertically),
                                    //.border(border = BorderStroke(2.dp, Color.Red)),
                                    fontSize = 13.sp,
                                    fontFamily = montserrat_light
                                )
                            }
                        }
                    }
                })
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

    // Delete dialog box
    if (openDialog) {
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = { openDialog = false }) {
            Surface(shape = RoundedCornerShape(size = 12.dp)) {
                Row() {
                    Box(contentAlignment = Alignment.Center) {

                        // white dialog box
                        Canvas(
                            modifier = Modifier
                                .size(size = 300.dp)
                                .clip(RoundedCornerShape(12))
                                .align(Alignment.Center)
                        ) {
                            drawRect(color = Color.White)
                        }

                        // dialog text
                        Box(
                            modifier = Modifier
                                .padding(bottom = 110.dp)
                                .size(width = 300.dp, height = 120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Are you sure you want to delete this playlist?",
                                fontSize = 22.sp,
                                fontFamily = montserrat_bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // yes button
                        Button(
                            onClick = { navController.navigate(ScreenRoutes.HomeScreen.route) },
                            modifier = Modifier
                                .padding(top = 150.dp, end = 150.dp)
                                .size(100.dp, 60.dp)
                                .clip(RoundedCornerShape(100)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color(
                                    0xFF3392EA
                                )
                            )
                        ) {
                            Text(
                                text = "Yes",
                                fontFamily = montserrat_light,
                                color = Color.White,
                                fontSize = 24.sp
                            )
                        }

                        // no button
                        Button(
                            onClick = { openDialog = false },
                            modifier = Modifier
                                .padding(top = 150.dp, start = 150.dp)
                                .size(100.dp, 60.dp)
                                .clip(RoundedCornerShape(100)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color(
                                    0xFF3392EA
                                )
                            )
                        ) {
                            Text(
                                text = "No",
                                fontFamily = montserrat_light,
                                color = Color.White,
                                fontSize = 24.sp
                            )
                        }


                    }
                }
            }
        }
    }
}





