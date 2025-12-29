package com.potikot.doings.presentation.project

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potikot.doings.R
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ID
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.model.TagId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.domain.repository.AppDataRepository
import com.potikot.doings.domain.use_case.ProjectUseCases
import com.potikot.doings.presentation.util.OptionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectViewState(
    val isLoading: Boolean = false,
    val project: Project? = null,
    val boardId: BoardId? = null,
    val columnId: ColumnId? = null,
    val projectToRename: Project? = null,
    val boardToRename: Board? = null,
    val columnToRename: Column? = null,
    val taskToRename: Task? = null,
    val taskToMove: TaskId? = null,
)

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val useCases: ProjectUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectViewState())
    val state: StateFlow<ProjectViewState> = _state

    private val _options = MutableStateFlow<List<OptionItem>?>(null)
    val options: StateFlow<List<OptionItem>?> = _options

    private val _taskMoveOptions = MutableStateFlow<List<OptionItem>?>(null)
    val taskMoveOptions: StateFlow<List<OptionItem>?> = _taskMoveOptions

    private val _shouldDismissTMO = MutableStateFlow(false)
    val shouldDismissTMO: StateFlow<Boolean> = _shouldDismissTMO.asStateFlow()

    init {
        savedStateHandle.get<Long>("projectId")?.let { numId ->
            loadProject(ProjectId(numId))
        }
    }

    fun sendEvent(event: ProjectEvent) {
        when(event) {
            is ProjectEvent.DeleteProject -> deleteProject()

            is ProjectEvent.AddBoard -> addBoard(event.name)
            is ProjectEvent.DeleteBoard -> deleteBoard(event.id)
            is ProjectEvent.SelectBoard -> selectBoard(event.id)

            is ProjectEvent.AddColumn -> addColumn(event.name)
            is ProjectEvent.DeleteColumn -> deleteColumn(event.id)
            is ProjectEvent.SelectColumn -> {
                // Log.w("ProjectViewModel", "Select column call for id: ${event.id}")
                selectColumn(event.id)
            }
            is ProjectEvent.OpenOptions -> _options.value = createOptionsGeneric(event.id)
            is ProjectEvent.OpenRenameDialog -> openRenameDialog(event.id)
            is ProjectEvent.ConfirmRenameDialog -> closeRenameDialog(true, event.newName)
            is ProjectEvent.DismissRenameDialog -> closeRenameDialog(false, "")

            is ProjectEvent.AddTask -> addTask(event.columnId, event.name, event.description)
            is ProjectEvent.DeleteTask -> deleteTask(event.id)
            is ProjectEvent.ToggleTaskCompleted -> toggleTaskCompleted(event.id, event.isCompleted)
            is ProjectEvent.MoveTask -> moveTask(event.id, event.targetColumnId)

            is ProjectEvent.DismissOptions -> {
                if (event.id == 0) {
                    _options.value = null
                } else {
                    _taskMoveOptions.value = null
                }
            } // todo: rework
        }
    }

    // region OnEvent

    private fun createOptionsGeneric(id: ID): List<OptionItem> {
        return when(id) {
            is AccountId -> listOf()
            is ProjectId -> createProjectOptions(id)
            is BoardId -> createBoardOptions(id)
            is ColumnId -> createColumnOptions(id)
            is TaskId -> createTaskOptions(id)
            is TagId -> listOf()
        }
    }

    private fun deleteProject() {
        viewModelScope.launch {
            val project = _state.value.project ?: return@launch
            useCases.deleteProject(project.id)
        }
    }

    private fun addBoard(name: String) {
        viewModelScope.launch {
            val project = _state.value.project ?: return@launch
            val boardId = useCases.addOrUpdateBoard(project.id, Board(
                name = name,
                position = project.boards.size,
                selectedColumnId = null,
                columns = emptyList()
            ))

            delay(50)
            selectBoard(boardId)

            // Log.d("ProjectViewModel", "AddedBoard '$boardId' to project '${project.id}'")
        }
    }

    private fun deleteBoard(id: BoardId) {
        viewModelScope.launch {
            val project = _state.value.project ?: return@launch
            var newSelectedId: BoardId? = null

            if (_state.value.boardId == id) {
                val currentBoardIndex = project.boards.indexOfFirst { it.id == id }

                newSelectedId = if (project.boards.size <= 1) null else {
                    when {
                        currentBoardIndex == 0 -> project.boards[1].id
                        else -> project.boards[currentBoardIndex - 1].id
                    }
                }
            }

            useCases.deleteBoard(id)
            delay(50)
            selectBoard(newSelectedId)
        }
    }

    private fun selectBoard(id: BoardId?) {
        viewModelScope.launch {
            if (id == _state.value.boardId) return@launch
            val project = _state.value.project ?: return@launch
            useCases.selectBoard(project.id, id)
            val board = project.boards.firstOrNull { it.id == id }
            _state.update { it.copy(boardId = id, columnId = board?.run { selectedColumnId }) }
            // Log.e("ProjectViewModel", "SelectedBoard '${board?.id}' in project '${project.name}' with column '${board?.selectedColumnId}'")
        }
    }

    private fun addColumn(name: String) {
        viewModelScope.launch {
            // Log.d("ProjectViewModel", "Trying add column '$name' to board '${_state.value.boardId}' in project '#${_state.value.project?.name}'")
            val project = _state.value.project ?: return@launch
            val boardId = _state.value.boardId ?: return@launch
            val columnId = useCases.addOrUpdateColumn(boardId, Column(
                name = name,
                position = project.let { p ->
                    val board = p.boards.firstOrNull { it.id == boardId } ?: return@launch
                    board.columns.size
                },
                tasks = emptyList()
            ))

            delay(50)
            selectColumn(columnId)

            // Log.d("ProjectViewModel", "AddedColumn '$columnId' to board '$boardId'")
        }
    }

    private fun deleteColumn(id: ColumnId) {
        viewModelScope.launch {
            val project = _state.value.project ?: return@launch
            val board = project.boards.firstOrNull { it.id == _state.value.boardId } ?: return@launch
            var newSelectedId: ColumnId? = null

            if (_state.value.columnId == id) {
                val columnIndex = board.columns.indexOfFirst { it.id == id }

                newSelectedId = if (board.columns.size <= 1) null else {
                    when {
                        columnIndex == 0 -> board.columns[1].id
                        else -> board.columns[columnIndex - 1].id
                    }
                }
            }

            useCases.deleteColumn(id)
            selectColumn(newSelectedId)
            _state.update { it.copy(columnId = newSelectedId) }
        }
    }

    private fun selectColumn(id: ColumnId?) {
        viewModelScope.launch {
            if (id == _state.value.columnId) return@launch
            val boardId = _state.value.boardId ?: return@launch
            useCases.selectColumn(boardId, id)
            _state.update { it.copy(columnId = id) }
            // Log.e("ProjectViewModel", "SelectedColumn '$id' in board '$boardId'")
        }
    }

    fun createProjectOptions(id: ProjectId): List<OptionItem> {
        return listOf(
            OptionItem(
                title = "Переименовать",
                icon = Icons.Default.Edit,
                action = { sendEvent(ProjectEvent.OpenRenameDialog(id)) }
            ),
            OptionItem(
                title = "Удалить",
                icon = Icons.Default.Delete,
                action = { sendEvent(ProjectEvent.DeleteProject(id)) }
            ),
        )
    }

    fun createBoardOptions(id: BoardId): List<OptionItem> {
        return listOf(
            OptionItem(
                title = "Переименовать",
                icon = Icons.Default.Edit,
                action = { sendEvent(ProjectEvent.OpenRenameDialog(id)) }
            ),
            OptionItem(
                title = "Удалить",
                icon = Icons.Default.Delete,
                action = { sendEvent(ProjectEvent.DeleteBoard(id)) }
            ),
        )
    }

    private fun createColumnOptions(id: ColumnId): List<OptionItem> {
        return listOf(
            OptionItem(
                title = "Переименовать",
                icon = Icons.Default.Edit,
                action = { sendEvent(ProjectEvent.OpenRenameDialog(id)) }
            ),
            OptionItem(
                title = "Удалить",
                icon = Icons.Default.Delete,
                action = { sendEvent(ProjectEvent.DeleteColumn(id)) }
            ),
        )
    }

    private fun createTaskOptions(id: TaskId): List<OptionItem> {
        return listOf(
            OptionItem(
                title = "Редактировать",
                icon = Icons.Filled.Edit,
                action = { sendEvent(ProjectEvent.OpenRenameDialog(id)) }
            ),
            OptionItem(
                title = "Переместить",
                icon = Icons.Filled.Send,
                action = {
                    _shouldDismissTMO.value = false
                    _taskMoveOptions.value = createBoardSelectionList(id)
                }
            ),
            OptionItem(
                title = "Удалить",
                icon = Icons.Filled.Delete,
                action = { sendEvent(ProjectEvent.DeleteTask(id)) }
            ),
        )
    }

    private fun createBoardSelectionList(id: TaskId): List<OptionItem> {
        val boards = _state.value.project?.boards ?: return emptyList()
        var list = listOf<OptionItem>()
        for (board in boards) {
            if (board.columns.isEmpty())
                continue

            list = list.plus(OptionItem(
                title = board.name,
                icon = Icons.Default.KeyboardArrowRight,
                action = {
                    _taskMoveOptions.value = createColumnSelectionList(board, id)
                    _shouldDismissTMO.value = true
                }
            ))
        }

        return list
    }

    private fun createColumnSelectionList(board: Board, id: TaskId): List<OptionItem> {
        var list = listOf<OptionItem>()
        for (item in board.columns) {
            list = list.plus(OptionItem(
                title = item.name,
                rightIcon = Icons.Default.KeyboardArrowRight,
                action = { sendEvent(ProjectEvent.MoveTask(id, item.id)) }
            ))
        }

        return list
    }


    private fun openRenameDialog(id: ID) {
        val project = _state.value.project ?: return
        when (id) {
            is AccountId -> throw NotImplementedError()
            is ProjectId -> {
                _state.update { it.copy(projectToRename = project) }
            }
            is BoardId -> {
                val board = project.boards.firstOrNull { it.id == id } ?: return
                _state.update { it.copy(boardToRename = board) }
            }
            is ColumnId -> {
                val board = project.boards.firstOrNull { it.id == _state.value.boardId } ?: return
                val column = board.columns.firstOrNull { it.id == id } ?: return
                _state.update { it.copy(columnToRename = column) }
            }
            is TaskId -> {
                val board = project.boards.firstOrNull { it.id == _state.value.boardId } ?: return
                val column = board.columns.firstOrNull { it.id == _state.value.columnId } ?: return
                val task = column.tasks.firstOrNull { it.id == id } ?: return
                _state.update { it.copy(taskToRename = task) }
            }
            is TagId -> throw NotImplementedError()
        }
    }

    private fun closeRenameDialog(isConfirmed: Boolean, newName: String) {
        if (isConfirmed && newName.isNotBlank()) {
            viewModelScope.launch {
                _state.value.projectToRename?.let { project ->
                    useCases.addOrUpdateProject(appDataRepository.currentAccountId.value ?: return@launch, project.copy(name = newName))
                }
                _state.value.boardToRename?.let { board ->
                    useCases.addOrUpdateBoard(_state.value.project!!.id, board.copy(name = newName))
                }
                _state.value.columnToRename?.let { column ->
                    useCases.addOrUpdateColumn(_state.value.boardId!!, column.copy(name = newName))
                }
                _state.value.taskToRename?.let { task ->
                    useCases.addOrUpdateTask(_state.value.columnId!!, task.copy(name = newName))
                }
            }
        }

        _state.update {
            it.copy(
                projectToRename = null,
                boardToRename = null,
                columnToRename = null,
                taskToRename = null
            )
        }
    }

    private fun addTask(columnId: ColumnId, name: String, description: String?) {
        viewModelScope.launch {
//            val columnId = _state.value.columnId ?: return@launch
            useCases.addOrUpdateTask(columnId, Task(
                name = name,
                description = description,
                isCompleted = false,
                position = useCases.getTasksCount(columnId),
            ))
        }
    }

    private fun deleteTask(id: TaskId) {
        viewModelScope.launch {
            useCases.deleteTask(id)
        }
    }

    private fun toggleTaskCompleted(id: TaskId, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.toggleTaskCompleted(id, isCompleted)
        }
    }

    private fun moveTask(id: TaskId, targetColumnId: ColumnId) {
        viewModelScope.launch {
            useCases.moveTask(id, targetColumnId)
        }
    }

    // endregion

    private fun loadProject(id: ProjectId) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            useCases.getProject(id).collect { project ->
                val boardId = project?.selectedBoardId
                val columnId = project?.boards?.firstOrNull { it.id == boardId }?.selectedColumnId
                _state.update {
                    it.copy(
                        isLoading = false,
                        project = project,
                        boardId = boardId,
                        columnId = columnId
                    )
                }
            }
        }
    }
}