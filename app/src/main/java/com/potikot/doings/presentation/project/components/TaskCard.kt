package com.potikot.doings.presentation.project.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potikot.doings.domain.model.CommonTagData
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.util.PriorityLevel
import com.potikot.doings.presentation.components.TagItem
import com.potikot.doings.presentation.util.generateMockTask
import com.potikot.doings.ui.theme.DoingsTheme
import java.time.Instant
import java.time.LocalDateTime

@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onToggleCompleted: (Boolean) -> Unit,
    onOpenOptions: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = modifier
    ) {
        Column {
            Row(modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)) {
                Spacer(Modifier.width(8.dp))

                TaskCheckmark(
                    isChecked = task.isCompleted,
                    onToggleCompleted = onToggleCompleted,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Spacer(Modifier.width(8.dp))

                // set max height to 3 lines
                val textStyle = MaterialTheme.typography.bodyMedium
                val lineHeight = textStyle.lineHeight
                val minHeight = with(LocalDensity.current) { lineHeight.toDp() }
                val maxHeight = with(LocalDensity.current) { (lineHeight * 3).toDp() }

                Text(
                    text = task.name,
                    style = textStyle,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = minHeight, max = maxHeight)
                        .align(Alignment.CenterVertically)
                )

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenOptions,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Task options"
                    )
                }

                Spacer(Modifier.width(8.dp))
            }

            if (task.tags.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                ) {
                    items(task.tags.size) { index ->
                        TagItem(
                            tag = task.tags[index],
                            modifier = Modifier
                                .padding(start = if (index > 0) 8.dp else 0.dp),
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskCardPreview() {
    DoingsTheme {
        TaskCard(
            task = generateMockTask(
                title = "Long task title. ".repeat(10),
                description = "Description",
                tags = listOf(
                    Tag.Deadline(getCommonTagData(), end = LocalDateTime.now().plusDays(3)),
                    Tag.Priority(getCommonTagData(), PriorityLevel.CRITICAL),
                    Tag.Priority(getCommonTagData(), PriorityLevel.NONE),
                    Tag.Priority(getCommonTagData(), PriorityLevel.LOW),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                    Tag.Priority(getCommonTagData(), PriorityLevel.HIGH),
                )
            ),
            modifier = Modifier,
            onToggleCompleted = { },
            onOpenOptions = { }
        )
    }
}

private fun getCommonTagData(): CommonTagData {
    return CommonTagData(
        id = TagId(0),
        externalId = "",
        position = 0,
        createdAt = Instant.now()
    )
}