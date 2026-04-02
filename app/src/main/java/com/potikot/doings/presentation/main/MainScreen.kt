package com.potikot.doings.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potikot.doings.presentation.components.BottomOptionsSheet
import com.potikot.doings.presentation.components.IconFloatingActionButton
import com.potikot.doings.presentation.components.ListProjectItem
import com.potikot.doings.presentation.components.TextFieldDialogue
import com.potikot.doings.presentation.components.TopOptionsSheet
import com.potikot.doings.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val account = state.account

    val bottomOptions by viewModel.options.collectAsState()

    var isCreatingProject by remember { mutableStateOf(false) }

    if (isCreatingProject) {
        TextFieldDialogue(
            initialValue = "",
            placeholder = "Введите название...",
            confirmText = "Добавить проект",
            dismissText = "Отменить",
            onValueChange = { },
            onDismissRequest = { isCreatingProject = false },
            onConfirm = {
                isCreatingProject = false
                viewModel.sendEvent(MainEvent.AddProject(it.trim()))
            }
        )
    }

    state.projectToRename?.let { project ->
        TextFieldDialogue(
            initialValue = project.name,
            placeholder = "Введите название...",
            confirmText = "Переименовать",
            onValueChange = { },
            onDismissRequest = { viewModel.sendEvent(MainEvent.DismissRenameProjectDialog) },
            onConfirm = { viewModel.sendEvent(MainEvent.ConfirmRenameProjectDialog(it)) }
        )
    }

    bottomOptions?.let {
        BottomOptionsSheet(
            options = it,
            onDismiss = { viewModel.sendEvent(MainEvent.DismissOptions) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Проекты") },
                modifier = Modifier.padding(16.dp),
                actions = {
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Manage account",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            IconFloatingActionButton(
                onClick = { isCreatingProject = true },
                imageVector = Icons.Default.Add,
                contentDescription = "Add project"
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        if (account == null) {
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding).padding(bottom = 64.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(account.projects) { project ->
                ListProjectItem(
                    project = project,
                    onClick = { navController.navigate(Screen.Project.route + "/${project.id.value}") },
                    onOpenOptions = { viewModel.sendEvent(MainEvent.OpenProjectOptions(project.id)) }
                )
            }
        }
    }
}