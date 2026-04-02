package com.potikot.doings.data.data_source.repository

import android.util.Log
import com.potikot.doings.data.data_source.dao.AccountDao
import com.potikot.doings.data.data_source.dao.ProjectDao
import com.potikot.doings.data.data_source.mappers.toDomain
import com.potikot.doings.data.data_source.mappers.toEntity
import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.repository.AccountRepository
import com.potikot.doings.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dao: AccountDao,
    private val childRepository: ProjectRepository
) : AccountRepository {
    override suspend fun insert(account: Account): AccountId {
        val id = AccountId(dao.insert(account.toEntity()))
        account.projects.forEach {
            childRepository.insert(id, it)
        }
        return id
    }

    override suspend fun delete(id: AccountId) {
        dao.deleteById(id.value)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun update(account: Account, withChildren: Boolean) {
        dao.update(account.toEntity())
        if (withChildren) {
            account.projects.forEach {
                childRepository.update(account.id, it, true)
            }
        }
    }

    override fun get(id: AccountId): Flow<Account?> {
        return dao.getById(id.value).map { it?.toDomain() }
    }

    override fun getByExternalId(id: String): Flow<Account?> {
        return dao.getByExternalId(id).map {
            if (it.isEmpty()) return@map null
            if (it.size > 1) {
                Log.e("AccountRepositoryImpl", "getByExternalId: Found more than one account with id $id")
            }

            it.first().toDomain()
        }
    }

    override fun getAll(): Flow<List<Account>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override fun count(): Int {
        return dao.count()
    }
}