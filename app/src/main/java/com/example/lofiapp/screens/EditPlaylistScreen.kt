package com.example.lofiapp.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.component1
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.Collections
import java.util.Collections.list


@Composable
fun EditPlaylistScreen(navController: NavController) {

    //val list = remember { mutableStateListOf<String>("1", "2", "3") } // placeholder
    var list = remember { mutableStateOf(listOf("abc 1", "letter 2", "sing 3", "thai 4","five 5", "big 6",)) }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        list.value = list.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example

    var text by remember { mutableStateOf("Playlist of the Century") }

    var openDialog by remember { mutableStateOf(false) } // delete dialog popup
    var openNoTitleDialog by remember { mutableStateOf(false) } // no title dialog popup
    var openDeleteVideoDialog by remember { mutableStateOf(false) } // delete video dialog popup
    var deleteVideo by remember { mutableStateOf(false) } // delete video dialog popup
    var index by remember { mutableStateOf(0) } // delete video dialog popup


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                backgroundColor = Color(0xFF24CAAC),
                actions = {
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
                    IconButton(onClick = {
                        if (!text.isEmpty())
                            navController.navigate(ScreenRoutes.PlaylistScreen.route)
                        else
                            openNoTitleDialog = true
                    }) {
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
                    val customTextSelectionColors = TextSelectionColors( // selection text color
                        handleColor = Color(0xFF24CAAC),
                        backgroundColor = Color(0xFF24CAAC).copy(alpha = 0.4f)
                    )
                    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                        BasicTextField(
                            // textfield
                            value = text,
                            onValueChange = { newText -> text = newText },
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontFamily = montserrat_bold
                            ),
                            cursorBrush = SolidColor(Color.Black),
                            modifier = Modifier
                                .padding(start = 5.dp, top = 2.dp)
                                .size(width = 150.dp, height = 50.dp),
                            maxLines = 2,
                            decorationBox = { innerTextField ->
                                Row {
                                    if (text.isEmpty()) { // if there is no text
                                        Text(
                                            // search placeholder
                                            text = "Please insert a playlist name",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black.copy(alpha = 0.5f),
                                            fontFamily = flamenco_regular,
                                            modifier = Modifier.padding(top = 2.dp, start = 7.dp),
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                        )
                    }
                }

            }
        },
        content = { padding ->

            // Searched Videos Display (vertical. scroll)
            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 55.dp)
                    .reorderable(state)
                    .detectReorderAfterLongPress(state),
                state = state.listState,
            ) {
                items(list.value, {it}) { item ->
                    ReorderableItem(state, key = item) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12, 12, 5, 5))
                                    .align(CenterHorizontally)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 0.dp, bottom = 10.dp)
                                        .clip(RoundedCornerShape(12, 12, 5, 5))
                                        .background(Color.Transparent)
                                ) { // Video Display
                                    // edit list button
                                    Image(
                                        painter = painterResource(id = R.drawable.reorder_icon),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(color = Color.Black),
                                        modifier = Modifier
                                            .size(35.dp)
                                            .align(CenterVertically)
                                            .padding(end = 10.dp)
                                    )
                                    AsyncImage( // Video thumbnail
                                        model = fullsize_path_img,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(width = 140.dp, height = 87.dp)
                                            .clip(RoundedCornerShape(12))
                                            .align(CenterVertically)
                                            .background(Color.Transparent)
                                    )
                                    Text( // Video name
                                        //  "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                                        text = "video " + item,
                                        maxLines = 4,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .width(90.dp)
                                            .height(80.dp)
                                            .align(CenterVertically),
                                        //.border(border = BorderStroke(2.dp, Color.Red)),
                                        fontSize = 13.sp,
                                        fontFamily = montserrat_light
                                    )
                                    // delete video button
                                    Image(
                                        painter = painterResource(id = R.drawable.delete_video_icon),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(color = Color.Black),
                                        modifier = Modifier
                                            .size(28.dp)
                                            .align(CenterVertically)
                                            .clip(RoundedCornerShape(12))
                                            .clickable {
                                                index = list.component1().indexOf(item)
                                                Log.d(
                                                    "deleteVideo",
                                                    "finding " + index
                                                )
                                                openDeleteVideoDialog = true
                                            }
                                    )
                                    if (deleteVideo) { // deletes video

                                        Log.d(
                                            "deleteVideo",
                                            "removed index " + index + " " + list.value[index] + "\n" + list.value.subList(0,index) + " " + list.value.subList(index+1,list.value.size)
                                        )
                                        list.value = list.value.subList(0,index) + list.value.subList(index+1,list.value.size)
                                        deleteVideo = false
                                    }

                                }
                            }
                        }
                    }
                }
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
                                fontSize = 26.sp
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
                                fontSize = 26.sp
                            )
                        }


                    }
                }
            }
        }
    }

    if (openNoTitleDialog) {
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = { openNoTitleDialog = false }) {
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
                                text = "Please enter a name for this playlist",
                                fontSize = 22.sp,
                                fontFamily = montserrat_bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Ok button
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                onClick = { openNoTitleDialog = false },
                                modifier = Modifier
                                    .padding(top = 150.dp, start = 0.dp)
                                    .size(100.dp, 60.dp)
                                    .clip(RoundedCornerShape(100)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = Color(
                                        0xFF3392EA
                                    )
                                )
                            ) {
                                Text(
                                    text = "OK",
                                    fontFamily = montserrat_light,
                                    color = Color.White,
                                    fontSize = 26.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (openDeleteVideoDialog) {
        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = { openDeleteVideoDialog = false }) {
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
                                .size(width = 300.dp, height = 150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Are you sure you want to delete this video from your playlist?",
                                fontSize = 22.sp,
                                fontFamily = montserrat_bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        // yes button
                        Button(
                            onClick = {
                                deleteVideo = true
                                openDeleteVideoDialog = false
                            },
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
                                fontSize = 26.sp
                            )
                        }

                        // no button
                        Button(
                            onClick = { openDeleteVideoDialog = false },
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
                                fontSize = 26.sp
                            )
                        }


                    }
                }
            }
        }
    }

    // empty title back button function
    if (text.isEmpty()) {
        BackHandler(true, onBack = {
            openNoTitleDialog = true
        })
    }


}










