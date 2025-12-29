package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.AccountId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppDataRepository {
    private var _currentAccountId = MutableStateFlow<AccountId?>(null)
    val currentAccountId = _currentAccountId.asStateFlow()

    fun setCurrentAccountId(id: AccountId) {
        _currentAccountId.value = id
    }
}