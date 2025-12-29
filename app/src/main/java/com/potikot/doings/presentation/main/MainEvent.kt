package com.potikot.doings.presentation.main

import com.potikot.doings.domain.model.ProjectId

sealed class MainEvent {
    data class AddProject(val name: String) : MainEvent()
    data class DeleteProject(val id: ProjectId) : MainEvent()
    data class OpenRenameProjectDialog(val id: ProjectId) : MainEvent()
    data class ConfirmRenameProjectDialog(val newName: String) : MainEvent()
    object DismissRenameProjectDialog : MainEvent()

    data class OpenProjectOptions(val id: ProjectId) : MainEvent()
    object DismissOptions : MainEvent()
}