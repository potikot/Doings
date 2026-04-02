package com.potikot.doings.domain.remote

import java.time.Instant

data class RemoteData<T>(
    val data: T,
    val updatedAt: Instant
)