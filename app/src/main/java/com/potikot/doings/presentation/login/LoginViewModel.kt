package com.potikot.doings.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potikot.doings.domain.remote.util.ApiResponse
import com.potikot.doings.domain.remote.yougile.YougileApi
import com.potikot.doings.domain.remote.yougile.request.CreateKey
import com.potikot.doings.domain.remote.yougile.request.GetCompanies
import com.potikot.doings.domain.remote.yougile.request.GetKeys
import com.potikot.doings.domain.remote.yougile.response.GetCompaniesResponse
import com.potikot.doings.domain.remote.yougile.response.GetKeysResponse
import com.potikot.doings.domain.repository.AppDataRepository
import com.potikot.doings.presentation.util.ListElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val list: List<ListElement>? = null,
    val lastElement: ListElement? = null,
    val companies: GetCompaniesResponse? = null,
    val keys: List<GetKeysResponse>? = null,
    val isHandlingRequest: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val yougileApi: YougileApi
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private var _selectedApiKey: String? = null

    // todo: should i null other values when list another one?
    fun sendEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.ListCompanies -> getCompanies(event.email, event.password)
            is LoginEvent.ListKeys -> getKeys(event.email, event.password, event.companyId)
            is LoginEvent.AddKey -> addKey(event.email, event.password, event.companyId)
            is LoginEvent.SelectKey -> selectKey(event.key)
            is LoginEvent.Login -> login()
        }
    }

    private fun login() {
        if (_selectedApiKey == null) return

    }

    private fun getCompanies(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isHandlingRequest = true) }
            val response = yougileApi.request(GetCompanies(email, password))
            val data = getData(response)

            _state.update { it.copy(
                isHandlingRequest = false,
                companies = data,
                list = data?.content?.map { c -> ListElement(
                    label = c.name,
                    onClick = { sendEvent(LoginEvent.ListKeys(email, password, c.id)) }
                ) },
                lastElement = null
            ) }
        }
    }

    private fun getKeys(email: String, password: String, companyId: String) {
        viewModelScope.launch {
            _state.update { it.copy(companies = null, isHandlingRequest = true) }
            val response = yougileApi.request(GetKeys(email, password, companyId))
            val data = getData(response)

            _state.update { it.copy(
                isHandlingRequest = false,
                keys = data,
                list = data?.map { k -> ListElement(
                    label = k.key,
                    onClick = { sendEvent(LoginEvent.SelectKey(k.key)) }
                ) },
                lastElement = ListElement("Добавить ключ", { sendEvent(LoginEvent.AddKey(email, password, companyId)) })
            ) }
        }
    }

    private fun resetScreen() {
        _state.update { it.copy(
            isHandlingRequest = false,
            list = null,
            lastElement = null,
            keys = null,
            companies = null,
        ) }
    }

    private fun addKey(email: String, password: String, companyId: String) {
        viewModelScope.launch {
            _state.update { it.copy(keys = null, isHandlingRequest = true) }
            yougileApi.request(CreateKey(email, password, companyId))
            sendEvent(LoginEvent.ListKeys(email, password, companyId))
        }
    }

    private fun selectKey(key: String) {
        appDataRepository.setYougileApiKey(key)
    }

    private fun <T> getData(response: ApiResponse<T>): T? {
        return when(response) {
            is ApiResponse.Success -> response.data
            is ApiResponse.Error -> null
        }
    }
}