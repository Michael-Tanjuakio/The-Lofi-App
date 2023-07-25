package com.example.lofiapp.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

@Composable
fun CreatePlaylistScreen(navController: NavController) {

    // Title
    var text by remember { mutableStateOf("") }
    var openNoTitleDialog by remember { mutableStateOf(false) } // no title dialog popup

    // Search videos to add
    val list = remember { mutableStateListOf<youtubeVideo?>() }
    val videos = FirebaseDatabase.getInstance().getReference("videos")
    fun searchByName(name: String) {
        videos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (name.isEmpty())
                    list.clear()
                else {
                    if (snapshot.exists()) {
                        list.clear()
                        Log.d("Found video", "CLEAR LIST")
                        for (i in snapshot.children) {
                            val video = i.getValue<youtubeVideo>()
                            if (video?.videoTitle.toString().lowercase()
                                    .contains(name.lowercase())
                            ) {
                                list.add(video)
                                Log.d("Found video", "" + video?.videoTitle.toString())
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    var searchVideoText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier,
        backgroundColor = Color.White,
        topBar = {
            // Top Bar Composable
            TopAppBar(
                title = { },
                backgroundColor = Color(0xFF24CAAC),
                actions = {
                    IconButton(onClick = {
                        if (!text.isEmpty())
                            navController.navigate(ScreenRoutes.PlaylistScreen.route)
                        else
                            openNoTitleDialog = true
                    }) {
                        Icon( // Search icon
                            painter = painterResource(id = R.drawable.done_icon),
                            contentDescription = stringResource(MenuAction.Search.label),
                            tint = Color.White,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                }
            )

            Row {
                Box(modifier = Modifier.padding(start = 5.dp, top = 3.dp)) { // Back button
                    IconButton(onClick = {
                        navController.navigateUp()
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

            Column(modifier = Modifier.fillMaxSize(1f)) {
                Box(modifier = Modifier.fillMaxSize(0.45f)) {
                    Text("Foo", modifier = Modifier.padding())
                }
                Divider(startIndent = 0.dp, thickness = 1.dp, color = Color.Black)
                Box(modifier = Modifier.fillMaxSize(1f)) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { }, backgroundColor = Color(0xFF24CAAC))
                            // Top App Bar Components (title padding is off)
                            Row {
                                Box(
                                    modifier = Modifier.padding(
                                        start = 5.dp,
                                        top = 3.dp
                                    )
                                ) {
                                    Canvas( // Search box background
                                        modifier = Modifier
                                            .offset(x = (5).dp, y = (7).dp)
                                            .height(40.dp)
                                            .fillMaxWidth(.95f),
                                        onDraw = {
                                            drawRoundRect(
                                                // Search box shape
                                                color = Color(0xFFECECEC),
                                                cornerRadius = CornerRadius(
                                                    x = 36.dp.toPx(),
                                                    36.dp.toPx()
                                                ),
                                            )
                                        }
                                    )

                                    val customTextSelectionColors =
                                        TextSelectionColors( // selection text color
                                            handleColor = Color(0xFF24CAAC),
                                            backgroundColor = Color(0xFF24CAAC).copy(alpha = 0.4f)
                                        )
                                    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                                        BasicTextField(
                                            // textfield
                                            value = text,
                                            onValueChange = { newText ->
                                                text = newText
                                                searchByName(text)
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Black,
                                                fontFamily = montserrat_light
                                            ),
                                            singleLine = true,
                                            cursorBrush = SolidColor(Color.Black),
                                            modifier = Modifier
                                                .padding(top = 14.dp, start = 20.dp)
                                                .height(30.dp)
                                                .fillMaxWidth(.95f),
                                            decorationBox = { innerTextField ->
                                                Row {
                                                    if (text.isEmpty()) { // if there is no text
                                                        Icon( // search icon
                                                            imageVector = MenuAction.Search.icon,
                                                            contentDescription = stringResource(
                                                                MenuAction.Search.label
                                                            ),
                                                            tint = Color.Black.copy(alpha = 0.5f),
                                                            modifier = Modifier
                                                                .size(30.dp)
                                                                .padding(bottom = 2.dp)
                                                        )
                                                        Text(
                                                            // search placeholder
                                                            text = "Search",
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Normal,
                                                            color = Color.Black.copy(alpha = 0.5f),
                                                            fontFamily = flamenco_regular,
                                                            modifier = Modifier.padding(
                                                                top = 2.dp,
                                                                start = 7.dp
                                                            ),
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
                            LazyColumn(modifier = Modifier.padding(top = 20.dp, bottom = 50.dp)) {
                                items(items = list, itemContent = { item ->
                                    Log.d("Found video", "Show list")
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 20.dp)
                                    ) { // Video Display
                                        Box(modifier = Modifier
                                            .clip(RoundedCornerShape(12, 12, 5, 5))
                                            .align(CenterHorizontally)
                                            .clickable {
                                                navController.navigate("video_screen/" + item?.videoID.toString())
                                            }
                                        ) {
                                            Row() {
                                                // add to playlist button
                                                IconButton(onClick = {

                                                },
                                                modifier = Modifier.align(Alignment.CenterVertically)) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.add_new_icon),
                                                        contentDescription = null,
                                                        colorFilter = ColorFilter.tint(color = Color.Black),
                                                        modifier = Modifier
                                                            .size(35.dp)
                                                            .align(Alignment.CenterVertically)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                AsyncImage( // Video thumbnail
                                                    model = "https://img.youtube.com/vi/" + item?.videoID.toString() + "/maxres2.jpg",
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .size(width = 140.dp, height = 87.dp)
                                                        .clip(RoundedCornerShape(12))
                                                )
                                                Text( // Video name
                                                    text = item?.videoTitle.toString(),
                                                    maxLines = 4,
                                                    modifier = Modifier
                                                        .padding(start = 8.dp)
                                                        .width(90.dp)
                                                        .height(80.dp)
                                                        .wrapContentHeight(),
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
                                        model = null,
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
            }

        },
        bottomBar = {}
    )

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
                                    fontFamily = montserrat_bold,
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

}