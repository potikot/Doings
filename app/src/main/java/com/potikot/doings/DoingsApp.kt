package com.potikot.doings

import android.app.Application
import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.repository.AccountRepository
import com.potikot.doings.domain.repository.AppDataRepository
import com.potikot.doings.domain.util.AccountProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DoingsApp : Application() {
    @Inject
    lateinit var accountRepository: AccountRepository
    @Inject
    lateinit var appDataRepository: AppDataRepository

    override fun onCreate() {
        super.onCreate()
        appDataRepository.setAccountProvider(AccountProvider.Local)
        initDataStorage()
    }

    private fun initDataStorage() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val accountCount = accountRepository.count()
            if (accountCount > 0) return@launch

            val id = accountRepository.insert(Account(name = "Local"))

            appDataRepository.setAccountId(id)
        }
    }
}