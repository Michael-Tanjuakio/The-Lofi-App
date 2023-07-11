package com.example.lofiapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.NonNull
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VideoScreen(navController: NavController, video_id: String) {

    // placeholder variables
    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    val playlistsList = listOf(
        "laura's playlist",
        "michael's playlist",
        "chris soundtrack",
        "jonathan album",
        "laura's playlist",
        "michael's playlist",
        "chris soundtrack",
        "jonathan album"
    )
    //val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var showControls by remember { mutableStateOf(true) }
    var showQueueH by remember { mutableStateOf(false) }
    var showQueueV by remember { mutableStateOf(false) }
    var showAddPlaylist by remember { mutableStateOf(false) }
    var showCreateNewPlaylist by remember { mutableStateOf(false) }
    var openNoTitleDialog by remember { mutableStateOf(false) } // no title dialog popup
    val context = LocalContext.current
    val activity = remember { context as Activity }
    val interactionSource = remember { MutableInteractionSource() }

    // initial screen orientation
    var execute by remember { mutableStateOf(true) }
    var hori by remember { mutableStateOf(true) }
    var dialogue by remember { mutableStateOf(false) }
    var lockOrientation by remember { mutableStateOf(true) }
    val configuration = LocalConfiguration.current

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Black)
    systemUiController.setNavigationBarColor(color = Color.Black)

    var back by remember { mutableStateOf(true) }
    BackHandler(back, onBack = { back = false }) // system back button
    if (!back) {
        navController.navigateUp()
    }

    class DemoOnTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (!dialogue)
                showControls = true
            return false
        }
    }

    val listener = DemoOnTouchListener()
    val view = LocalView.current
    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter { motionEvent -> // touch screen shows controls for 3 seconds
            listener.onTouch(view, motionEvent)
        }) {
        // Video Display
        Scaffold(
            modifier = Modifier,
            backgroundColor = Color.Black,
            topBar = { },
            content = { padding ->
                // Diaplyed Videos
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        YoutubeScreen(
                            video_id,
                            Modifier
                                .aspectRatio(16 / 9f)
                        )

                    }
                }
            })

        // Controls Display
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AnimatedVisibility( // Appears smoothly
                visible = showControls,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
                modifier = Modifier.align(Alignment.Center)
            ) {

                LaunchedEffect(showControls) {// Disappears after 3 seconds
                    delay(3.seconds).apply {
                        showControls = false
                    }
                }

                Box( // control buttons
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    // Back button
                    IconButton(onClick = { back = !back }) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.TopStart),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // rotation buttons
                    if (lockOrientation) { // locked orientation button

                        activity.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LOCKED // disable rotation

                        if (execute) { // initial screen orientation (landscape)
                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            Log.d("orientationScreen", "landscape")
                        }

                        Image(
                            // button icon
                            painter = painterResource(id = R.drawable.screen_lock_rotation_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.BottomStart)
                                .padding(start = 3.dp, bottom = 3.dp)
                                .clickable(
                                    onClick = {
                                        if (execute)
                                            execute = !execute // executes once
                                        lockOrientation = !lockOrientation
                                    }
                                ),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    } else { // rotate button

                        activity.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR // enable rotation

                        Image(
                            painter = painterResource(id = R.drawable.screen_rotation_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.BottomStart)
                                .padding(start = 3.dp, bottom = 3.dp)
                                .clickable(
                                    onClick = {
                                        lockOrientation = !lockOrientation
                                    }
                                ),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // Add Playlist button
                    IconButton(
                        onClick = {
                            dialogue = true
                            showAddPlaylist = true.apply {
                                showControls = false
                                dialogue = true
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.playlist_add_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // queue button
                    IconButton(
                        onClick = {
                            when (configuration.orientation) {
                                Configuration.ORIENTATION_LANDSCAPE -> {
                                    showQueueH = true.apply {
                                        showControls = false
                                        dialogue = true
                                    }
                                }
                                Configuration.ORIENTATION_PORTRAIT -> {
                                    showQueueV = true.apply {
                                        showControls = false
                                        dialogue = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.queue_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // Play previous button
                    IconButton(
                        onClick = {
                            navController.navigate(ScreenRoutes.VideoScreen.route)
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.skip_previous_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // Play next button
                    IconButton(
                        onClick = {
                            navController.navigate(ScreenRoutes.VideoScreen.route)
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.skip_next_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                }
            }


            // Display queue (horizontal)
            AnimatedVisibility(
                visible = showQueueH,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
                modifier = Modifier.align(Alignment.Center)
            ) {

                activity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LOCKED // disable rotation

                // System back button
                BackHandler(
                    enabled = true,
                    onBack = { showQueueH = false.apply { dialogue = false } }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                ) {

                    // Back button
                    IconButton(onClick = { showQueueH = false.apply { dialogue = false } }) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.TopStart),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // Queue display
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 13.dp, top = 6.dp)
                            .align(Alignment.Center)
                    ) {
                        items(items = list, itemContent = { item ->
                            Column(modifier = Modifier
                                .padding(start = 16.dp)
                                .clip(RoundedCornerShape(12, 12, 5, 5))
                                .clickable {
                                    navController.navigate(ScreenRoutes.VideoScreen.route) // link to video
                                }
                            ) { // Video Display
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
                                    fontFamily = montserrat_light,
                                    color = Color.White
                                )
                            }
                        })
                    }
                }
            }

            // Display queue (vertical)
            AnimatedVisibility(
                visible = showQueueV,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
                modifier = Modifier.align(Alignment.Center)
            ) {

                activity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LOCKED // disable rotation

                // System back button
                BackHandler(
                    enabled = true,
                    onBack = { showQueueV = false.apply { dialogue = false } }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                ) {

                    Column() {

                        // Back button
                        Box(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                showQueueV = false.apply { dialogue = false }
                            }) {
                                Image(
                                    // back symbol
                                    painter = painterResource(id = R.drawable.arrow_back_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .align(Alignment.TopStart),
                                    colorFilter = ColorFilter.tint(color = Color.White),
                                )
                            }
                        }

                        // Queue Display
                        LazyColumn(
                            modifier = Modifier
                                .padding(start = 0.dp, top = 5.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            items(items = list, itemContent = { item ->
                                Column(modifier = Modifier
                                    .padding(start = 0.dp)
                                    .clip(RoundedCornerShape(12, 12, 5, 5))
                                    .clickable {
                                        navController.navigate(ScreenRoutes.VideoScreen.route) // link to video
                                    }
                                ) { // Video Display
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
                                        fontFamily = montserrat_light,
                                        color = Color.White
                                    )
                                }
                            })
                        }
                    }
                }
            }

            // Display add playlist
            AnimatedVisibility(
                visible = showAddPlaylist,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
                modifier = Modifier.align(Alignment.Center)
            ) {

                // System back button
                BackHandler(
                    enabled = true,
                    onBack = { showAddPlaylist = false.apply { dialogue = false } })

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                ) {

                    // Back button
                    IconButton(onClick = { showAddPlaylist = false.apply { dialogue = false } }) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.TopStart),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    Box( // white box
                        modifier = Modifier
                            .clip(RoundedCornerShape(6, 6, 0, 0))
                            .background(Color.White)
                            .fillMaxHeight(0.65f)
                            .fillMaxWidth(0.80f)
                            .align(Alignment.BottomCenter)
                            .fillMaxHeight(0.5f)
                    ) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            Box( // green box
                                modifier = Modifier
                                    .background(Color(0xFF24CAAC))
                                    .fillMaxHeight(0.18f)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 20.dp)
                                ) {
                                    Text( // Title Text
                                        text = "Add to Playlist",
                                        fontSize = 23.sp,
                                        fontFamily = montserrat_bold,
                                        color = Color.White
                                    )
                                }
                            }
                            // Playlist list
                            Box(
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    top = 10.dp,
                                    end = 20.dp
                                )
                            ) {
                                Column() {
                                    Row(modifier = Modifier.clickable() {
                                        showCreateNewPlaylist = true
                                    }) {
                                        Text( // Title Text
                                            text = "Create New Playlist",
                                            fontSize = 20.sp,
                                            fontFamily = montserrat_light,
                                            color = Color.Black
                                        )

                                        Spacer(Modifier.weight(1f))

                                        Image( // add new icon
                                            painter = painterResource(id = R.drawable.add_new_icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(35.dp)
                                                .align(Alignment.CenterVertically)
                                                .clickable(onClick = {
                                                    showCreateNewPlaylist = true
                                                }),
                                            colorFilter = ColorFilter.tint(color = Color.Gray)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(7.dp))

                                    Box( // line seperater
                                        modifier = Modifier
                                            .background(Color(0xFF828282))
                                            .fillMaxWidth(1f)
                                            .height(2.dp)
                                    )

                                    Spacer(modifier = Modifier.height(7.dp))

                                    Box(modifier = Modifier.fillMaxHeight(.75f)) {
                                        LazyColumn() {
                                            items(items = playlistsList, itemContent = { item ->

                                                Row(modifier = Modifier.clickable() {
                                                    // ADD VIDEO TO PLAYLIST HERE
                                                }) {
                                                    Text( // Playlist Title
                                                        text = item,
                                                        fontSize = 20.sp,
                                                        fontFamily = montserrat_light,
                                                        color = Color.Black
                                                    )

                                                    Spacer(Modifier.weight(1f))

                                                    Image( // add to playlist icon
                                                        painter = painterResource(id = R.drawable.check_box_outline_blank_icon),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(35.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .clickable() {
                                                                // ADD VIDEO TO PLAYLIST HERE
                                                            },
                                                        colorFilter = ColorFilter.tint(color = Color.Gray)
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(5.dp))

                                            })
                                        }
                                    }

                                    Button(onClick = {
                                        showAddPlaylist = false.apply { dialogue = false }
                                    },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3392EA)),
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = "Confirm", color = Color.White, fontFamily = montserrat_bold)
                                    }


                                }
                            }
                        }
                    }


                }
            }

            // Display Create new playlist
            AnimatedVisibility(
                visible = showCreateNewPlaylist,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
                modifier = Modifier.align(Alignment.Center)
            ) {

                var text by remember { mutableStateOf("") } // editable text

                // System back button
                BackHandler(
                    enabled = true,
                    onBack = { showCreateNewPlaylist = false })

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                ) {

                    // Back button
                    IconButton(onClick = { showCreateNewPlaylist = false }) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.TopStart),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    Box( // white box
                        modifier = Modifier
                            .clip(RoundedCornerShape(6, 6, 0, 0))
                            .background(Color.White)
                            .fillMaxHeight(0.65f)
                            .fillMaxWidth(0.80f)
                            .align(Alignment.BottomCenter)
                            .fillMaxHeight(0.5f)
                    ) {
                        Column() {
                            Box( // green box
                                modifier = Modifier
                                    .background(Color(0xFF24CAAC))
                                    .fillMaxHeight(0.18f)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 20.dp)
                                ) {
                                    Text( // Title Text
                                        text = "Create New Playlist",
                                        fontSize = 23.sp,
                                        fontFamily = montserrat_bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    top = 10.dp,
                                    end = 20.dp
                                )
                            ) {
                                Column() {
                                    Spacer(modifier = Modifier.height(11.dp))
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
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Black,
                                                fontFamily = montserrat_light
                                            ),
                                            singleLine = true,
                                            cursorBrush = SolidColor(Color.Black),
                                            modifier = Modifier
                                                .fillMaxWidth(1f)
                                                .height(30.dp),
                                            decorationBox = { innerTextField ->
                                                Row {
                                                    if (text.isEmpty()) { // if there is no text
                                                        Text(
                                                            // search placeholder
                                                            text = "Insert Playlist Name",
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Normal,
                                                            color = Color.Black.copy(alpha = 0.5f),
                                                            fontFamily = montserrat_light,
                                                            modifier = Modifier.align(Alignment.CenterVertically),
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            },
                                        )
                                    }

                                    Box( // line seperater
                                        modifier = Modifier
                                            .background(Color(0xFF828282))
                                            .fillMaxWidth(1f)
                                            .height(2.dp)
                                    )

                                    Spacer(modifier = Modifier.height(50.dp))

                                    Button(onClick = {
                                        if (!text.isEmpty())
                                            showCreateNewPlaylist = false
                                        else
                                            openNoTitleDialog = true
                                    },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3392EA)),
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = "Confirm", color = Color.White, fontFamily = montserrat_bold)
                                    }

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
    }
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {
    val context = LocalContext.current
    val activityLifecycle: LifecycleOwner = LocalLifecycleOwner.current
    val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
    var isPlaying by remember { mutableStateOf(false) }

    val iFramePlayerOptions: IFramePlayerOptions = IFramePlayerOptions.Builder()
        .controls(1)
        .fullscreen(1) // enable full screen button
        .build()

    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            // Add custom layout
            //inflateCustomPlayerUi(R.layout.custom_ui_layout)
            activityLifecycle.lifecycle.addObserver(this)
            enableAutomaticInitialization = false
            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    //val playPauseButton = this@apply.findViewById<Button>(R.id.skip_next_button)
                    youTubePlayer.cueVideo(videoId, 0f)

                    /*
                                        playPauseButton.setOnClickListener { view: View? ->
                                            /*
                                            if (isPlaying) youTubePlayer.pause() else youTubePlayer.play()
                                            isPlaying = !isPlaying
                                             */

                                        }

                     */

                }
            }, options)
        }
    }

    AndroidView(
        {
            youtubePlayer
        }, modifier = modifier
    )


}

