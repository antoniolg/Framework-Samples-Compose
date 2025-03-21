package com.antonioleiva.frameworksamples.ui.screens.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.antonioleiva.frameworksamples.ui.theme.FrameworkSamplesTheme
import kotlinx.serialization.Serializable

@Serializable
data object ComponentWrapperScreen

@Composable
fun ComponentWrapperScreen(
    onBack: () -> Unit
) {
    Screen(
        title = stringResource(R.string.theme_component_wrapper_title),
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
                text = stringResource(R.string.theme_component_wrapper_desc),
                style = MaterialTheme.typography.bodyMedium
            )

            // Card with button examples
            StyleCard(title = stringResource(R.string.theme_button_examples)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Regular Material buttons for comparison
                    Text(
                        text = stringResource(R.string.theme_standard_components),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(onClick = { }) {
                        Text(stringResource(R.string.theme_primary_button))
                    }

                    OutlinedButton(onClick = { }) {
                        Text(stringResource(R.string.theme_secondary_button))
                    }

                    TextButton(onClick = { }) {
                        Text(stringResource(R.string.theme_text_button))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Wrapped custom styled buttons
                    Text(
                        text = stringResource(R.string.theme_wrapper_components),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Custom wrapper buttons
                    PrimaryButton(
                        onClick = { },
                        text = stringResource(R.string.theme_primary_button)
                    )

                    SecondaryButton(
                        onClick = { },
                        text = stringResource(R.string.theme_secondary_button)
                    )

                    TertiaryButton(
                        onClick = { },
                        text = stringResource(R.string.theme_text_button)
                    )
                }
            }

            // Card with text field examples
            StyleCard(title = stringResource(R.string.theme_textfield_examples)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Regular Material text fields for comparison
                    Text(
                        text = stringResource(R.string.theme_standard_components),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    var standardText by remember { mutableStateOf("") }
                    TextField(
                        value = standardText,
                        onValueChange = { standardText = it },
                        label = { Text(stringResource(R.string.theme_standard_textfield)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    var standardOutlinedText by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = standardOutlinedText,
                        onValueChange = { standardOutlinedText = it },
                        label = { Text(stringResource(R.string.theme_standard_outlined_textfield)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Wrapped custom styled text fields
                    Text(
                        text = stringResource(R.string.theme_wrapper_components),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    var customText by remember { mutableStateOf("") }
                    AppTextField(
                        value = customText,
                        onValueChange = { customText = it },
                        label = stringResource(R.string.theme_custom_textfield),
                        modifier = Modifier.fillMaxWidth()
                    )

                    var customOutlinedText by remember { mutableStateOf("") }
                    AppOutlinedTextField(
                        value = customOutlinedText,
                        onValueChange = { customOutlinedText = it },
                        label = stringResource(R.string.theme_custom_outlined_textfield),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun StyleCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

// Wrapper Components

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview(name = "Custom Components Light")
@Preview(
    name = "Custom Components Dark",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Composable
fun CustomComponentsPreview() {
    FrameworkSamplesTheme {
        Surface {
            Column {
                // Buttons
                Text("Custom Buttons", style = MaterialTheme.typography.titleMedium)
                PrimaryButton(onClick = {}, text = "Primary Button")
                SecondaryButton(onClick = {}, text = "Secondary Button")
                TertiaryButton(onClick = {}, text = "Text Button")

                Spacer(modifier = Modifier.height(16.dp))

                // Text Fields
                Text("Custom Text Fields", style = MaterialTheme.typography.titleMedium)
                AppTextField(
                    value = "Text Field",
                    onValueChange = { },
                    label = "Text Field Label"
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppOutlinedTextField(
                    value = "Outlined Field",
                    onValueChange = { },
                    label = "Outlined Field Label"
                )
            }
        }
    }
}