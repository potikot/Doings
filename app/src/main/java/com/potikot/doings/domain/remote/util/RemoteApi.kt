package com.potikot.doings.domain.remote.util

import com.potikot.doings.data.data_source.entity.BoardEntity
import com.potikot.doings.data.data_source.entity.ColumnEntity
import com.potikot.doings.data.data_source.entity.ProjectEntity
import com.potikot.doings.data.data_source.entity.TaskEntity
import com.potikot.doings.domain.remote.yougile.YougileApi.Companion.API_ROOT
import com.potikot.doings.domain.remote.yougile.request.ApiRequest
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.reflect.typeInfo

abstract class RemoteApi<TBRequest, TBResponse> {

}

/*
    suspend fun addTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun getTask(id: String): TaskEntity
    suspend fun getTasks(offset: Int, limit: Int = 50): List<TaskEntity>

    suspend fun addColumn(task: ColumnEntity)
    suspend fun updateColumn(task: ColumnEntity)
    suspend fun deleteColumn(task: ColumnEntity)
    suspend fun getColumn(id: String): ColumnEntity
    suspend fun getColumns(offset: Int, limit: Int = 50): List<ColumnEntity>

    suspend fun addBoard(task: BoardEntity)
    suspend fun updateBoard(task: BoardEntity)
    suspend fun deleteBoard(task: BoardEntity)
    suspend fun getBoard(id: String): BoardEntity
    suspend fun getBoards(offset: Int, limit: Int = 50): List<BoardEntity>

    suspend fun addProject(task: ProjectEntity)
    suspend fun updateProject(task: ProjectEntity)
    suspend fun deleteProject(task: ProjectEntity)
    suspend fun getProject(id: String): ProjectEntity
    suspend fun getProjects(offset: Int, limit: Int = 50): List<ProjectEntity>
*/