package com.example.lofiapp.data



import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.core.content.ContextCompat
import com.example.lofiapp.R

sealed class MenuAction(@StringRes val label: Int, val icon: ImageVector) {
    object Search : MenuAction(R.string.Search, Icons.Filled.Search)
}
