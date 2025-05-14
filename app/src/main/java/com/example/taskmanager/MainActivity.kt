package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.ui.theme.TaskManagerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lockViewModel: LockViewModel = viewModel()
            val taskViewModel: TaskViewModel = viewModel()

            /*val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "task") {
                composable("task") {
                    val viewModel: TaskViewModel = hiltViewModel()
                    TaskScreen(
                        viewModel = viewModel,
                        onAddTaskClick = {
                            // Later: Navigate to a task creation screen
                        }
                    )
                }

            }*/
            TaskManagerApp(lockViewModel)
        }
    }
}

@Composable
fun TaskManagerApp(lockViewModel: LockViewModel){

    val navController = rememberNavController()
    val isUnlocked by lockViewModel.isUnlocked.collectAsState()


    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            navController.navigate(Screen.TaskScreen.route) {
                popUpTo(Screen.Lock.route) { inclusive = true }
            }
        }else{
            navController.navigate(Screen.Lock.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = if (isUnlocked) Screen.Lock.route
        else Screen.TaskScreen.route
    ){
        composable(Screen.Lock.route){
            LockScreen(onUnlock = { lockViewModel.unlock() })
        }

        composable(Screen.TaskScreen.route){
            val viewModel: TaskViewModel = hiltViewModel()
            TaskScreen(
                viewModel = viewModel,
                onAddTaskClick = {
                    // Later: Navigate to a task creation screen
                }
            )
        }
    }
}


