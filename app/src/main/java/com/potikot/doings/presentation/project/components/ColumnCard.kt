package com.potikot.doings.presentation.project.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.presentation.util.generateMockColumn
import com.potikot.doings.ui.theme.DoingsTheme

@Composable
fun ColumnCard(
    column: Column,
    modifier: Modifier = Modifier,
    onCreateTask: (ColumnId, String, String?) -> Unit, // not used because implemented in FAB
    onOpenOptions: (ColumnId) -> Unit,
    onDelete: (ColumnId) -> Unit, // not used because implemented in options
    onToggleDone_Task: (TaskId, Boolean) -> Unit,
    onOpenOptions_Task: (TaskId) -> Unit,
    onTagClick_Task: (TaskId, Tag) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val cardShape = MaterialTheme.shapes.large

    Surface(
        modifier = modifier,
        color = containerColor,
        shape = cardShape,
    ) {
        Column {
            Row(
                modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = column.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = column.tasks.size.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onCreateTask(column.id, "ss", "ss") },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "More options with column",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onOpenOptions(column.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options with column",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (column.tasks.isEmpty()) {
                return@Column
            }

            Spacer(modifier = Modifier.height(4.dp))

            column.tasks.forEach { task ->
                TaskCard(
                    task = task,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onToggleCompleted = { onToggleDone_Task(task.id, it) },
                    onOpenOptions = { onOpenOptions_Task(task.id) },
                    onTagClick = { tag -> onTagClick_Task(task.id, tag) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun ColumnCardPreview() {
    DoingsTheme {
        ColumnCard(
            column = generateMockColumn(
                name = "Column 1",
                taskCount = 4
            ),
            modifier = Modifier,
            onCreateTask = { _, _, _ -> },
            onOpenOptions = { },
            onDelete = { },
            onToggleDone_Task = { _, _ -> },
            onOpenOptions_Task = { },
            onTagClick_Task = { _, _ -> }
        )
    }
}