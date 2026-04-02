package com.potikot.doings.domain.model

import java.time.Instant

data class Account(
    val id: AccountId = AccountId(0),
    val externalId: String? = null,
    val name: String,
    val selectedProjectId: ProjectId? = null,
    val projects: List<Project> = emptyList(),
    val createdAt: Instant = Instant.now()
)

@JvmInline
value class AccountId(override val value: Long): ID