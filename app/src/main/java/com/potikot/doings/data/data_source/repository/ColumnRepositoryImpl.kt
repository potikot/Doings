package com.potikot.doings.data.data_source.repository

import android.util.Log
import com.potikot.doings.data.data_source.dao.ColumnDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.repository.ColumnRepository
import com.potikot.doings.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ColumnRepositoryImpl @Inject constructor(
    private val dao: ColumnDao,
    private val childRepository: TaskRepository
) : ColumnRepository {
    override suspend fun insert(parentId: BoardId, column: Column): ColumnId {
        val id = ColumnId(dao.insert(column.toEntity(parentId)))
        // Log.d("Column Repository", "id: ${id.value}, name: ${column.name}")
        column.tasks.forEach {
            childRepository.insert(id, it)
        }
        return id
    }

    override suspend fun delete(id: ColumnId) {
        dao.deleteById(id.value)
    }

    override suspend fun update(parentId: BoardId, column: Column, withChildren: Boolean) {
        dao.update(column.toEntity(parentId))
        if (withChildren) {
            column.tasks.forEach {
                childRepository.update(column.id, it, true)
            }
        }
    }

    override suspend fun updatePosition(id: ColumnId, position: Int) {
        dao.updatePosition(id.value, position)
    }

    override fun get(id: ColumnId): Flow<Column?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getAllFromBoard(id: BoardId): Flow<List<Column>> {
        return dao.getAllFromBoard(id.value).map { list -> list.map { it.toDomain() } }
    }
}

