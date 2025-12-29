package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.TaskId
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    suspend fun insert(parentId: TaskId, tag: Tag): TagId
    suspend fun delete(id: TagId)

    suspend fun update(parentId: TaskId, tag: Tag)
    suspend fun updatePosition(id: TagId, position: Int)

    fun get(id: TagId): Flow<Tag?>
    fun getAllFromTask(id: TaskId): Flow<List<Tag>>
}
