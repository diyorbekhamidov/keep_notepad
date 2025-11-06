package com.bounce.keep.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class TopAppBarState(
    val navigationBack: (@Composable () -> Unit)? = null,
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val floatingActionButton: (@Composable () -> Unit)? = null
)
