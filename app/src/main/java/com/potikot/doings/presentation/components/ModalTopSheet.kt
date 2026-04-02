package com.potikot.doings.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun ModalTopSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    // We use a Dialog to capture back-presses and clicks outside the sheet.
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            // Use non-secure policy to allow screenshots, and make it expand to the full screen width
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                securePolicy = SecureFlagPolicy.Inherit
            )
        ) {
            // Box to align the content to the top
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                // The actual animated content
                AnimatedVisibility(
                    visible = true, // Control visibility via the parent Dialog
                    enter = slideInVertically(
                        // Animate from the top of the screen
                        initialOffsetY = { -it },
                        animationSpec = tween(300)
                    ),
                    exit = slideOutVertically(
                        // Animate back to the top
                        targetOffsetY = { -it },
                        animationSpec = tween(300)
                    )
                ) {
                    // It's good practice to have a container with a background
                    // inside the animation to prevent UI flicker.
                    Box(modifier = Modifier.fillMaxWidth()) {
                        content()
                    }
                }
            }
        }
    }
}