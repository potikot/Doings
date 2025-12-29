package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("account_id")]
)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "account_id")
    val accountId: Long,
    val name: String,
    @ColumnInfo(name = "selected_board_id")
    val selectedBoardId: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)