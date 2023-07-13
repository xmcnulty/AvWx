package io.mcnulty.avwx.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = Orange20,
    primaryContainer = Orange30,
    onPrimaryContainer = Orange90,
    secondaryContainer = LightGray30,
    onSecondaryContainer = LightGray90,
    background = Black10,
    onBackground = Black90,
    surface = Black10,
    onSurface = Black90,
    surfaceVariant = DarkGray30,
    onSurfaceVariant = DarkGray80,
    outline = DarkGray50,
    outlineVariant = DarkGray30,
    inverseSurface = White,
    inverseOnSurface = Black10,
    inversePrimary = Orange50
)

private val LightColorScheme = lightColorScheme(
    primary = Orange50,
    onPrimary = White,
    primaryContainer = Orange90,
    onPrimaryContainer = Orange10,
    secondaryContainer = LightGray40,
    onSecondaryContainer = LightGray10,
    background = White,
    onBackground = Black10,
    surface = White,
    onSurface = Black10,
    surfaceVariant = DarkGray90,
    onSurfaceVariant = DarkGray30,
    outline = DarkGray50,
    outlineVariant = DarkGray80,
    inverseSurface = Black10,
    inverseOnSurface = Black90,
    inversePrimary = Orange80
)

@Composable
fun AvWxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}