package com.example.lofiapp.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lofiapp.R
import com.example.lofiapp.data.MenuAction
import com.example.lofiapp.data.ScreenRoutes
import com.example.lofiapp.ui.theme.flamenco_regular
import com.example.lofiapp.ui.theme.montserrat_light


@Composable
fun SearchScreen(navController: NavController) {

    val list = listOf("1","1","1","1", "1","1","1","1") // placeholder
    val video_id = "jfKfPfyJRdk" // video-id example
    val fullsize_path_img:String = "https://img.youtube.com/vi/$video_id/maxresdefault.jpg" // thumbnail link example
    var text by remember { mutableStateOf("") }

    // Top App Bar and Videos
    Column( modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { }, backgroundColor = Color(0xFF24CAAC))

        // Recommended Videos Display (horz. scroll)
        LazyColumn(modifier = Modifier.padding(top = 20.dp).align(CenterHorizontally)) {
            items(items = list, itemContent = { item ->
                Column() { // Video Display
                    AsyncImage( // Video thumbnail
                        model = fullsize_path_img,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 270.dp, height = 140.dp)
                            .clip(RoundedCornerShape(12))
                            .align(CenterHorizontally)
                    )
                    Text( // Video name
                        text = "lofi hip hop radio \uD83D\uDCDA - beats to relax/study to",
                        maxLines = 2,
                        modifier = Modifier
                            .padding(start = 5.dp, bottom = 20.dp)
                            .width(278.dp)
                            .height(IntrinsicSize.Max)
                            .align(CenterHorizontally),
                        fontSize = 16.sp,
                        fontFamily = montserrat_light
                    )
                }
            })
        }
    }
    // Top App Bar Components
    Row() {
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
        Canvas( // Search box background
            modifier = Modifier.offset(x = (0).dp, y = (7).dp),
            onDraw = {
                drawRoundRect( // Search box shape
                    color = Color(0xFFECECEC),
                    size = Size(width = 245.dp.toPx(), height = 40.dp.toPx()),
                    cornerRadius = CornerRadius(x = 36.dp.toPx(), 36.dp.toPx()),
                )
            }
        )

        val customTextSelectionColors = TextSelectionColors( // selection text color
            handleColor = Color(0xFF24CAAC),
            backgroundColor = Color(0xFF24CAAC).copy(alpha = 0.4f)
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField( // textfield
                value = text,
                onValueChange = { newText -> text = newText },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = montserrat_light
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .padding(top = 14.dp, start = 10.dp)
                    .size(width = 225.dp, height = 30.dp),
                decorationBox = { innerTextField ->
                    Row() {
                        if (text.isEmpty()) { // if there is no text
                            Icon( // search icon
                                imageVector = MenuAction.Search.icon,
                                contentDescription = stringResource(MenuAction.Search.label),
                                tint = Color.Black.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(bottom = 2.dp)
                            )
                            Text( // search placeholder
                                text = "Search",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black.copy(alpha = 0.5f),
                                fontFamily = flamenco_regular,
                                modifier = Modifier.padding(top = 2.dp, start = 5.dp),
                                )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

