package com.potikot.doings.domain.util

sealed interface AccountProvider {
    val id: String
    val displayName: String

    data object Local : AccountProvider {
        override val id = "local"
        override val displayName = "Локальный"
    }

    data object Yougile : AccountProvider {
        override val id = "yougile"
        override val displayName = "YouGile"
    }
}