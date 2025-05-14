package com.example.taskmanager

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.ui.theme.TaskManagerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lockViewModel: LockViewModel = viewModel()
            val counterViewModel: CounterViewModel = viewModel()
            TaskManagerTheme {
                // Call the main screen here
                TaskManagerApp(lockViewModel, counterViewModel)
            }
        }
    }
}

@Composable
fun TaskManagerApp(lockViewModel: LockViewModel, counterViewModel: CounterViewModel){

    val navController = rememberNavController()
    val isUnlocked by lockViewModel.isUnlocked.collectAsState()

    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            navController.navigate(Screen.Home.route) {
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
        startDestination = if (isUnlocked) Screen.Lock.route else Screen.Home.route
    ){
        composable(Screen.Lock.route){
            LockScreen(onUnlock = { lockViewModel.unlock() })
        }

        composable(Screen.Home.route){
            HomeScreen(
                onLock = { lockViewModel.lock() },
                onReset = { counterViewModel.resetCounter() }
            )
        }
        

    }
}

