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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(navController: NavController, bottomBar_pic: String, bottomBar_title: String) {

    // Search textbox editable text
    var text by remember { mutableStateOf("") }

    // Locked Vertical Orientation
    val context = LocalContext.current
    val activity = remember { context as Activity }
    activity.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

    // List of videos found in search
    val list = remember { mutableStateListOf<youtubeVideo?>() }
    val videos = FirebaseDatabase.getInstance().getReference("videos")

    // Search function
    fun searchByName(name: String) {
        videos.addValueEventListener(object :
            ValueEventListener { // goes through the list of videos in database
            override fun onDataChange(snapshot: DataSnapshot) {
                if (name.isEmpty())
                    list.clear() // clear list if there are no characters
                else {
                    if (snapshot.exists()) {
                        list.clear()
                        Log.d("Found video", "CLEAR LIST")
                        for (i in snapshot.children) { // goes through all videos in database
                            val video = i.getValue<youtubeVideo>() // searched video
                            if (video?.videoTitle.toString()
                                    .lowercase() // if video title matches in letters add video
                                    .contains(name.lowercase()) // lowercases all letters to make match more accessible
                            ) {
                                list.add(video) // add video to searched videos list
                                Log.d("Found video", "" + video?.videoTitle.toString())
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Scaffold (Top bar, Content, Bottom Bar)
    Scaffold(
        topBar = {
            TopAppBar(title = { }, backgroundColor = Color(0xFF24CAAC))
            // Top App Bar Components (title padding is off)
            Row {
                Box(modifier = Modifier.padding(start = 5.dp, top = 3.dp)) { // Back button
                    IconButton(onClick = {
                        navBack()
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
                Canvas( // Search box background
                    modifier = Modifier
                        .offset(x = (5).dp, y = (7).dp)
                        .fillMaxWidth(.9f)
                ) {
                    drawRoundRect(
                        // Search box shape
                        color = Color(0xFFECECEC),
                        size = Size(width = size.width, height = 40.dp.toPx()),
                        cornerRadius = CornerRadius(x = 36.dp.toPx(), 36.dp.toPx()),
                    )
                }
            }
            Row() {
                Spacer(modifier = Modifier.width(55.dp))

                val customTextSelectionColors = TextSelectionColors( // selection text color
                    handleColor = Color(0xFF24CAAC),
                    backgroundColor = Color(0xFF24CAAC).copy(alpha = 0.4f)
                )
                // Textfield
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    BasicTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                            searchByName(text) // call search function
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
                            .fillMaxWidth(.88f)
                            .height(30.dp),
                        decorationBox = { innerTextField ->
                            Row {
                                if (text.isEmpty()) { // if there is no text
                                    Icon( // search icon
                                        imageVector = MenuAction.Search.icon,
                                        contentDescription = stringResource(MenuAction.Search.label),
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
                                        modifier = Modifier.padding(top = 2.dp, start = 7.dp),
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
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
                            .clickable(
                                // navigates to video screen
                                onClick = {
                                    navController.navigate(  // navigates to video screen
                                        "video_screen/"
                                                + item?.videoID.toString()
                                                + "/"
                                                + URLEncoder.encode( // encode to pass "&" character
                                            item?.videoTitle.toString(),
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    )
                                }
                            )
                        ) {
                            Column() {
                                AsyncImage( // Video thumbnail
                                    model = "https://img.youtube.com/vi/" + item?.videoID.toString() + "/maxres2.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 270.dp, height = 140.dp)
                                        .clip(RoundedCornerShape(12))
                                )
                                Text( // Video name
                                    text = item?.videoTitle.toString(),
                                    maxLines = 4,
                                    modifier = Modifier
                                        .padding(start = 0.dp, top = 2.dp)
                                        .width(270.dp)
                                        .wrapContentHeight(),
                                    fontSize = 16.sp,
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
                                "Search_screen: playing: " + bottomBar_title + " " + bottomBar_pic
                            )
                            Row(modifier = Modifier.fillMaxHeight()) { // wrap in row to avoid default spacing
                                Spacer(modifier = Modifier.width(16.dp))
                                AsyncImage( // video thumbnail
                                    model = "https://img.youtube.com/vi/" + bottomBar_pic + "/maxres2.jpg",
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
}

