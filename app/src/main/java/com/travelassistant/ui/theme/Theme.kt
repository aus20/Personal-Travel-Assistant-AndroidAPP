package com.travelassistant.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Background,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryText,
    secondary = PrimaryLight,
    onSecondary = PrimaryText,
    secondaryContainer = PrimaryLight,
    onSecondaryContainer = PrimaryText,
    tertiary = PrimaryDark,
    onTertiary = Background,
    tertiaryContainer = PrimaryDark,
    onTertiaryContainer = Background,
    background = Background,
    onBackground = PrimaryText,
    surface = Surface,
    onSurface = PrimaryText,
    surfaceVariant = CardBackground,
    onSurfaceVariant = SecondaryText,
    outline = Divider,
    inverseOnSurface = Background,
    inverseSurface = PrimaryText,
    inversePrimary = PrimaryLight,
    surfaceTint = PrimaryBlue,
    outlineVariant = Divider,
    scrim = PrimaryText
)

@Composable
fun TravelAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
} 