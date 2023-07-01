package com.example.lofiapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun VideoScreen(navController: NavController, video_id: String) {

    val list = listOf("1","1","1","1", "1","1","1","1") // placeholder
    //val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img = "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { }, backgroundColor = Color(0xFF24CAAC))
            // Top App Bar Components
            Row {
                Box(modifier = Modifier.padding(start = 5.dp, top = 3.dp)) { // Back button
                    IconButton(onClick = { navController.navigate(ScreenRoutes.HomeScreen.route) }) {
                        Image( // back symbol
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            colorFilter = ColorFilter.tint(color = Color.White),
                        )
                    }
                }
            }
        },
        content = { padding ->
            // Diaplyed Videos
            Column(modifier = Modifier.padding()) {
                YoutubeScreen(video_id, Modifier)
            }
        }
    )
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {
    val ctx = LocalContext.current
    AndroidView(factory = {
        var view = YouTubePlayerView(it)
        val fragment = view.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        )
        view
    })
}

