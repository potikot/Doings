package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import kotlinx.coroutines.flow.Flow

interface ColumnRepository {
    suspend fun insert(parentId: BoardId, column: Column): ColumnId
    suspend fun delete(id: ColumnId)

    suspend fun update(parentId: BoardId, column: Column, withChildren: Boolean = true)
    suspend fun updatePosition(id: ColumnId, position: Int)

    fun get(id: ColumnId): Flow<Column?>
    fun getAllFromBoard(id: BoardId): Flow<List<Column>>
}