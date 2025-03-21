package com.antonioleiva.frameworksamples.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

// Default purple theme
internal val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

internal val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Blue theme
private val BlueDarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = BlueGrey80,
    tertiary = LightBlue80,
    background = BlueDarkBackground,
    surface = BlueDarkSurface,
    onPrimary = BlueDarkOnPrimary,
    onBackground = BlueDarkOnBackground,
    onSurface = BlueDarkOnSurface,
    surfaceVariant = BlueDarkSurface.copy(alpha = 0.7f),
    onSurfaceVariant = BlueDarkOnSurface.copy(alpha = 0.7f)
)

private val BlueLightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = LightBlue40,
    background = BlueBackground,
    surface = BlueSurface,
    onPrimary = BlueOnPrimary,
    onBackground = BlueOnBackground,
    onSurface = BlueOnSurface,
    surfaceVariant = BlueSurface.copy(alpha = 0.7f),
    onSurfaceVariant = BlueOnSurface.copy(alpha = 0.7f)
)

// Theme types
enum class ThemeType {
    Default,
    Blue,
    Dynamic
}

// Singleton object to store the app-wide theme state
object AppThemeState {
    var currentTheme by mutableStateOf(ThemeType.Default)
}

// CompositionLocal to manage current theme type
val LocalThemeType = staticCompositionLocalOf { ThemeType.Default }

@Composable
fun FrameworkSamplesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeType: ThemeType = ThemeType.Default,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        themeType == ThemeType.Dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> when (themeType) {
            ThemeType.Blue -> BlueDarkColorScheme
            else -> DarkColorScheme
        }
        else -> when (themeType) {
            ThemeType.Blue -> BlueLightColorScheme
            else -> LightColorScheme
        }
    }

    CompositionLocalProvider(LocalThemeType provides themeType) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Helper function to get the current theme name
@Composable
fun currentThemeName(): String {
    return when (LocalThemeType.current) {
        ThemeType.Default -> "Default Purple Theme"
        ThemeType.Blue -> "Blue Theme"
        ThemeType.Dynamic -> "Dynamic Theme (Material You)"
    }
}