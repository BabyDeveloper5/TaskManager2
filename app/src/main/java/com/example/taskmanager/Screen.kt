package com.example.taskmanager

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Lock: Screen("lock")
    object TaskScreen: Screen("task_screen")
}