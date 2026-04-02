package com.potikot.doings.presentation.util

import androidx.compose.ui.graphics.vector.ImageVector

data class ListElement(
    val label: String,
    override val onClick: () -> Unit,
    override val onLongClick: (() -> Unit)? = null,
    val leadingIcon: ImageVector? = null,
) : Clickable