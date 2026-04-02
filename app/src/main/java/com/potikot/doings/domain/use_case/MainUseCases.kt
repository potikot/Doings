package com.potikot.doings.domain.use_case

data class MainUseCases(
    val addOrUpdateAccount: AddOrUpdateAccountUseCase,
    val getAccount: GetAccountUseCase,
    val getExternalAccount: GetExternalAccountUseCase,
    val getAccounts: GetAccountsUseCase,

    val addOrUpdateProject: AddOrUpdateProjectUseCase,
    val deleteProject: DeleteProjectUseCase,
    val updateProjectName: UpdateProjectNameUseCase,
)