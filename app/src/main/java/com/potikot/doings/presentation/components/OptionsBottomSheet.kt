package com.potikot.doings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.potikot.doings.presentation.util.OptionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsBottomSheet(
    options: List<OptionItem>,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
    ) {
        LazyColumn(
            modifier = Modifier.navigationBarsPadding()
        ) {
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
                        onDismiss()
                    }
                )
            }
        }
    }
}