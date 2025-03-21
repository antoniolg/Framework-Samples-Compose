package com.antonioleiva.frameworksamples.ui.screens.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.antonioleiva.frameworksamples.ui.theme.CustomTypographyShapesTheme
import kotlinx.serialization.Serializable

@Serializable
data object CustomTypographyShapesScreen

@Composable
fun CustomTypographyShapesScreen(
    onBack: () -> Unit
) {
    CustomTypographyShapesTheme {
        Screen(
            title = stringResource(R.string.theme_custom_typo_shapes_title),
            onBack = onBack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Introduction
                Text(
                    text = stringResource(R.string.theme_custom_typo_shapes_desc),
                    style = MaterialTheme.typography.bodyMedium
                )

                // Typography showcase
                Text(
                    text = stringResource(R.string.theme_custom_typo_showcase),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                // Display typography examples
                TypographyExample(
                    title = "DisplayLarge",
                    style = MaterialTheme.typography.displayLarge
                )

                TypographyExample(
                    title = "DisplayMedium",
                    style = MaterialTheme.typography.displayMedium
                )

                TypographyExample(
                    title = "HeadlineLarge",
                    style = MaterialTheme.typography.headlineLarge
                )

                TypographyExample(
                    title = "TitleLarge",
                    style = MaterialTheme.typography.titleLarge
                )

                TypographyExample(
                    title = "BodyLarge",
                    style = MaterialTheme.typography.bodyLarge
                )

                // Shapes showcase
                Text(
                    text = stringResource(R.string.theme_custom_shapes_showcase),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )

                // Small shape
                ShapeExample(
                    title = "Small Shape",
                    shape = MaterialTheme.shapes.small
                )

                // Medium shape
                ShapeExample(
                    title = "Medium Shape",
                    shape = MaterialTheme.shapes.medium
                )

                // Large shape
                ShapeExample(
                    title = "Large Shape",
                    shape = MaterialTheme.shapes.large
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TypographyExample(title: String, style: TextStyle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.theme_typography_sample_text),
                style = style
            )
        }
    }
}

@Composable
private fun ShapeExample(title: String, shape: Shape) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = shape
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.theme_shape_label),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
} 