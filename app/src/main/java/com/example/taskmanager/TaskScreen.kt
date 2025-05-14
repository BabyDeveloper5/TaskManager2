package com.example.taskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel = hiltViewModel(), onAddTaskClick :() -> Unit){

    val tasks by viewModel.allTasks.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Task List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick =  onAddTaskClick ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) {paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onToggleDone = { isChecked ->
                        viewModel.upsertTask(task.copy(isDone = isChecked))
                    },
                    onDelete = {
                        viewModel.deleteTask(task)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDelete: () -> Unit,
    onToggleDone: (Boolean) -> Unit
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggleDone }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    style = if(task.isDone) TextStyle(
                        textDecoration = TextDecoration.LineThrough)
                    else LocalTextStyle.current
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}