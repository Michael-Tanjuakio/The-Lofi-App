package com.example.lofiapp.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lofiapp.R

sealed class MenuAction(@StringRes val label: Int, val icon: ImageVector) {
    object Search : MenuAction(R.string.Search, Icons.Filled.Search)
}
