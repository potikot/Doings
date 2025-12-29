package com.potikot.doings.presentation.util

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Project : Screen("project_screen")
}