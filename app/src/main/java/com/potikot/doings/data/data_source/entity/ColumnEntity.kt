package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "columns",
    foreignKeys = [
        ForeignKey(
            entity = BoardEntity::class,
            parentColumns = ["id"],
            childColumns = ["board_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("board_id")]
)
data class ColumnEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "board_id")
    val boardId: Long,
    val name: String,
    val position: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)

