package com.example.taskmanager

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CounterViewModel(application: Application): AndroidViewModel(application) {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val context = application.applicationContext

    private val COUNTER_KEY = intPreferencesKey("Counter")
    init {
        viewModelScope.launch {
            context.dataStore.data.map { preferences->
                preferences[COUNTER_KEY] ?: 0
            }.collect{ savedCount->
                _count.value = savedCount
            }
        }
    }

    fun incrementCounter(){
        viewModelScope.launch {
            context.dataStore.edit { preferences->
                preferences[COUNTER_KEY] = _count.value + 1
            }
        }
    }

    fun resetCounter(){
        viewModelScope.launch {
            context.dataStore.edit { preferences->
                preferences[COUNTER_KEY] = 0
            }
        }
    }
}