package com.example.lofiapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.Button
import androidx.activity.compose.BackHandler
import androidx.annotation.NonNull
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.util.TimeUtils.*
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.lofiapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun VideoScreen(navController: NavController, video_id: String) {

    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    //val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as Activity
    var hori by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Black)
    systemUiController.setNavigationBarColor(color = Color.Black)

    var back by remember { mutableStateOf(true) }
    BackHandler(back, onBack = { back = false })
    if (!back) {
        navController.navigateUp().apply {
            systemUiController.setStatusBarColor(color = Color(0xFF24CAAC))
            systemUiController.setNavigationBarColor(color = Color(0xFF24CAAC))
        }
    }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = { },
        content = { padding ->
            // Diaplyed Videos
            Column(
                modifier = Modifier.fillMaxWidth(),
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





            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

                IconButton(onClick = { navController.navigateUp() }) {
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

                IconButton(
                    onClick = {
                        if (hori)
                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                        else
                            activity.requestedOrientation =
                                ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                        hori = !hori
                    },
                    modifier = Modifier.align(Alignment.BottomStart)) {
                    Image(
                        // back symbol
                        painter = painterResource(id = R.drawable.screen_rotation_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp),
                        colorFilter = ColorFilter.tint(color = Color.White),
                    )
                }

                IconButton(
                    onClick = {     },
                    modifier = Modifier.align(Alignment.TopEnd)) {
                    Image(
                        painter = painterResource(id = R.drawable.playlist_add_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp),
                        colorFilter = ColorFilter.tint(color = Color.White),
                    )
                }
                IconButton(
                    onClick = {     },
                    modifier = Modifier.align(Alignment.BottomEnd)) {
                    Image(
                        painter = painterResource(id = R.drawable.queue_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp),
                        colorFilter = ColorFilter.tint(color = Color.White),
                    )
                }
            }


        }
    )
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
            inflateCustomPlayerUi(R.layout.custom_ui_layout)
            activityLifecycle.lifecycle.addObserver(this)
            enableAutomaticInitialization = false
            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    val playPauseButton = this@apply.findViewById<Button>(R.id.skip_next_button)
                    youTubePlayer.cueVideo(videoId, 0f)


                    playPauseButton.setOnClickListener { view: View? ->
                        /*
                        if (isPlaying) youTubePlayer.pause() else youTubePlayer.play()
                        isPlaying = !isPlaying
                         */

                    }

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

