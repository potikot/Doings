package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ColumnEntity::class,
            parentColumns = ["id"],
            childColumns = ["column_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("column_id")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "column_id")
    val columnId: Long,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    val position: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)