package com.example.taskmanager

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow


class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun upsert(task: Task) {
        taskDao.upsert(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }


}