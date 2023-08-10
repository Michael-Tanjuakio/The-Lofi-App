package com.example.lofiapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    navController: NavController,
    playlist_name: String,
    bottomBar_pic: String,
    bottomBar_title: String
) {

    // Navigate back function
    fun navBack() {
        // Save data when navigating back
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("new_bottomBar_pic", bottomBar_pic)
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("new_bottomBar_title", bottomBar_title)

        // navigate back
        navController
            .popBackStack()
    }

    // System Back handler
    BackHandler(true, onBack = {
        navBack()
    })

    // Locked Vertical Orientation
    val context = LocalContext.current
    val activity = remember { context as Activity }
    activity.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    // System bar colors
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(0xFF24CAAC))         // System top bar color
    systemUiController.setNavigationBarColor(color = Color(0xFF24CAAC))     // System bottom bar color

    // leftover testing data
    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example

    // delete dialog popup
    var openDialog by remember { mutableStateOf(false) }

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
                                navController.navigate(
                                    "playlist_screen/" +
                                            "playlist_name" +
                                            "?bottomBar_pic=" + bottomBar_pic +
                                            "&bottomBar_title=" +
                                            URLEncoder.encode(
                                                bottomBar_title,
                                                StandardCharsets.UTF_8.toString()
                                            )
                                )
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
                    IconButton(onClick = { navBack() }) {
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
            LazyColumn(modifier = Modifier
                .padding(top = 20.dp, bottom = 55.dp)
                .fillMaxWidth()) {
                items(items = list, itemContent = { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(12, 12, 5, 5))
                            .align(CenterHorizontally)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(start = 0.dp, bottom = 10.dp)
                                    .clip(RoundedCornerShape(12, 12, 5, 5))
                                    .clickable {
                                        navController.navigate(
                                            "video_screen/" +
                                                    video_id +
                                                    "/" +
                                                    URLEncoder.encode( // encode to pass "&" character
                                                        "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                                                        StandardCharsets.UTF_8.toString()
                                                    ))
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
            if (!bottomBar_title.equals("")) {
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomNavigation(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12))
                                .clickable {
                                    navController.navigate(
                                        "video_screen/"
                                                + bottomBar_pic + "/"
                                                + URLEncoder.encode( // encode to pass "&" character
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    )
                                }
                                .fillMaxWidth(.95f),
                            backgroundColor = Color(0xFF3392EA),
                        ) {
                            Log.d(
                                "video playing",
                                "playlist_screen: playing: " + bottomBar_title + " " + bottomBar_pic
                            )
                            Row(modifier = Modifier.fillMaxHeight()) { // wrap in row to avoid default spacing
                                Spacer(modifier = Modifier.width(16.dp))
                                AsyncImage( // video thumbnail
                                    model = "https://img.youtube.com/vi/$bottomBar_pic/maxres2.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 65.dp, height = 43.dp)
                                        .clip(RoundedCornerShape(12))
                                        .align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(.90f)
                                        .fillMaxHeight(.95f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text( // video name (decodes title)
                                        text = URLDecoder.decode(
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        ),
                                        fontFamily = montserrat_bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .basicMarquee(),
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                }
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





