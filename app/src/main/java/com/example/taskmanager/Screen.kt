package com.example.taskmanager

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Lock: Screen("lock")
    object TaskListScreen: Screen("task_list_screen")
    object AddTask: Screen("add_task")
}