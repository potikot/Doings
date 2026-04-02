package com.potikot.doings.presentation.main

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import com.potikot.doings.domain.remote.util.fetchAccount
import com.potikot.doings.domain.remote.yougile.YougileApi
import com.potikot.doings.domain.repository.AppDataRepository
import com.potikot.doings.domain.use_case.MainUseCases
import com.potikot.doings.domain.util.AccountProvider
import com.potikot.doings.presentation.util.OptionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainViewState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val projectToRename: Project? = null,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val useCases: MainUseCases,
    private val yougileApi: YougileApi,
) : ViewModel() {
    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state

    private val _options = MutableStateFlow<List<OptionItem>?>(null)
    val options: StateFlow<List<OptionItem>?> = _options

    init {
        loadAccount()
    }

    fun sendEvent(event: MainEvent) {
        when (event) {
            is MainEvent.AddProject -> addProject(event.name)
            is MainEvent.OpenRenameProjectDialog -> openRenameProjectDialog(event.id)
            is MainEvent.ConfirmRenameProjectDialog -> closeRenameProjectDialog(true, event.newName)
            is MainEvent.DismissRenameProjectDialog -> closeRenameProjectDialog(false, "")
            is MainEvent.DeleteProject -> deleteProject(event.id)
            is MainEvent.OpenProjectOptions -> _options.value = createProjectOptions(event.id)
            is MainEvent.DismissOptions -> _options.value = null
        }
    }

    // region OnEvent

    private fun addProject(name: String) {
        viewModelScope.launch {
            _state.value.account?.let {
                useCases.addOrUpdateProject(it.id, Project(
                    name = name
                ))
            }
        }
    }

    private fun deleteProject(id: ProjectId) {
        viewModelScope.launch {
            useCases.deleteProject(id)
        }
    }

    private fun openRenameProjectDialog(id: ProjectId) {
        val account = _state.value.account ?: return
        val project = account.projects.firstOrNull { it.id == id } ?: return
        _state.update { it.copy(projectToRename = project) }
    }

    private fun closeRenameProjectDialog(isConfirmed: Boolean, newName: String) {
        if (isConfirmed && !newName.isBlank()) {
            viewModelScope.launch {
                val projectToRename = _state.value.projectToRename ?: return@launch
                useCases.updateProjectName(projectToRename.id, newName)
            }
        }

        _state.update { it.copy(projectToRename = null) }
    }

    private fun createProjectOptions(id: ProjectId): List<OptionItem> {
        return listOf(
            OptionItem(
                title = "Rename",
                icon = Icons.Default.Edit,
                action = { sendEvent(MainEvent.OpenRenameProjectDialog(id)) }
            ),
            OptionItem(
                title = "Delete",
                icon = Icons.Default.Delete,
                action = { sendEvent(MainEvent.DeleteProject(id)) }
            )
        )
    }

    // endregion

    private fun loadAccount() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(appDataRepository.currentAccountProvider.value) {
                is AccountProvider.Local -> {
                    Log.d("Initialize Main Screen", "Local Account")
                    useCases.getAccounts().collect { accounts ->
                        val account = accounts.first()
                        appDataRepository.setAccountId(account.id)
                        _state.update { it.copy(isLoading = false, account = account) }
                    }
                }
                is AccountProvider.Yougile -> {
                    Log.d("Initialize Main Screen", "Yougile Account")
                    val remoteAccount = yougileApi.fetchAccount()
                    val externalId = remoteAccount.externalId ?: return@launch
                    val existingDbAccount = useCases.getExternalAccount(externalId).first()
                    val updatedAccount = if (existingDbAccount == null) {
                        remoteAccount
                    } else {
                        remoteAccount.copy(id = existingDbAccount.id)
                    }
                    val localAccountId = useCases.addOrUpdateAccount(updatedAccount)
                    appDataRepository.setAccountId(localAccountId)

                    useCases.getAccount(localAccountId).collect { finalAccount ->
                        _state.update { it.copy(isLoading = false, account = finalAccount) }
                    }
                }
                else -> {
                    throw Exception("Invalid Account Provider: ${appDataRepository.currentAccountProvider.value}")
                }
            }
        }
    }
}