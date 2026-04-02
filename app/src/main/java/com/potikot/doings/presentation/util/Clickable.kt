package com.potikot.doings.presentation.util

interface Clickable {
    val onClick: () -> Unit
    val onLongClick: (() -> Unit)?
}