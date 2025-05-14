package com.example.taskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Counter(onLock: () -> Unit, onReset: () -> Unit ){
    val counterViewModel: CounterViewModel = viewModel()
    val count by counterViewModel.count.collectAsState()

    Text(
        text = "Count: $count",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(100.dp))

    Button(onClick = { counterViewModel.incrementCounter() }) {
        Text(text = "Increment Counter")
    }
    Spacer(modifier = Modifier.height(100.dp))

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onReset) {
            Text(text = "Reset")
        }
        Button(onClick = onLock) {
            Text(text = "Lock")
        }
    }
}