package com.potikot.doings.presentation.project.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.potikot.doings.R
import com.potikot.doings.domain.util.PriorityLevel
import com.potikot.doings.domain.util.getPriorityColor
import com.potikot.doings.presentation.components.PopupWindow
import com.potikot.doings.presentation.project.ProjectEvent
import com.potikot.doings.presentation.project.TagDialogMode
import com.potikot.doings.presentation.project.TagDialogState
import com.potikot.doings.presentation.project.TagDialogType
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagPopupDialog(
    state: TagDialogState,
    onEvent: (ProjectEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mode = state.mode ?: return
    val type = state.type ?: return

    var isPickingStartDate by remember(state.taskId, type, mode) { mutableStateOf(false) }
    var isPickingEndDate by remember(state.taskId, type, mode) { mutableStateOf(false) }

    PopupWindow(
        title = if (mode == TagDialogMode.CREATE) "Создание тега" else "Редактирование тега",
        saveEnabled = isTagSaveEnabled(state),
        onSave = { onEvent(ProjectEvent.SaveTagDialog) },
        onCancel = { onEvent(ProjectEvent.CancelTagDialog) },
        onDelete = if (mode == TagDialogMode.EDIT) {
            { onEvent(ProjectEvent.DeleteTagDialog) }
        } else {
            null
        },
        modifier = modifier
    ) {
        TagDialogContent(
            dialog = state,
            onEvent = onEvent,
            onPickStartDate = { isPickingStartDate = true },
            onPickEndDate = { isPickingEndDate = true }
        )
    }

    if (type == TagDialogType.DEADLINE && isPickingStartDate) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.deadlineStartDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { isPickingStartDate = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(ProjectEvent.UpdateTagDeadlineStart(pickerState.selectedDateMillis))
                        isPickingStartDate = false
                    },
                    enabled = pickerState.selectedDateMillis != null
                ) {
                    Text("Выбрать")
                }
            },
            dismissButton = {
                TextButton(onClick = { isPickingStartDate = false }) {
                    Text("Отменить")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (type == TagDialogType.DEADLINE && isPickingEndDate) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.deadlineEndDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { isPickingEndDate = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(ProjectEvent.UpdateTagDeadlineEnd(pickerState.selectedDateMillis))
                        isPickingEndDate = false
                    },
                    enabled = pickerState.selectedDateMillis != null
                ) {
                    Text("Выбрать")
                }
            },
            dismissButton = {
                TextButton(onClick = { isPickingEndDate = false }) {
                    Text("Отменить")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}


@Composable
private fun TagDialogContent(
    dialog: TagDialogState,
    onEvent: (ProjectEvent) -> Unit,
    onPickStartDate: () -> Unit,
    onPickEndDate: () -> Unit
) {
    when (dialog.type) {
        TagDialogType.CUSTOM -> {
            OutlinedTextField(
                value = dialog.customValue,
                onValueChange = { onEvent(ProjectEvent.UpdateTagCustomValue(it)) },
                singleLine = true,
                label = { Text("Текст тега") }
            )
        }
        TagDialogType.PRIORITY -> {
            LazyColumn() {
                items(PriorityLevel.entries) { level ->
                    TextButton(
                        onClick = { onEvent(ProjectEvent.UpdateTagPriorityValue(level)) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_priority_24),
                                tint = if (dialog.priorityValue == level) getPriorityColor(level) else LocalContentColor.current,
                                contentDescription = null,
                            )

                            Text(
                                text = level.toString(),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        TagDialogType.DEADLINE -> {
            Column() {
                TextButton(onClick = onPickStartDate) {
                    Text("Дата начала: ${formatDate(dialog.deadlineStartDateMillis)}")
                }
                TextButton(onClick = onPickEndDate) {
                    Text("Дата конца: ${formatDate(dialog.deadlineEndDateMillis)}")
                }
            }
        }
        null -> Unit
    }
}

private fun isTagSaveEnabled(dialog: TagDialogState): Boolean {
    return when (dialog.type) {
        TagDialogType.CUSTOM -> dialog.customValue.trim().isNotEmpty()
        TagDialogType.PRIORITY -> true
        TagDialogType.DEADLINE -> {
            val start = dialog.deadlineStartDateMillis
            val end = dialog.deadlineEndDateMillis
            start != null && end != null && start <= end
        }
        null -> false
    }
}

private fun formatDate(value: Long?): String {
    val date = value?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    return date?.toString() ?: "Не выбрана"
}

//@Preview
//@Composable
//private fun PreviewTagCard() {
//    DoingsTheme {
//        val tags = listOf(
//            Tag.Deadline(
//                start = LocalDateTime.now(),
//                end = LocalDateTime.now(),
//                common = CommonTagData(position = 0),
//            ),
//            Tag.Priority(
//                level = PriorityLevel.HIGH,
//                common = CommonTagData(position = 0),
//            ),
//            Tag.Custom(
//                value = "Custom",
//                common = CommonTagData(position = 0)
//            )
//        )
//
//        LazyColumn {
//            items(tags) {
//                TagCard(
//                    tag = it,
//                    modifier = Modifier.padding(bottom = 8.dp),
//                )
//            }
//        }
//    }
//}