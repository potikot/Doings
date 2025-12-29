package com.potikot.doings.data.data_source.mappers

import com.google.gson.Gson
import com.potikot.doings.core.util.toIso
import com.potikot.doings.core.util.toLocalDateTime
import com.potikot.doings.data.data_source.entity.TagEntity
import com.potikot.doings.data.data_source.util.TagPayloads
import com.potikot.doings.data.data_source.util.TagType
import com.potikot.doings.domain.model.CommonTagData
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.TaskId
import java.time.Instant

// todo: mb i should change this to class and inject 'gson' as dependency?
private val gson = Gson()

fun TagEntity.toDomain(): Tag {
    return when(this.type) {
        TagType.CUSTOM -> {
            val data = gson.fromJson(this.jsonValue, TagPayloads.Custom::class.java)
            Tag.Custom(
                value = data.value,
                common = getDomainCommonData(this)
            )
        }
        TagType.DEADLINE -> {
            val data = gson.fromJson(this.jsonValue, TagPayloads.Deadline::class.java)
            Tag.Deadline(
                start = data.start?.toLocalDateTime(),
                end = data.end?.toLocalDateTime(),
                common = getDomainCommonData(this)
            )
        }
        TagType.PRIORITY -> {
            val data = gson.fromJson(this.jsonValue, TagPayloads.Priority::class.java)
            Tag.Priority(
                level = data.level,
                common = getDomainCommonData(this)
            )
        }
    }
}

fun Tag.toEntity(parentId: TaskId): TagEntity {
    val data = getTagEntityData(this)
    return TagEntity(
        id = this.common.id.value,
        externalId = this.common.externalId,
        taskId = parentId.value,
        position = this.common.position,
        createdAt = this.common.createdAt.toEpochMilli(),
        type = data.type,
        jsonValue = data.json
    )
}

private fun getDomainCommonData(tag: TagEntity): CommonTagData {
    return CommonTagData(
        id = TagId(tag.id),
        externalId = tag.externalId,
        position = tag.position,
        createdAt = Instant.ofEpochMilli(tag.createdAt)
    )
}

private fun getTagEntityData(tag: Tag): TagEntityData {
    return when(tag) {
        is Tag.Custom -> TagEntityData(
            type = TagType.CUSTOM,
            json = gson.toJson(TagPayloads.Custom(value = tag.value))
        )
        is Tag.Deadline -> TagEntityData(
            type = TagType.DEADLINE,
            json = gson.toJson(TagPayloads.Deadline(start = tag.start?.toIso(), end = tag.end?.toIso()))
        )
        is Tag.Priority -> TagEntityData(
            type = TagType.PRIORITY,
            json = gson.toJson(TagPayloads.Priority(level = tag.level))
        )
    }
}

private data class TagEntityData(
    val type: TagType,
    val json: String
)