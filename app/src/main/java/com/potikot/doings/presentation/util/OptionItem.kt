package com.potikot.doings.presentation.util

import androidx.compose.ui.graphics.vector.ImageVector

data class OptionItem(
    val title: String,
    val icon: ImageVector? = null,
    val iconId: Int? = null,
    val rightIcon: ImageVector? = null,
    val action: () -> Unit
)