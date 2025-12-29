package com.potikot.doings.data.data_source.repository

import android.util.Log
import com.potikot.doings.data.data_source.dao.BoardDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.repository.BoardRepository
import com.potikot.doings.domain.repository.ColumnRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val dao: BoardDao,
    private val childRepository: ColumnRepository
) : BoardRepository {
    override suspend fun insert(parentId: ProjectId, board: Board): BoardId {
        val id = BoardId(dao.insert(board.toEntity(parentId)))
        board.columns.forEach {
            childRepository.insert(id, it)
        }
        return id
    }

    override suspend fun delete(id: BoardId) {
        dao.deleteById(id.value)
    }

    override suspend fun update(parentId: ProjectId, board: Board, withChildren: Boolean) {
        dao.update(board.toEntity(parentId))
        if (withChildren) {
            board.columns.forEach {
                childRepository.update(board.id, it, true)
            }
        }
    }

    override suspend fun updatePosition(id: BoardId, position: Int) {
        dao.updatePosition(id.value, position)
    }

    override suspend fun updateSelectedColumn(id: BoardId, columnId: ColumnId?) {
        // Log.d("Board Repository", "Update selected column to '${columnId?.value}'")
        dao.updateSelectedColumn(id.value, columnId?.value)
    }

    override fun get(id: BoardId): Flow<Board?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getAllFromProject(id: ProjectId): Flow<List<Board>> {
        return dao.getAllFromProject(id.value).map { list -> list.map { it.toDomain() } }
    }
}
