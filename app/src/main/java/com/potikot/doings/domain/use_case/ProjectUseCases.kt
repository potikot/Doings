package com.potikot.doings.domain.use_case

import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.domain.repository.AccountRepository
import com.potikot.doings.domain.repository.BoardRepository
import com.potikot.doings.domain.repository.ColumnRepository
import com.potikot.doings.domain.repository.ProjectRepository
import com.potikot.doings.domain.repository.TagRepository
import com.potikot.doings.domain.repository.TaskRepository

// Account

class AddOrUpdateAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(account: Account): AccountId = repository.insert(account)
}

class DeleteAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(account: Account) = repository.delete(account.id)
}

class GetAccountUseCase(private val repository: AccountRepository) {
    operator fun invoke(id: AccountId) = repository.get(id)
}

class GetAccountsUseCase(private val repository: AccountRepository) {
    operator fun invoke() = repository.getAll()
}

// Project

class AddOrUpdateProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(accountId: AccountId, project: Project): ProjectId = repository.insert(accountId, project)
}

class DeleteProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(id: ProjectId) = repository.delete(id)
    suspend operator fun invoke(project: Project) = repository.delete(project.id)
}

class GetProjectUseCase(private val repository: ProjectRepository) {
    operator fun invoke(id: ProjectId) = repository.get(id)
}

class GetProjectsUseCase(private val repository: ProjectRepository) {
    operator fun invoke(accountId: AccountId) = repository.getAllFromAccount(accountId)
}

class UpdateProjectNameUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(id: ProjectId, name: String) = repository.updateName(id, name)
}

// Board

class AddOrUpdateBoardUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(projectId: ProjectId, board: Board): BoardId = repository.insert(projectId, board)
}

class DeleteBoardUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(id: BoardId) = repository.delete(id)
    suspend operator fun invoke(board: Board) = repository.delete(board.id)
}

class GetBoardUseCase(private val repository: BoardRepository) {
    operator fun invoke(id: BoardId) = repository.get(id)
}

class GetBoardsUseCase(private val repository: BoardRepository) {
    operator fun invoke(projectId: ProjectId) = repository.getAllFromProject(projectId)
}

class SelectBoardUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(projectId: ProjectId, boardId: BoardId?) = repository.updateSelectedBoard(projectId, boardId)
}

// Column

class AddOrUpdateColumnUseCase(private val repository: ColumnRepository) {
    suspend operator fun invoke(boardId: BoardId, column: Column) = repository.insert(boardId, column)
}

class DeleteColumnUseCase(private val repository: ColumnRepository) {
    suspend operator fun invoke(id: ColumnId) = repository.delete(id)
    suspend operator fun invoke(column: Column) = repository.delete(column.id)
}

class GetColumnsUseCase(private val repository: ColumnRepository) {
    operator fun invoke(boardId: BoardId) = repository.getAllFromBoard(boardId)
}

class SelectColumnUseCase(private val repository: BoardRepository) {
    suspend operator fun invoke(boardId: BoardId, columnId: ColumnId?) = repository.updateSelectedColumn(boardId, columnId)
}

// Task

class AddOrUpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(columnId: ColumnId, task: Task) = repository.insert(columnId, task)
}

class ToggleTaskCompletedUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: TaskId, isCompleted: Boolean) = repository.updateIsDone(id, isCompleted)
}

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: TaskId) = repository.delete(id)
    suspend operator fun invoke(task: Task) = repository.delete(task.id)
}

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(columnId: ColumnId) = repository.getAllFromColumn(columnId)
}

class GetTasksCountUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(): Int = repository.count()
    suspend operator fun invoke(columnId: ColumnId): Int = repository.count(columnId)
}

class MoveTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: TaskId, targetColumnId: ColumnId) = repository.updateColumn(id, targetColumnId)
}

// Tag

class AddOrUpdateTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(taskId: TaskId, tag: Tag) = repository.insert(taskId, tag)
}

class DeleteTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(id: TagId) = repository.delete(id)
    suspend operator fun invoke(tag: Tag) = repository.delete(tag.common.id)
}

class GetTagsUseCase(private val repository: TagRepository) {
    operator fun invoke(taskId: TaskId) = repository.getAllFromTask(taskId)
}

data class ProjectUseCases(
    val addOrUpdateAccount: AddOrUpdateAccountUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val getAccount: GetAccountUseCase,
    val getAccounts: GetAccountsUseCase,

    val addOrUpdateProject: AddOrUpdateProjectUseCase,
    val deleteProject: DeleteProjectUseCase,
    val getProject: GetProjectUseCase,
    val getProjects: GetProjectsUseCase,

    val addOrUpdateBoard: AddOrUpdateBoardUseCase,
    val deleteBoard: DeleteBoardUseCase,
    val getBoard: GetBoardUseCase,
    val getBoards: GetBoardsUseCase,
    val selectBoard: SelectBoardUseCase,

    val addOrUpdateColumn: AddOrUpdateColumnUseCase,
    val deleteColumn: DeleteColumnUseCase,
    val getColumns: GetColumnsUseCase,
    val selectColumn: SelectColumnUseCase,

    val addOrUpdateTask: AddOrUpdateTaskUseCase,
    val toggleTaskCompleted: ToggleTaskCompletedUseCase,
    val deleteTask: DeleteTaskUseCase,
    val getTasks: GetTasksUseCase,
    val getTasksCount: GetTasksCountUseCase,
    val moveTask: MoveTaskUseCase,

    val addOrUpdateTag: AddOrUpdateTagUseCase,
    val deleteTag: DeleteTagUseCase,
    val getTags: GetTagsUseCase
)
