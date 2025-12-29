package com.potikot.doings.data.data_source.mappers

import com.potikot.doings.data.data_source.AccountWithProjects
import com.potikot.doings.data.data_source.entity.AccountEntity
import com.potikot.doings.domain.model.Account
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.ProjectId
import java.time.Instant

fun AccountWithProjects.toDomain(): Account {
    return Account(
        id = AccountId(this.account.id),
        externalId = this.account.externalId,
        name = this.account.name,
        selectedProjectId = this.account.selectedProjectId?.let { ProjectId(it) },
        projects = this.projects.map { it.toDomain() },
        createdAt = Instant.ofEpochMilli(this.account.createdAt)
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id.value,
        externalId = this.externalId,
        name = this.name,
        selectedProjectId = this.selectedProjectId?.value,
        createdAt = this.createdAt.toEpochMilli()
    )
}

fun Account.toEntityRelation(): AccountWithProjects {
    return AccountWithProjects(
        account = this.toEntity(),
        projects = this.projects.map { it.toEntityRelation(this.id) }
    )
}