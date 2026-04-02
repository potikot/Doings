package com.potikot.doings.domain.repository

import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.AccountId
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun insert(account: Account): AccountId
    suspend fun delete(id: AccountId)
    suspend fun deleteAll()

    suspend fun update(account: Account, withChildren: Boolean = true)

    fun get(id: AccountId): Flow<Account?>
    fun getByExternalId(id: String): Flow<Account?>
    fun getAll(): Flow<List<Account>>

    fun count(): Int
}