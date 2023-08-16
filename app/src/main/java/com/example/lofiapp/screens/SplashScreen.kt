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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@IgnoreExtraProperties
data class youtubeVideo(
    val videoTitle: String = "",
    val videoID: String = ""
) {}

@IgnoreExtraProperties
data class single_playlist(
    val playlistID: String = "",
    val playlistTitle: String = "",
    val playlistCount: Int = 0,
    val videoList : MutableList<youtubeVideo?> = mutableListOf()
)  {}

@Composable
fun SplashScreen(navController: NavController) {



    val animationDelay = 1500
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF24CAAC))
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {

            // code provided by SemicolonSpace
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
        val database = Firebase.database.reference
        database.child("videos")
        database.child("playlists")
        val videos = FirebaseDatabase.getInstance().getReference("videos")
        val playlists = FirebaseDatabase.getInstance().getReference("playlists")
        playlists.child("add_playlist").setValue(
            single_playlist(
                "Create New Playlist",
                "Create New Playlist",
                0,
                mutableListOf())
        )
        videos.child("video1").setValue(
            youtubeVideo(
                "Rainy Jazz Cafe - Slow Jazz Music in Coffee Shop Ambience for Work, Study and Relaxation",
                "NJuSStkIZBg"
            )
        )
        videos.child("video2").setValue(
            youtubeVideo(
                "Breathe \uD83C\uDF40 Lofi Deep Focus \uD83C\uDF33 Study/Calm/Heal [ Lofi Hip Hop - Lofi Chill ] ",
                "6H-PLF2CR18"
            )
        )
        videos.child("video3").setValue(
            youtubeVideo(
                "Calm Jazz Music | Background Chill | Cafe Music | Relaxing Work & Study",
                "Zf3kjcmWcos"
            )
        )
        videos.child("video4").setValue(
            youtubeVideo(
                "a rainy afternoon in sweden ~ nordic lofi mix",
                "-EY97tZAkNY"
            )
        )
        videos.child("video5").setValue(
            youtubeVideo(
                "Popular Anime Openings But It's Lofi Remix ~ Best Anime Lofi Hip Hop Mix",
                "CtwQuK5pB3Q"
            )
        )
        videos.child("video6").setValue(
            youtubeVideo(
                "Poké & Chill",
                "2DVpys50LVE"
            )
        )
        videos.child("video7").setValue(
            youtubeVideo(
                "Chill Lofi Mix chill lo fi hip hop beats",
                "AkWyMHevVjk"
            )
        )
        videos.child("video8").setValue(
            youtubeVideo(
                "When Everything Is Gone - Lofi Hip Hop Mix | Chill Beats",
                "ihBlOSkbJTc"
            )
        )
        videos.child("video9").setValue(
            youtubeVideo(
                "After Sunset- Lofi Hip Hop Mix | Chill Beats | Free background Music",
                "31sy0CZHDgE"
            )
        )
        videos.child("video10").setValue(
            youtubeVideo(
                "Stardew & Chill",
                "yEPUhesWICA"
            )
        )
        videos.child("video11").setValue(
            youtubeVideo(
                "Lofi Mix | 1 hour of Chill Music | Best Music Beat 2021 ",
                "lQDS7ryK2qo"
            )
        )
        videos.child("video12").setValue(
            youtubeVideo(
                "lofi beats by James | code, relax, study, stream | no copyright",
                "eRNClZgJzd4"
            )
        )
        videos.child("video13").setValue(
            youtubeVideo(
                "Lofi Mix 2 - Stress Relief Relaxing Music",
                "6VapvDlN4pg"
            )
        )
        videos.child("video14").setValue(
            youtubeVideo(
                "No Copyright Music Playlist - 20 Minutes Lofi Hip Hop Mix",
                "pxPWEudVo3M"
            )
        )
        videos.child("video15").setValue(
            youtubeVideo(
                "Tavern/Inn Music - Fantasy Medieval Music (No Copyright) Vol. 2",
                "roABNwbjZf4"
            )
        )
    }


    var ticks by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            ticks++
        }
    }
    if (ticks == 3) // Launches app in 3 seconds
        LaunchedEffect(Unit) {
            navController.navigate("home_screen")
        }
}