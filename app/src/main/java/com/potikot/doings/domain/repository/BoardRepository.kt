package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ProjectId
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    suspend fun insert(parentId: ProjectId, board: Board): BoardId
    suspend fun delete(id: BoardId)

    suspend fun update(parentId: ProjectId, board: Board, withChildren: Boolean = true)
    suspend fun updatePosition(id: BoardId, position: Int)
    suspend fun updateSelectedColumn(id: BoardId, columnId: ColumnId?)

    fun get(id: BoardId): Flow<Board?>
    fun getAllFromProject(id: ProjectId): Flow<List<Board>>
}