package com.example.lofiapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.NonNull
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
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

    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    //val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var execute by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(true) }
    var showQueueH by remember { mutableStateOf(false) }
    var showQueueV by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val activity = context as Activity
    if (execute) { // initial screen orientation
        activity.requestedOrientation = remember { (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) }
        activity.requestedOrientation = remember { ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR }
        execute = !execute
    }
    var hori by remember { mutableStateOf(true) }

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

                    // Rotate Screen button
                    IconButton(
                        onClick = {
                            if (hori) {
                                activity.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                                activity.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                            } else {
                                activity.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                                activity.requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                            }
                            hori = !hori
                        },
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Image(
                            // back symbol
                            painter = painterResource(id = R.drawable.screen_rotation_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }

                    // Add Playlist button
                    IconButton(
                        onClick = { },
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
                            if (hori) {
                                showQueueH = true.apply { showControls = false }
                            } else {
                                showQueueV = true.apply { showControls = false }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    // System back button
                    BackHandler(
                        enabled = true,
                        onBack = { showQueueH = false.apply { showControls = true } })
                    // Back button
                    IconButton(onClick = { showQueueH = false.apply { showControls = true } }) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    // System back button
                    BackHandler(
                        enabled = true,
                        onBack = { showQueueV = false.apply { showControls = true } })
                    // Back button
                    Column() {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                showQueueV = false.apply {
                                    showControls = true
                                }
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
                                        fontFamily = montserrat_light,
                                        color = Color.White
                                    )
                                }
                            })
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

