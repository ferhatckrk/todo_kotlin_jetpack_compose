package com.example.todosapp

import android.app.Application
import androidx.room.Room
import com.example.todosapp.database.TodoDatabase
import com.example.todosapp.repositories.TodoRepo
import com.example.todosapp.repositories.TodoRepoImpl
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class TodoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {

            modules(module {
                single {
                    Room
                        .databaseBuilder(this@TodoApp, TodoDatabase::class.java, "db")
                        .build()
                }
                single {
                    TodoRepoImpl(database = get())
                } bind TodoRepo::class
            })

        }


    }
}