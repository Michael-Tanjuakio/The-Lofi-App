package com.example.lofiapp.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.lofiapp.R
import com.example.lofiapp.data.ScreenRoutes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@IgnoreExtraProperties
data class youtubeVideo(val videoTitle: String? = null, val videoID: String? = null) {}

@Composable
fun SplashScreen(navController: NavController) {

    val animationDelay = 1500
Box(modifier = Modifier.fillMaxSize().background(Color(0xFF24CAAC))) {
    Box(modifier = Modifier.align(Alignment.Center)) {

        // 3 circles
        val circles = listOf(
            remember {
                Animatable(initialValue = 0f)
            },
            remember {
                Animatable(initialValue = 0f)
            },
            remember {
                Animatable(initialValue = 0f)
            }
        )

        circles.forEachIndexed { index, animatable ->
            LaunchedEffect(Unit) {
                // Use coroutine delay to sync animations
                // divide the animation delay by number of circles
                delay(timeMillis = (animationDelay / 3L) * (index + 1))

                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = animationDelay,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }

        // outer circle
        Box(
            modifier = Modifier
                .size(size = 200.dp)
                .background(color = Color(0xFF24CAAC))
        ) {
            // animating circles
            circles.forEachIndexed { index, animatable ->
                Box(
                    modifier = Modifier
                        .scale(scale = animatable.value)
                        .size(size = 200.dp)
                        .clip(shape = CircleShape)
                        .background(
                            color = Color.White
                                .copy(alpha = (1 - animatable.value))
                        )
                ) {
                }
            }
        }
    }
}


    // Enter data to the database
    LaunchedEffect(true) {
        val database = Firebase.database
        val youtubeVideos = database.getReference("videos")

        val video = youtubeVideo(
            "Rainy Jazz Cafe - Slow Jazz Music in Coffee Shop Ambience for Work, Study and Relaxation",
            "NJuSStkIZBg"
        )
        youtubeVideos.setValue(video)

        // Read from the database
        youtubeVideos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<youtubeVideo>()
                Log.d(
                    "database!!!",
                    "Video Title: " + value?.videoTitle + "\nVideoID: " + value?.videoID
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("database!!!", "Failed to read value.", error.toException())
            }
        })
    }


    var ticks by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            ticks++
        }
    }
    if (ticks == 3)
        LaunchedEffect(Unit) {
            navController.navigate(ScreenRoutes.HomeScreen.route)
        }
}