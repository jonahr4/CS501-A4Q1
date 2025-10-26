package com.example.myapplication

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF006C4E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF78F8C6),
    onPrimaryContainer = Color(0xFF002115),
    secondary = Color(0xFF00677D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFAEECFF),
    onSecondaryContainer = Color(0xFF001F27),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5ADBAB),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF005139),
    onPrimaryContainer = Color(0xFF78F8C6),
    secondary = Color(0xFF6CD3F5),
    onSecondary = Color(0xFF003544),
    secondaryContainer = Color(0xFF004D61),
    onSecondaryContainer = Color(0xFFAEECFF),
    background = Color(0xFF101412),
    onBackground = Color(0xFFE0E3DF),
    surface = Color(0xFF101412),
    onSurface = Color(0xFFE0E3DF)
)

@Composable
fun LifeTrackerTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
