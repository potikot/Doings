package com.potikot.doings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun TextFieldDialogue(
    initialValue: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Enter text...",
    autoFocus: Boolean = true,
    confirmText: String = "Ok",
    dismissText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var projectName by remember { mutableStateOf(initialValue) }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        text = {
            Column {
                val textStyle = MaterialTheme.typography.bodyMedium
                TextField(
                    value = projectName,
                    placeholder = { Text(placeholder, style = textStyle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                    onValueChange = { projectName = it },
                    singleLine = true,
                    textStyle = textStyle,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(projectName)
                },
                enabled = projectName.isNotBlank()
            ) {
                Text(confirmText, style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissText, style = MaterialTheme.typography.bodyMedium)
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
    )

    LaunchedEffect(Unit) {
        if (autoFocus) {
            delay(25)
            focusRequester.requestFocus()
        }
    }
}