package com.potikot.doings.presentation.project

import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ID
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.model.TaskId

sealed class ProjectEvent {
    data class DeleteProject(val id: ProjectId) : ProjectEvent()

    data class AddBoard(val name: String) : ProjectEvent()
    data class DeleteBoard(val id: BoardId) : ProjectEvent()
    data class SelectBoard(val id: BoardId) : ProjectEvent()

    data class AddColumn(val name: String) : ProjectEvent()
    data class DeleteColumn(val id: ColumnId) : ProjectEvent()
    data class SelectColumn(val id: ColumnId) : ProjectEvent()
    data class OpenOptions(val id: ID) : ProjectEvent()
    data class OpenRenameDialog(val id: ID) : ProjectEvent()
    data class ConfirmRenameDialog(val newName: String) : ProjectEvent()
    object DismissRenameDialog : ProjectEvent()

    data class AddTask(val columnId: ColumnId, val name: String, val description: String?) : ProjectEvent()
    data class DeleteTask(val id: TaskId) : ProjectEvent()
    data class ToggleTaskCompleted(val id: TaskId, val isCompleted: Boolean) : ProjectEvent()
    data class MoveTask(val id: TaskId, val targetColumnId: ColumnId) : ProjectEvent()

    data class DismissOptions(val id: Int) : ProjectEvent()
}