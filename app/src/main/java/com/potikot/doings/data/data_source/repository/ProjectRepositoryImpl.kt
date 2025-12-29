package com.potikot.doings.data.data_source.repository

import android.util.Log
import com.potikot.doings.data.data_source.dao.ProjectDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.repository.BoardRepository
import com.potikot.doings.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val dao: ProjectDao,
    private val childRepository: BoardRepository
) : ProjectRepository {
    override suspend fun insert(parentId: AccountId, project: Project): ProjectId {
        val id = ProjectId(dao.insert(project.toEntity(parentId)))
        project.boards.forEach {
            childRepository.insert(id, it)
        }
        return id
    }

    override suspend fun delete(id: ProjectId) {
        dao.deleteById(id.value)
    }

    override suspend fun update(parentId: AccountId, project: Project, withChildren: Boolean) {
        dao.update(project.toEntity(parentId))
        if (withChildren) {
            project.boards.forEach {
                childRepository.update(project.id, it, true)
            }
        }
    }

    override suspend fun updateSelectedBoard(id: ProjectId, boardId: BoardId?) {
        // Log.d(
//            "Project Repository",
//            "Update selected board to '${boardId?.let { dao.getById(it.value) }}(${boardId?.value})' in project '${
//                dao.getById(id.value)
//                    .first()?.project?.name ?: "NOT FOUND"
//            }()'"
//        )
        dao.updateSelectedBoard(id.value, boardId?.value)
    }

    override suspend fun updateName(id: ProjectId, name: String) {
        dao.updateName(id.value, name)
    }

    override fun get(id: ProjectId): Flow<Project?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getAllFromAccount(id: AccountId): Flow<List<Project>> {
        return dao.getAllFromAccount(id.value).map { list -> list.map { it.toDomain() } }
    }
}
