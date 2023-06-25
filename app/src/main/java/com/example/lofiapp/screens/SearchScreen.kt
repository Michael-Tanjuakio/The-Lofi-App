package com.example.lofiapp.screens

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener


@Composable
fun SearchScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {         },
            backgroundColor = Color(0xFF24CAAC),
            actions = {
                Box(
                    modifier = Modifier
                        .size(width = 240.dp, height = 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFECECEC))
                )
                Box( // padding
                    modifier = Modifier
                        .size(width = 5.dp, height = 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF24CAAC))
                )
            }

        )
    }
    // Search Button
    Box(modifier = Modifier.padding(start = 5.dp, top = 3.dp)) {
        IconButton(onClick = { navController.navigate(ScreenRoutes.HomeScreen.route) }) {
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
    val video_id = "jfKfPfyJRdk" // video-id example
    //YoutubeScreen("jfKfPfyJRdk", Modifier)
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {


}