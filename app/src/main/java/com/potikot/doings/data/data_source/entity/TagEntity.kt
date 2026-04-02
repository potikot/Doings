package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.potikot.doings.data.data_source.util.TagType

// todo: change from store model { type, data } of polymorphic tags. Because SQL doesn't allow to query data inside json. Leads to performance issues in the future
@Entity(
    tableName = "task_tags",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("task_id")]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    val position: Int,
    val type: TagType,
    @ColumnInfo(name = "json_value")
    val jsonValue: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)