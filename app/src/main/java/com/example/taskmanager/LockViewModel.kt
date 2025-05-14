package com.example.taskmanager

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LockViewModel(application: Application): AndroidViewModel(application) {

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    val context = application.applicationContext
    private val UNLOCK_KEY = booleanPreferencesKey("is_unlocked")
    init {
        viewModelScope.launch {
            context.dataStore.data.map { preference->
                preference[UNLOCK_KEY] ?: false
            }.collect{ unlocked->
                _isUnlocked.value = unlocked
            }
        }
    }

    fun lock(){
        viewModelScope.launch{
            context.dataStore.edit { preferences->
                preferences[UNLOCK_KEY] = false
            }
        }
    }

    fun unlock(){
        viewModelScope.launch{
            context.dataStore.edit { preferences->
                preferences[UNLOCK_KEY] = true
            }
        }
    }
}