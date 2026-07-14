package com.a1.clickbloom.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Sunset,
    secondary = SoftBlue,
    tertiary = Mint,
    background = DeepNavy,
    surface = Slate,
    onPrimary = DeepNavy,
    onSecondary = DeepNavy,
    onTertiary = DeepNavy,
)

@Composable
fun ClickBloomTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content,
    )
}
