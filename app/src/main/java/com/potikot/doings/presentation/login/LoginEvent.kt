package com.potikot.doings.presentation.login

sealed class LoginEvent {
    data class ListCompanies(val email: String, val password: String) : LoginEvent()
    data class ListKeys(val email: String, val password: String, val companyId: String) : LoginEvent()
    data class AddKey(val email: String, val password: String, val companyId: String) : LoginEvent()
    data class SelectKey(val key: String) : LoginEvent()
    object Login : LoginEvent()
}