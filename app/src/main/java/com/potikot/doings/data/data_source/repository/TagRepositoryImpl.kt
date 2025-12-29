package com.potikot.doings.data.data_source.repository

import com.potikot.doings.data.data_source.dao.TagDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val dao: TagDao
) : TagRepository {
    override suspend fun insert(parentId: TaskId, tag: Tag): TagId {
        return TagId(dao.insert(tag.toEntity(parentId)))
    }

    override suspend fun delete(id: TagId) {
        dao.deleteById(id.value)
    }

    override suspend fun update(parentId: TaskId, tag: Tag) {
        dao.update(tag.toEntity(parentId))
    }

    override suspend fun updatePosition(id: TagId, position: Int) {
        dao.updatePosition(id.value, position)
    }

    override fun get(id: TagId): Flow<Tag?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getAllFromTask(id: TaskId): Flow<List<Tag>> {
        return dao.getAllFromTask(id.value).map { list -> list.map { it.toDomain() } }
    }
}
