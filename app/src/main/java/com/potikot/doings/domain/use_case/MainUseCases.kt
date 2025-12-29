package com.potikot.doings.domain.use_case

data class MainUseCases(
    val getAccount: GetAccountUseCase,
    val getAccounts: GetAccountsUseCase,

    val addOrUpdateProject: AddOrUpdateProjectUseCase,
    val deleteProject: DeleteProjectUseCase,
    val updateProjectName: UpdateProjectNameUseCase,
)