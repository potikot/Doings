package com.potikot.doings.presentation.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.presentation.components.BottomOptionsSheet
import com.potikot.doings.presentation.components.LoadingView
import com.potikot.doings.presentation.components.TextFieldDialogue
import com.potikot.doings.presentation.project.components.ProjectCard
import com.potikot.doings.presentation.project.components.TagPopupDialog
import com.potikot.doings.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    navController: NavController,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val options by viewModel.options.collectAsState()
    val taskMoveOptions by viewModel.taskMoveOptions.collectAsState()
    val tagOptions by viewModel.tagOptions.collectAsState()
    val shouldDismissTMO by viewModel.shouldDismissTMO.collectAsState()

    var isCreatingBoard by remember { mutableStateOf(false) }
    var isCreatingColumn by remember { mutableStateOf(false) }
    var isCreatingTask by remember { mutableStateOf(false) }
    var columnForTask by remember { mutableStateOf<ColumnId?>(null) }

    if (isCreatingTask) {
        TextFieldDialogue(
            initialValue = "",
            placeholder = "Введите название...",
            confirmText = "Добавить задачу",
            dismissText = "Отменить",
            onValueChange = { },
            onDismissRequest = { isCreatingTask = false },
            onConfirm = {
                isCreatingTask = false
                if (columnForTask != null) {
                    viewModel.sendEvent(ProjectEvent.AddTask(columnForTask!!, it.trim(), null))
                }
            }
        )
    }

    if (isCreatingColumn) {
        TextFieldDialogue(
            initialValue = "",
            placeholder = "Введите название...",
            confirmText = "Добавить колонку",
            dismissText = "Отменить",
            onValueChange = { },
            onDismissRequest = { isCreatingColumn = false },
            onConfirm = {
                isCreatingColumn = false
                viewModel.sendEvent(ProjectEvent.AddColumn(it.trim()))
            }
        )
    }

    if (isCreatingBoard) {
        TextFieldDialogue(
            initialValue = "",
            placeholder = "Введите название...",
            confirmText = "Добавить доску",
            dismissText = "Отменить",
            onValueChange = { },
            onDismissRequest = { isCreatingBoard = false },
            onConfirm = {
                isCreatingBoard = false
                viewModel.sendEvent(ProjectEvent.AddBoard(it.trim()))
            }
        )
    }

    RenameDialogues(state, viewModel)
    TagPopupDialog(
        state = state.tagDialog,
        onEvent = { viewModel.sendEvent(it) },
        modifier = Modifier
    )

    Scaffold(
//        floatingActionButton = {
//            IconFloatingActionButton(
//                onClick = {
//                    isCreatingBoard = state.boardId == null
//                    isCreatingColumn = !isCreatingBoard && state.columnId == null
//                    isCreatingTask = !isCreatingColumn && !isCreatingBoard
//                  },
//                imageVector = Icons.Default.Add,
//                contentDescription = "Add hierarchy element"
//            )
//        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                LoadingView(
                    text = "Загрузка...",
                    modifier = Modifier.padding(innerPadding)
                )
            }
            state.project == null -> { }
            else -> {
                ProjectCard(
                    project = state.project as Project,
                    modifier = Modifier,
                    onNavigateBack = { navController.navigate(Screen.Main.route) },
                    onCreateBoard = { name -> isCreatingBoard = true }, // todo: fix
                    onSelectBoard = { id -> viewModel.sendEvent(ProjectEvent.SelectBoard(id)) },
                    onOpenOptions = { id -> viewModel.sendEvent(ProjectEvent.OpenOptions(id)) },
                    onCreateColumn_Board = { name -> isCreatingColumn = true }, // todo: fix
                    onSelectColumn_Board = { id -> viewModel.sendEvent(ProjectEvent.SelectColumn(id)) },
                    onCreateTask_Column = { id, name, description -> columnForTask = id; isCreatingTask = true },
                    onOpenOptions_Column = { id -> viewModel.sendEvent(ProjectEvent.OpenOptions(id)) },
                    onDelete_Column = { id -> viewModel.sendEvent(ProjectEvent.DeleteColumn(id)) },
                    onToggleDone_Task = { id, isCompleted -> viewModel.sendEvent(ProjectEvent.ToggleTaskCompleted(id, isCompleted)) },
                    onOpenOptions_Task = { id -> viewModel.sendEvent(ProjectEvent.OpenOptions(id)) },
                    onTagClick_Task = { taskId, tag -> viewModel.sendEvent(ProjectEvent.OpenTagDialog(taskId, tag)) }
                )

                if (taskMoveOptions != null) {
                    BottomOptionsSheet(
                        options = taskMoveOptions!!,
                        onDismiss = { if (shouldDismissTMO) viewModel.sendEvent(ProjectEvent.DismissOptions(1)) }
                    )
                }

                if (options != null) {
                    BottomOptionsSheet(
                        options = options!!,
                        onDismiss = { viewModel.sendEvent(ProjectEvent.DismissOptions(0)) }
                    )
                }

                if (tagOptions != null) {
                    BottomOptionsSheet(
                        options = tagOptions!!,
                        onDismiss = { viewModel.sendEvent(ProjectEvent.DismissOptions(2)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RenameDialogues(
    state: ProjectViewState,
    viewModel: ProjectViewModel
) {
    state.projectToRename?.let { project ->
        TextFieldDialogue(
            initialValue = project.name,
            placeholder = "Введите название...",
            confirmText = "Переименовать",
            onValueChange = { },
            onDismissRequest = { viewModel.sendEvent(ProjectEvent.DismissRenameDialog) },
            onConfirm = { viewModel.sendEvent(ProjectEvent.ConfirmRenameDialog(it)) }
        )
    }

    state.boardToRename?.let { board ->
        TextFieldDialogue(
            initialValue = board.name,
            placeholder = "Введите название...",
            confirmText = "Переименовать",
            onValueChange = { },
            onDismissRequest = { viewModel.sendEvent(ProjectEvent.DismissRenameDialog) },
            onConfirm = { viewModel.sendEvent(ProjectEvent.ConfirmRenameDialog(it)) }
        )
    }

    state.columnToRename?.let { column ->
        TextFieldDialogue(
            initialValue = column.name,
            placeholder = "Введите название...",
            confirmText = "Переименовать",
            onValueChange = { },
            onDismissRequest = { viewModel.sendEvent(ProjectEvent.DismissRenameDialog) },
            onConfirm = { viewModel.sendEvent(ProjectEvent.ConfirmRenameDialog(it)) }
        )
    }

    state.taskToRename?.let { task ->
        TextFieldDialogue(
            initialValue = task.name,
            placeholder = "Введите задачу...",
            confirmText = "Сохранить",
            onValueChange = { },
            onDismissRequest = { viewModel.sendEvent(ProjectEvent.DismissRenameDialog) },
            onConfirm = { viewModel.sendEvent(ProjectEvent.ConfirmRenameDialog(it)) }
        )
    }
}