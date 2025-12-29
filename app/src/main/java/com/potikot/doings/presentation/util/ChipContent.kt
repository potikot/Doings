package com.potikot.doings.presentation.util

import androidx.compose.runtime.Composable

data class ChipContent(
    val label: @Composable () -> Unit,
    val leadingIcon: @Composable (() -> Unit)? = null,
    val trailingIcon: @Composable (() -> Unit)? = null
)
