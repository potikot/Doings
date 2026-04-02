package com.potikot.doings.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PopupWindow(
    title: String,
    saveEnabled: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = content,
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Отмена")
            }
        },
        confirmButton = {
            if (onDelete != null) {
                TextButton(onClick = onDelete) {
                    Text("Удалить")
                }
            }
            TextButton(
                onClick = onSave,
                enabled = saveEnabled
            ) {
                Text("Сохранить")
            }
        }
    )
}