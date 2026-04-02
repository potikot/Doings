package com.potikot.doings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.potikot.doings.presentation.util.OptionItem

@Composable
fun TopOptionsSheet(
    visible: Boolean,
    options: List<OptionItem>,
    onDismiss: () -> Unit
) {
    ModalTopSheet(
        visible = visible,
        onDismiss = onDismiss
    ) {
        // Use a Card or Surface for good visual structure
        Card {
            LazyColumn {
                items(options) { option ->
                    ListItem(
                        headlineContent = { Text(option.title) },
                        leadingContent = {
                            if (option.icon != null) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.clickable {
                            option.action()
                            onDismiss() // Close the sheet after action
                        }
                    )
                }
            }
        }
    }
}