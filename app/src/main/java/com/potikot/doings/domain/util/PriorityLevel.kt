package com.potikot.doings.domain.util

import androidx.compose.ui.graphics.Color

enum class PriorityLevel {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

fun getPriorityColor(level: PriorityLevel): Color {
    return when(level) {
        PriorityLevel.NONE -> Color.White
        PriorityLevel.LOW -> Color.White
        PriorityLevel.MEDIUM -> Color.Yellow
        PriorityLevel.HIGH -> Color(255, 128, 0)
        PriorityLevel.CRITICAL -> Color.Red
    }
}