package com.example.lofiapp.screens

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun VideoScreen(navController: NavController, video_id: String) {

    val list = listOf("1", "1", "1", "1", "1", "1", "1", "1") // placeholder
    //val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img =
        "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var text by remember { mutableStateOf("") }

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
        topBar = { },
        content = { padding ->
            // Diaplyed Videos
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding()
                    .background(Color.Black)
            ) {
                Box(modifier = Modifier.align(CenterVertically)) {
                    Box(modifier = Modifier.height(380.dp)) {
                        YoutubeScreen(video_id, Modifier)
                    }
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


    val ctx = LocalContext.current
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val video_id = "beamS4GZ5T8"
            loadUrl("https://www.youtube.com/embed/$video_id?rel=0&amp;controls=0")
            setClickable(true)
        }
    })

    Button(
        onClick = { },
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .alpha(0f),
        content = {},
    )
    Column() {
        Spacer(modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .alpha(0f),
            content = {},
        )
    }
}

