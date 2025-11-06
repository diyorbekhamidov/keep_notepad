package com.bounce.keep

import androidx.compose.ui.window.ComposeUIViewController
import com.bounce.keep.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }