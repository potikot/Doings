package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun insert(parentId: AccountId, project: Project): ProjectId
    suspend fun delete(id: ProjectId)

    suspend fun update(parentId: AccountId, project: Project, withChildren: Boolean = true)
    suspend fun updateSelectedBoard(id: ProjectId, boardId: BoardId?)
    suspend fun updateName(id: ProjectId, name: String)

    fun get(id: ProjectId): Flow<Project?>
    fun getAllFromAccount(id: AccountId): Flow<List<Project>>
}