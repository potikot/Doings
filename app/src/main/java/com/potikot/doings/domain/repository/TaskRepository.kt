package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insert(parentId: ColumnId, task: Task): TaskId
    suspend fun delete(id: TaskId)

    suspend fun update(parentId: ColumnId, task: Task, withChildren: Boolean = true)
    suspend fun updateIsDone(id: TaskId, isDone: Boolean)
    suspend fun updatePosition(id: TaskId, position: Int)
    suspend fun updateColumn(id: TaskId, targetColumnId: ColumnId)

    fun get(id: TaskId): Flow<Task?>
    fun getAllFromColumn(id: ColumnId): Flow<List<Task>>

    suspend fun count(): Int
    suspend fun count(parentId: ColumnId): Int
}