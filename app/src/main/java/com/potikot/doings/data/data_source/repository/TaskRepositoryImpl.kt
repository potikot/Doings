package com.potikot.doings.data.data_source.repository

import android.util.Log
import com.potikot.doings.data.data_source.dao.TaskDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.domain.repository.TagRepository
import com.potikot.doings.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao,
    private val childRepository: TagRepository
) : TaskRepository {
    override suspend fun insert(parentId: ColumnId, task: Task): TaskId {
        val id = TaskId(dao.insert(task.toEntity(parentId)))
        task.tags.forEach {
            childRepository.insert(id, it)
        }
        // Log.d("Task Repository", "Added task ${task.name}(${task.id.value}) to column $'${parentId.value}'")
        return id
    }

    override suspend fun delete(id: TaskId) {
        dao.deleteById(id.value)
    }

    override suspend fun update(parentId: ColumnId, task: Task, withChildren: Boolean) {
        dao.update(task.toEntity(parentId))
        if (withChildren) {
            task.tags.forEach {
                childRepository.update(task.id, it)
            }
        }
    }

    override suspend fun updateIsDone(id: TaskId, isDone: Boolean) {
        dao.updateIsCompleted(id.value, isDone)
    }

    override suspend fun updatePosition(id: TaskId, position: Int) {
        dao.updatePosition(id.value, position)
    }

    override suspend fun updateColumn(id: TaskId, targetColumnId: ColumnId) {
        dao.updateColumn(id.value, targetColumnId.value)
    }

    override fun get(id: TaskId): Flow<Task?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getAllFromColumn(id: ColumnId): Flow<List<Task>> {
        return dao.getAllFromColumn(id.value).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun count(): Int {
        return dao.count()
    }

    override suspend fun count(parentId: ColumnId): Int {
        return dao.count(parentId.value)
    }
}

