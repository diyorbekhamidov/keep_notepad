package com.bounce.keep.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    primaryContainer = Blue80,
    onPrimary = Color.DarkGray,
    onPrimaryContainer = Color.White,
    secondary = BlueGrey80,
    tertiary = Yellow80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    primaryContainer = Blue80,
    onPrimary = Color.Black,
    onPrimaryContainer = Color.White,
    secondary = BlueGrey40,
    tertiary = Yellow40
)

@Composable
fun KeepNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}