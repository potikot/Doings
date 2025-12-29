package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "boards",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("project_id")]
)
data class BoardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "project_id")
    val projectId: Long,
    val name: String,
    val position: Int,
    @ColumnInfo(name = "selected_column_id")
    val selectedColumnId: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)