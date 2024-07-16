package com.example.pry_gym.presentation.theme
import androidx.compose.foundation.background
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier

private val DarkColorPalette = Colors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Color.White
)

@Composable
fun Pry_GYMTheme(
    content: @Composable () -> Unit
) {
    /**
     * Empty theme to customize for your app.
     * See: https://developer.android.com/jetpack/compose/designsystems/custom
     */
    MaterialTheme(
        colors = DarkColorPalette,
        content = content
    )
}