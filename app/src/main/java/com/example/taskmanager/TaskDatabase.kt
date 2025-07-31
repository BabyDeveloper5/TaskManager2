package com.example.taskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        @Volatile private var INSTANCE: TaskDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): TaskDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}