package com.potikot.doings.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    val name: String,
    @ColumnInfo(name = "selected_project_id")
    val selectedProjectId: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)