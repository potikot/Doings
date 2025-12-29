package com.potikot.doings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potikot.doings.R
import com.potikot.doings.domain.model.CommonTagData
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.util.PriorityLevel
import com.potikot.doings.domain.util.getPriorityColor
import com.potikot.doings.presentation.util.ChipContent
import com.potikot.doings.ui.theme.DoingsTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TagItem(
    tag: Tag,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val chipContent = getChipContent(tag)

    AssistChip(
        label = chipContent.label,
        leadingIcon = chipContent.leadingIcon,
        trailingIcon = chipContent.trailingIcon,
        modifier = modifier.height(24.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            leadingIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview
@Composable
private fun TagItemPreview() {
    DoingsTheme {
        TagItem(
            tag = Tag.Deadline(common = CommonTagData(
                id = TagId(0),
                externalId = "",
                position = 0,
                createdAt = Instant.now()
            ), end = LocalDateTime.now().plusDays(3)),
            onClick = {}
        )
    }
}

@Composable
private fun getChipContent(tag: Tag): ChipContent {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM")
    val icon = getTagIcon(tag)

    return when (tag) {
        is Tag.Deadline -> {
            ChipContent(
                label = {
                    Text(
                        text = tag.end?.format(dateFormatter) ?: "no",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    if (icon == null) return@ChipContent
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
        is Tag.Priority -> {
            ChipContent(
                label = {
                    Text(
                        text = tag.level.toString(),
                        color = if (tag.level > PriorityLevel.LOW) getPriorityColor(tag.level) else Color.Unspecified,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    if (icon == null) return@ChipContent
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (tag.level > PriorityLevel.LOW) getPriorityColor(tag.level) else Color.Unspecified
                    )
                }
            )
        }
        is Tag.Custom -> {
            ChipContent(
                label = {
                    Text(
                        text = tag.value,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    if (icon == null) return@ChipContent
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            )
        }
//        is Tag.Reminder -> {
//            ChipContent(
//                label = {
//                    Text(
//                        text = tag.date?.format(dateFormatter) ?: "no",
//                        style = MaterialTheme.typography.labelSmall
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = getTagIcon(tag),
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            )
//        }
//        is Tag.Timer -> {
//            ChipContent(
//                label = {
//                    Text(
//                        text = tag.remaining?.toString() ?: "no",
//                        style = MaterialTheme.typography.labelSmall
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = getTagIcon(tag),
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            )
//        }
    }
}

@Composable
private fun getTagIcon(tag: Tag): ImageVector? {
    return when(tag) {
        is Tag.Deadline -> Icons.Outlined.DateRange
        is Tag.Priority -> ImageVector.vectorResource(id = R.drawable.ic_priority_24)
//        is Tag.Reminder -> ImageVector.vectorResource(id = R.drawable.ic_reminder_v2)
//        is Tag.Timer -> ImageVector.vectorResource(id = R.drawable.ic_timer)
        is Tag.Custom -> null
    }
}