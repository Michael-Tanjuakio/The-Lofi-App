package com.example.lofiapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Paint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_bold
import com.example.lofiapp.ui.theme.montserrat_light
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// bottom bar navigation functionality, fixed search bar sizing
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, bottomBar_pic: String, bottomBar_title: String) {

    // System Back handler (Go nowhere)
    BackHandler(true, onBack = { })

    // Pull to refresh components
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(0) }

    // Refresh function
    fun refresh() = refreshScope.launch {
        refreshing = true
        itemCount += 1 // randomizes videos
        delay(1500)
        refreshing = false
    }

    // Refresh state
    val state = rememberPullRefreshState(refreshing, ::refresh)

    // Locked Vertical Orientation
    val context = LocalContext.current
    val activity = remember { context as Activity }
    activity.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    // Retrieve list from database
    val playlists = FirebaseDatabase.getInstance().getReference("playlists")
    val videos = FirebaseDatabase.getInstance().getReference("videos")

    // App list display
    val recList by remember { mutableStateOf(mutableListOf<youtubeVideo?>()) }
    val playlist_List = remember { mutableStateListOf<single_playlist?>() }
    val addPlaylist_List = remember { mutableStateListOf<single_playlist?>() }

    // Popups
    var showAddPlaylist by remember { mutableStateOf(false) }
    var showCreateNewPlaylist by remember { mutableStateOf(false) }
    var createPlaylist by remember { mutableStateOf(false) } // no title dialog popup
    var openNoTitleDialog by remember { mutableStateOf(false) }

    // Generate playlist list once
    var generateList by remember { mutableStateOf(true) }

    // Generate playlist list once
    var addVideo by remember { mutableStateOf(youtubeVideo()) }

    // Generate playlist list once
    var addVideoToPlaylists by remember { mutableStateOf(false) }

    // Read from the database
    LaunchedEffect(itemCount) {

        // Clear recommended videos list
        recList.clear()

        // Generate 5 distinct random numbers
        var randomInts = generateSequence { (1..15).random() }
            .distinct()
            .take(5)
            .toSet()

        // Retrieve videos of "video + random #" from database
        randomInts.forEach() {
            videos.child("video" + it)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!showAddPlaylist)
                            recList.add(dataSnapshot.getValue<youtubeVideo?>())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    // Retrieve playlists list from database
    var playlistCount by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = true) {
        playlists.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (generateList && !addVideoToPlaylists) {
                    for (childSnapshot in dataSnapshot.children) {
                        playlist_List.add(childSnapshot.getValue<single_playlist?>())
                        playlistCount = dataSnapshot.childrenCount.toInt()
                    }
                    generateList = false
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    // System bar colors
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color(0xFF24CAAC))         // System top bar color
    systemUiController.setNavigationBarColor(color = Color(0xFF24CAAC))     // System bottom bar color

    // Scroll State (used for vertical scrolling)
    val scrollState = rememberScrollState()

    // Interaction Source
    val interactionSource = remember { MutableInteractionSource() }

    // Scaffold (Top bar, Content, Bottom Bar)
    Scaffold(
        topBar = {
            // Top Bar Composable
            TopAppBar(
                title = {
                    // Title Text
                    Text(
                        text = "lofiapp",
                        color = Color.White,
                        fontFamily = flamenco_regular,
                        fontSize = 32.sp
                    )
                },
                backgroundColor = Color(0xFF24CAAC),
                actions = {
                    // Create New Playlist button
                    IconButton(
                        onClick = {
                            navController.navigate(
                                "playlist_screen" +
                                        "/none" +
                                        "?new_playlist=" + true +
                                        "&bottomBar_pic=" + bottomBar_pic +
                                        "&bottomBar_title=" +
                                        URLEncoder.encode(
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        ) +
                                        "&playlist_count=" + playlistCount
                            )
                        }
                    ) {
                        // create new playlist icon
                        Image(
                            painter = painterResource(id = R.drawable.playlist_add_icon),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.White),
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    // Search Button - navigates to search screen
                    IconButton(onClick = {
                        Log.d("new_bp", "passing: " + bottomBar_pic + " " + bottomBar_title)
                        navController.navigate(
                            "search_screen" +
                                    "?bottomBar_pic=$bottomBar_pic" +
                                    "&bottomBar_title=${
                                        URLEncoder.encode(
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    }"
                        ) // encode to pass "&" character
                    }) {
                        Icon( // Search icon
                            imageVector = MenuAction.Search.icon,
                            contentDescription = stringResource(MenuAction.Search.label),
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            )
        },
        // Content
        content = { padding ->
            Box(Modifier.pullRefresh(state)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState) // scrollState here
                    ,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    // Recommended Text
                    Row(modifier = Modifier.padding(top = 45.dp, start = 30.dp)) {
                        Text(
                            text = "Recommended",
                            color = Color(0xFF24CAAC),
                            modifier = Modifier,
                            fontSize = 21.sp,
                            fontFamily = montserrat_bold
                        )
                        Image( // Symbol
                            painter = painterResource(id = R.drawable.video_library_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(34.dp),
                            colorFilter = ColorFilter.tint(color = Color(0xFF5686E1))
                        )
                    }

                    // Recommended Videos Display (horz. scroll)
                    Row(
                        modifier = Modifier
                            .padding(start = 13.dp, top = 0.dp)
                            .height(IntrinsicSize.Max)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        recList.forEach {// goes through each recommended video in recList
                            // Don't display random video
                            if (recList.indexOf(it) == 4) {
                                return@forEach
                            }
                            Column(modifier = Modifier
                                .padding(start = 16.dp)
                                .clip(RoundedCornerShape(12, 12, 5, 5))
                                .clickable(
                                    onClick = {
                                        navController.navigate(  // navigates to video screen
                                            "video_screen/"
                                                    + it?.videoID.toString()
                                                    + "/"
                                                    + URLEncoder.encode( // encode to pass "&" character
                                                it?.videoTitle.toString(),
                                                StandardCharsets.UTF_8.toString()
                                            )
                                        )
                                    }
                                )
                            ) { // Video Display
                                AsyncImage( // Video thumbnail
                                    model = "https://img.youtube.com/vi/" + it?.videoID.toString() + "/maxres2.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 220.dp, height = 134.dp)
                                        .clip(RoundedCornerShape(12))
                                )
                                Text( // Video name
                                    text = it?.videoTitle.toString(),
                                    maxLines = 4,
                                    modifier = Modifier
                                        .width(220.dp),
                                    fontSize = 16.sp,
                                    fontFamily = montserrat_light
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    // Add to playlist button
                                    Image(
                                        painter = painterResource(id = R.drawable.add_new_icon_2),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(color = Color.Black),
                                        modifier = Modifier
                                            .size(35.dp)
                                            .clickable {
                                                if (it != null) {
                                                    addVideo = it
                                                }
                                                showAddPlaylist = true
                                            }
                                    )

                                }
                            }
                        }
                    }

                    // Playlists Title Text
                    Row(modifier = Modifier.padding(top = 30.dp, start = 29.dp)) {
                        Text(
                            text = "Playlists",
                            color = Color(0xFF24CAAC),
                            modifier = Modifier,
                            fontSize = 21.sp,
                            fontFamily = montserrat_bold
                        )
                        Image( // Symbol
                            painter = painterResource(id = R.drawable.playlist_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(34.dp),
                            colorFilter = ColorFilter.tint(color = Color(0xFF5686E1))
                        )
                    }

                    // Playlists display (horz. scroll)
                    LazyRow(modifier = Modifier.padding(start = 13.dp, top = 6.dp)) {
                        items(items = playlist_List, itemContent = { item ->
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clip(RoundedCornerShape(12, 12, 5, 5))
                                    .clickable { // Click to go to playlist
                                        // Go to selected playlist
                                        if (!item?.playlistTitle.equals("Create New Playlist"))
                                            navController.navigate(
                                                "playlist_screen" +
                                                        "/" + item?.playlistID.toString() +
                                                        "?new_playlist=" + false +
                                                        "&bottomBar_pic=" + bottomBar_pic +
                                                        "&bottomBar_title=" +
                                                        URLEncoder.encode(
                                                            bottomBar_title,
                                                            StandardCharsets.UTF_8.toString()
                                                        )
                                            )
                                        // Create New Playlist
                                        else
                                            navController.navigate(
                                                "playlist_screen" +
                                                        "/none" +
                                                        "?new_playlist=" + true +
                                                        "&bottomBar_pic=" + bottomBar_pic +
                                                        "&bottomBar_title=" +
                                                        URLEncoder.encode(
                                                            bottomBar_title,
                                                            StandardCharsets.UTF_8.toString()
                                                        ) +
                                                        "&playlist_count=" + playlistCount
                                            )
                                    }
                            ) {
                                Box() {
                                    // Create New Playlist button
                                    if (item?.playlistTitle.equals("Create New Playlist")) {
                                        Image(
                                            painter = painterResource(id = R.drawable.add_new_icon),
                                            colorFilter = ColorFilter.tint(Color.White),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(width = 220.dp, height = 134.dp)
                                                .clip(RoundedCornerShape(12))
                                                .background(Color.Black.copy(alpha = 0.6f))
                                        )
                                    }

                                    // Playlist with videos button
                                    if (item?.playlistCount!! > 0) {
                                        AsyncImage( // Video Thumbnail
                                            model = "https://img.youtube.com/vi/" + item?.videoList?.get(
                                                0
                                            )?.videoID.toString() + "/maxres2.jpg",
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(width = 220.dp, height = 134.dp)
                                                .clip(RoundedCornerShape(12))
                                        )
                                        Box( // Transparent background
                                            modifier = Modifier
                                                .size(width = 105.dp, height = 134.dp)
                                                .clip(RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp))
                                                .background(Color(0xFF404040).copy(alpha = 0.6f))
                                                .align(alignment = Alignment.TopEnd)
                                        ) {
                                            Text( // Number of videos in playlist
                                                text = item.playlistCount.toString(),
                                                modifier = Modifier
                                                    .align(alignment = Alignment.Center),
                                                color = Color.White,
                                                fontFamily = montserrat_light
                                            )
                                        }
                                    }

                                    // Playlist without videos button
                                    if (item?.playlistCount == 0 && !item?.playlistTitle.equals("Create New Playlist")) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 220.dp, height = 134.dp)
                                                .clip(RoundedCornerShape(12))
                                                .background(Color.Black.copy(alpha = 0.1f))
                                        )
                                    }
                                }
                                Text( // Playlist Name
                                    text = item?.playlistTitle.toString(),
                                    maxLines = 2,
                                    modifier = Modifier
                                        .width(220.dp)
                                        .height(IntrinsicSize.Max),
                                    fontSize = 16.sp,
                                    fontFamily = montserrat_light
                                )
                            }
                        })
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // Random video button
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(
                                "video_screen/"
                                        + recList[4]?.videoID.toString()
                                        + "/"
                                        + URLEncoder.encode( // encode to pass "&" character
                                    recList[4]?.videoTitle.toString(),
                                    StandardCharsets.UTF_8.toString()
                                )
                            )
                        },
                        contentColor = Color(0xFF24CAAC),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(75.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 0.dp, bottom = 0.dp)
                    )
                    {
                        Image( // shuffle symbol
                            painter = painterResource(id = R.drawable.shuffle_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
            }
        },
        bottomBar = {
            // Bottom bar (Displays what video is played)
            if (!bottomBar_title.equals("")) {
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomNavigation(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12))
                                .clickable {
                                    navController.navigate(
                                        "video_screen/"
                                                + bottomBar_pic + "/"
                                                + URLEncoder.encode( // encode to pass "&" and "/" characters
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    )
                                }
                                .fillMaxWidth(.95f),
                            backgroundColor = Color(0xFF3392EA),
                        ) {
                            Log.d(
                                "video playing",
                                "playing: " + bottomBar_title + " " + bottomBar_pic
                            )
                            Row(modifier = Modifier.fillMaxHeight()) { // wrap in row to avoid default spacing
                                Spacer(modifier = Modifier.width(16.dp))
                                AsyncImage( // video thumbnail
                                    model = "https://img.youtube.com/vi/" + bottomBar_pic + "/maxres2.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 65.dp, height = 43.dp)
                                        .clip(RoundedCornerShape(12))
                                        .align(Alignment.CenterVertically)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth(.90f)
                                        .fillMaxHeight(.95f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text( // video name (decodes title)
                                        text = URLDecoder.decode(
                                            bottomBar_title,
                                            StandardCharsets.UTF_8.toString()
                                        ),
                                        fontFamily = montserrat_bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .basicMarquee(),
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                }
            }
        }
    )

    // Popups
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Display add playlist popup
        AnimatedVisibility(
            visible = showAddPlaylist,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000)),
            modifier = Modifier.align(Alignment.Center)
        ) {

            // System back button
            BackHandler(
                enabled = true,
                onBack = { showAddPlaylist = false })

            // Shadowed Background
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
                IconButton(onClick = { showAddPlaylist = false }) {
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
                        .fillMaxWidth(.95f)
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.65f)
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
                                Text( // Title Text in green box
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
                                        color = Color.Black,
                                        modifier = Modifier.fillMaxWidth(.80f)
                                    )

                                    Spacer(Modifier.weight(1f))

                                    Image( // add new icon
                                        painter = painterResource(id = R.drawable.add_new_icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .align(Alignment.CenterVertically)
                                            .clickable(onClick = { // Open create new playlist popup
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
                                        itemsIndexed(playlist_List) { index, item ->

                                            // Checkbox icon
                                            var checkBoxIcon by remember { mutableStateOf(false) }

                                            Row(modifier = Modifier.clickable() {
                                                // Checkbox:
                                                checkBoxIcon = !checkBoxIcon  // Change the icon
                                                if (item != null && checkBoxIcon)
                                                    addPlaylist_List.add(item) // add to list
                                                else
                                                    addPlaylist_List.remove(item) // remove to list if unchecked

                                            }) {

                                                // Display all playlists (except Create New Playlist button)
                                                if (item != null && !(item.playlistTitle.equals("Create New Playlist"))) {
                                                    Text( // Playlist Title
                                                        text = item.playlistTitle,
                                                        fontSize = 20.sp,
                                                        fontFamily = montserrat_light,
                                                        color = Color.Black,
                                                        modifier = Modifier
                                                            .fillMaxWidth(.80f)
                                                    )

                                                    Spacer(Modifier.weight(1f))

                                                    Image( // add to playlist icon
                                                        painter = painterResource(
                                                            id =
                                                            if (!checkBoxIcon)
                                                                R.drawable.check_box_outline_blank_icon
                                                            else
                                                                R.drawable.check_box_icon
                                                        ),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(35.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .clickable() {
                                                                // Checkbox:
                                                                checkBoxIcon =
                                                                    !checkBoxIcon  // Change the icon
                                                                if (item != null && checkBoxIcon) {
                                                                    addPlaylist_List.add(item) // add to list
                                                                    Log.d(
                                                                        "new_bp",
                                                                        "adding: " + item.playlistTitle
                                                                    )
                                                                } else {
                                                                    addPlaylist_List.remove(item) // remove to list if unchecked
                                                                    Log.d(
                                                                        "new_bp",
                                                                        "removing: " + item.playlistTitle
                                                                    )
                                                                }
                                                            },
                                                        colorFilter = ColorFilter.tint(color = Color.Gray)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(5.dp))

                                        }
                                    }
                                }

                                // Update playlist(s)
                                if (addVideoToPlaylists) {
                                    addPlaylist_List.forEach() { playlist ->
                                        if (playlist != null) {
                                            Log.d(
                                                "new_bp",
                                                "adding: " + addVideo.videoTitle + " to " + playlist.playlistTitle
                                            )
                                            val videoList_ = ArrayList(playlist.videoList)
                                            videoList_.add(addVideo)
                                            playlists.child(playlist.playlistID).setValue(
                                                single_playlist(
                                                    playlist.playlistID,
                                                    playlist.playlistTitle,
                                                    ++playlist.playlistCount,
                                                    videoList_
                                                )
                                            )
                                        }
                                    }
                                    addVideoToPlaylists = false
                                }

                                Button(
                                    onClick = {
                                        addVideoToPlaylists = true.apply {
                                            showAddPlaylist = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF3392EA
                                        )
                                    ),
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Confirm",
                                        color = Color.White,
                                        fontFamily = montserrat_bold
                                    )
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
                        .fillMaxWidth(0.95f)
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.65f)
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
                                val customTextSelectionColors =
                                    TextSelectionColors( // selection text color
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

                                if (createPlaylist) {
                                    LaunchedEffect(true) {
                                        generateList = false
                                        FirebaseDatabase.getInstance().getReference("playlists")
                                            .child("Playlist " + playlistCount).setValue(
                                                single_playlist(
                                                    "Playlist " + playlistCount, // path string
                                                    text, // title of playlist
                                                    0, // playlist video count
                                                    listOf<youtubeVideo>() // list of videos
                                                ).apply {
                                                    // Add playlist to the end of the list
                                                    playlist_List.set(
                                                        playlist_List.lastIndex, (
                                                                single_playlist(
                                                                    "Playlist " + playlistCount, // path string
                                                                    text, // title of playlist
                                                                    0, // playlist video count
                                                                    listOf<youtubeVideo>() // list of videos
                                                                )
                                                                )
                                                    )
                                                    // Re-add the "Create New Playlist" button
                                                    playlist_List.add(
                                                        single_playlist(
                                                            "Create New Playlist",
                                                            "Create New Playlist",
                                                            0,
                                                            mutableListOf()
                                                        )
                                                    )
                                                }
                                            )
                                    }
                                    createPlaylist = false
                                }

                                Button(
                                    onClick = {
                                        if (!text.isEmpty()) {
                                            createPlaylist = true.apply {
                                                showCreateNewPlaylist = false
                                            }
                                        } else
                                            openNoTitleDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF3392EA
                                        )
                                    ),
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Confirm",
                                        color = Color.White,
                                        fontFamily = montserrat_bold
                                    )
                                }

                            }
                        }
                    }
                }


            }
        }

        // No title dialog
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




