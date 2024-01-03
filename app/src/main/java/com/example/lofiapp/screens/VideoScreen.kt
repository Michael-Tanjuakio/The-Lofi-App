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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VideoScreen(
    navController: NavController,
    bottomBar_pic: String,
    bottomBar_title: String,
    playlist: single_playlist,
    playlist_index: String
) {

    Log.d("new_bp",
        "playlist = " + playlist.playlistTitle +
                "\nplaylist_index = " + playlist_index +
                "\nplaylist size = " + playlist.videoList.size
    )

    // Popups
    var showControls by remember { mutableStateOf(true) }
    var openNoTitleDialog by remember { mutableStateOf(false) } // no title dialog popup
    val context = LocalContext.current
    val activity = remember { context as Activity }
    val interactionSource = remember { MutableInteractionSource() }

    // initial screen orientation
    var execute by remember { mutableStateOf(true) }
    var dialogue by remember { mutableStateOf(false) }
    var lockOrientation by remember { mutableStateOf(true) }

    // System bar colors
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Black)
    systemUiController.setNavigationBarColor(color = Color.Black)

    // Playlist boolean
    var skipNext by remember { mutableStateOf(false) }

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
        if ((playlist_index.equals("-1"))) // home/search screen
            navController.popBackStack()
        else // playlist screen
            navController.navigate(
                "playlist_screen" +
                        "/" + playlist.playlistID +
                        "?new_playlist=" + false +
                        "&bottomBar_pic=" + bottomBar_pic +
                        "&bottomBar_title=" +
                        URLEncoder.encode(
                            bottomBar_title,
                            StandardCharsets.UTF_8.toString()
                        )
            )
    }

    // System Back handler
    BackHandler(true, onBack = {
        navBack()
    })

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {  // touch screen shows controls for 3 seconds
            if (!dialogue)
                showControls = true

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

                        skipNext = YoutubeScreen(
                            bottomBar_pic,
                            Modifier
                                .aspectRatio(16 / 9f)
                        )

                        if (skipNext)
                            LaunchedEffect(skipNext) {// avoids crash
                                if (!(playlist_index.toInt() == playlist.videoList.size - 1))
                                    navController.navigate(
                                        "video_screen"
                                                + "?bottomBar_pic=" + playlist.videoList[playlist_index.toInt()+1].videoID
                                                + "&bottomBar_title=" +
                                                URLEncoder.encode( // encode to pass "&" and "/" characters
                                                    playlist.videoList[playlist_index.toInt()+1].videoTitle,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                + "&playlist_id=" + playlist.playlistID
                                                + "&playlist_index=" + (playlist_index.toInt() + 1)
                                    )
                            }


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
                        .fillMaxSize()
                ) {

                    // Back button
                    IconButton(onClick = {
                        navBack()
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
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
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
                                    .size(40.dp)
                                    .padding(top = 7.dp)
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
                                    .clickable(
                                        onClick = {
                                            lockOrientation = !lockOrientation
                                        }
                                    ),
                                colorFilter = ColorFilter.tint(color = Color.White),
                            )
                        }
                    }

                    if(!(playlist_index.toInt() == -1)) {

                        // Play previous button
                        if (!(playlist_index.equals("0"))) {
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        "video_screen"
                                                + "?bottomBar_pic=" + playlist.videoList[playlist_index.toInt()-1].videoID
                                                + "&bottomBar_title=" +
                                                URLEncoder.encode( // encode to pass "&" and "/" characters
                                                    playlist.videoList[playlist_index.toInt()-1].videoTitle,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                + "&playlist_id=" + playlist.playlistID
                                                + "&playlist_index=" + (playlist_index.toInt() - 1)
                                    )
                                },
                                modifier = Modifier.align(Alignment.BottomStart)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.skip_previous_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(35.dp),
                                    colorFilter = ColorFilter.tint(color = Color.White),
                                )
                            }
                        }

                        // Play next button
                        if (!(playlist_index.toInt() == playlist.videoList.size - 1))
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    "video_screen"
                                            + "?bottomBar_pic=" + playlist.videoList[playlist_index.toInt()+1].videoID
                                            + "&bottomBar_title=" +
                                            URLEncoder.encode( // encode to pass "&" and "/" characters
                                                playlist.videoList[playlist_index.toInt()+1].videoTitle,
                                                StandardCharsets.UTF_8.toString()
                                            )
                                            + "&playlist_id=" + playlist.playlistID
                                            + "&playlist_index=" + (playlist_index.toInt() + 1)
                                )
                            },
                            modifier = Modifier.align(Alignment.BottomEnd)
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
): Boolean {
    val context = LocalContext.current
    val activityLifecycle: LifecycleOwner = LocalLifecycleOwner.current
    val options: IFramePlayerOptions =
        IFramePlayerOptions.Builder().controls(0).build() // Creates embeded video
    val tracker = YouTubePlayerTracker()
    var state_ by remember { mutableStateOf("") }

    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            activityLifecycle.lifecycle.addObserver(this)
            enableAutomaticInitialization = false
            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)

                    youTubePlayer.addListener(tracker)
                    Log.d("CurrentSecond", "" + (tracker.videoDuration))
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    state_ = state.toString()
                }

            }, options)
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
    ) {
        /*
            Text(
                text = state_,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
                    */

    }

    AndroidView(
        {
            youtubePlayer
        }, modifier = modifier
    )

    if (state_ == "ENDED")
        return true

    return false
}

