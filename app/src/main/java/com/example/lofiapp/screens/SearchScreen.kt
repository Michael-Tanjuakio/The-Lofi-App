package com.example.lofiapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.ui.theme.flamenco_regular
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.CompletableFuture.AsynchronousCompletionTask


@Composable
fun SearchScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name), color = Color.White, fontFamily = flamenco_regular, fontSize = 32.sp)
            },
            backgroundColor = Color(0xFF24CAAC)
        )
        Text(text = "Search Page")

        // video-id
        val video_id = "B0-ql2lTXRc"

        // Display video
        // YoutubeScreen(video_id, Modifier)
        val fullsize_path_img:String = "https://img.youtube.com/vi/$video_id/0.jpg" // Display video thumbnail
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            YoutubeScreen(video_id, Modifier)
            AsyncImage(
                model = fullsize_path_img,
                contentDescription = null
            )
        }


    }
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
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            }
        )
        view
    })
}