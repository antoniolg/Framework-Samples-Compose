package com.antonioleiva.frameworksamples.ui.screens.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.antonioleiva.frameworksamples.ui.theme.AppThemeState
import com.antonioleiva.frameworksamples.ui.theme.FrameworkSamplesTheme
import com.antonioleiva.frameworksamples.ui.theme.ThemeType
import com.antonioleiva.frameworksamples.ui.theme.currentThemeName
import kotlinx.serialization.Serializable

@Serializable
object ThemeSelectorScreen

@Composable
fun ThemeSelectorScreen(onBack: () -> Unit = {}) {
    var currentTheme by rememberSaveable { mutableStateOf(AppThemeState.currentTheme) }

    // Update the app-wide theme state whenever the selection changes
    if (currentTheme != AppThemeState.currentTheme) {
        AppThemeState.currentTheme = currentTheme
    }

    Screen(
        title = stringResource(R.string.theme_selector_title),
        onBack = onBack
    ) { modifier ->
        // Main content column
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Theme selector
            ThemeOptions(
                currentTheme = currentTheme,
                onThemeSelected = { currentTheme = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Theme preview - the card now just shows the current theme's colors
            ThemePreviewCard()
        }
    }
}

@Composable
private fun ThemeOptions(
    currentTheme: ThemeType,
    onThemeSelected: (ThemeType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_theme),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ThemeType.entries.forEach { themeType ->
                ThemeOption(
                    themeType = themeType,
                    isSelected = themeType == currentTheme,
                    onSelected = { onThemeSelected(themeType) }
                )

                if (themeType == ThemeType.Dynamic) {
                    Text(
                        text = stringResource(R.string.theme_dynamic_note),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 48.dp, top = 4.dp, bottom = 8.dp)
                    )
                }

                if (themeType != ThemeType.entries.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    themeType: ThemeType,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected
        )

        Text(
            text = when (themeType) {
                ThemeType.Default -> stringResource(R.string.theme_default)
                ThemeType.Blue -> stringResource(R.string.theme_blue)
                ThemeType.Dynamic -> stringResource(R.string.theme_dynamic)
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun ThemePreviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.theme_preview),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.current_theme, currentThemeName()),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary color row
            Text(
                text = stringResource(R.string.theme_base_colors),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main color preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Primary color
                ColorPreview(
                    color = MaterialTheme.colorScheme.primary,
                    name = stringResource(R.string.primary)
                )

                // Secondary color
                ColorPreview(
                    color = MaterialTheme.colorScheme.secondary,
                    name = stringResource(R.string.secondary)
                )

                // Tertiary color
                ColorPreview(
                    color = MaterialTheme.colorScheme.tertiary,
                    name = stringResource(R.string.tertiary)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Background and surface colors
            Text(
                text = stringResource(R.string.theme_background_colors),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Background color
                ColorPreview(
                    color = MaterialTheme.colorScheme.background,
                    name = stringResource(R.string.background)
                )

                // Surface color
                ColorPreview(
                    color = MaterialTheme.colorScheme.surface,
                    name = stringResource(R.string.surface)
                )

                // Surface Variant color
                ColorPreview(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    name = stringResource(R.string.surface_variant)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "On" colors (text colors)
            Text(
                text = stringResource(R.string.theme_text_colors),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // On Primary color
                ColorPreview(
                    color = MaterialTheme.colorScheme.onPrimary,
                    name = stringResource(R.string.on_primary)
                )

                // On Background color
                ColorPreview(
                    color = MaterialTheme.colorScheme.onBackground,
                    name = stringResource(R.string.on_background)
                )

                // On Surface color
                ColorPreview(
                    color = MaterialTheme.colorScheme.onSurface,
                    name = stringResource(R.string.on_surface)
                )
            }
        }
    }
}

@Composable
private fun ColorPreview(
    color: androidx.compose.ui.graphics.Color,
    name: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ThemeSelectorScreenPreview() {
    FrameworkSamplesTheme {
        Surface {
            ThemeSelectorScreen()
        }
    }
}