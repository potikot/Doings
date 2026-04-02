package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.util.AccountProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppDataRepository {
    private var _currentAccountProvider = MutableStateFlow<AccountProvider?>(null)
    val currentAccountProvider = _currentAccountProvider.asStateFlow()

    private var _currentYougileApiKey = MutableStateFlow<String?>(null)
    val currentYougileApiKey = _currentYougileApiKey.asStateFlow()

    private var _currentAccountId = MutableStateFlow<AccountId?>(null)
    val currentAccountId = _currentAccountId.asStateFlow()

    fun setAccountProvider(provider: AccountProvider) {
        _currentAccountProvider.value = provider
    }

    fun setYougileApiKey(key: String) {
        _currentYougileApiKey.value = key
    }

    fun setAccountId(id: AccountId) {
        _currentAccountId.value = id
    }

}