package com.antonioleiva.frameworksamples.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antonioleiva.frameworksamples.R

// Custom font family using KoHo font
private val KoHoFamily = FontFamily(
    Font(R.font.koho_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.koho_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.koho_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.koho_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.koho_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.koho_extralight, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.koho_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.koho_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.koho_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.koho_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.koho_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.koho_extralight_italic, FontWeight.Thin, FontStyle.Italic),
)

// Custom font family using MPLUS Rounded font
private val MPlusRoundedFamily = FontFamily(
    Font(R.font.mplus_rounded1c_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.mplus_rounded1c_black, FontWeight.Black, FontStyle.Normal),
)

// Custom typography with different font families for different text styles
val CustomTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = KoHoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.25.sp
    ),
    displayMedium = TextStyle(
        fontFamily = KoHoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.15.sp
    ),
    displaySmall = TextStyle(
        fontFamily = KoHoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.1.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = KoHoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = KoHoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = MPlusRoundedFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MPlusRoundedFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MPlusRoundedFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MPlusRoundedFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MPlusRoundedFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// Custom shapes with different corner styles
val CustomShapes = Shapes(
    small = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
    medium = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
    large = CutCornerShape(16.dp)
)

@Composable
fun CustomTypographyShapesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        shapes = CustomShapes,
        content = content
    )
}