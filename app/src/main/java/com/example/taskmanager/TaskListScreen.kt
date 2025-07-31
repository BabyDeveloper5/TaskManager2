package com.example.taskmanager

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    onAddTaskClick: () -> Unit
) {
    val tasks by taskViewModel.allTasks.collectAsState()

    // State to manage the confirmation dialog visibility
    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }




    // Scaffold to display the app's layout
    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(text = "Task List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().background(Color(0xffe9f5db)),
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onToggleDone = { isChecked ->
                        taskViewModel.upsertTask(task.copy(isDone = isChecked))
                    },
                    onDelete = {
                        // When delete is clicked, show the confirmation dialog
                        taskToDelete = task
                        showDeleteDialog = true
                    }
                )
            }
        }

        // Show the confirmation dialog if needed
        if (showDeleteDialog && taskToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    // Close dialog when clicking outside
                    showDeleteDialog = false
                    taskToDelete = null
                },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete the task '${taskToDelete?.title}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            taskViewModel.deleteTask(taskToDelete!!) // Delete the task
                            showDeleteDialog = false
                            taskToDelete = null // Reset task to delete
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            taskToDelete = null // Reset task to delete
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDelete: () -> Unit,
    onToggleDone: (Boolean) -> Unit
) {
    // State to manage the description dialog visibility
    var showDescriptionDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDescriptionDialog = !showDescriptionDialog },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = if (task.isDone) CardDefaults.cardColors(Color(0xFFcaffbf))
        else CardDefaults.cardColors(Color(0xFFfae6e7))
    ) {
        Column(modifier = Modifier.fillMaxWidth().animateContentSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = onToggleDone
                )
                Text(
                    text = task.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = if (task.isDone) TextStyle(
                        textDecoration = TextDecoration.LineThrough
                    ) else LocalTextStyle.current
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            if (showDescriptionDialog){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    fontSize = 16.sp
                )
            }
        }
    }
}