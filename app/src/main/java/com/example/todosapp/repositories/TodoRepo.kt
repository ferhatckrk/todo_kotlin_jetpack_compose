package com.example.todosapp.repositories

import com.example.todosapp.database.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepo {

    suspend fun getTodos(): Flow<List<TodoEntity>>
    fun addTodo(todo: TodoEntity)
    fun updateTodo(todo: TodoEntity)
    fun deleteTodo(todo: TodoEntity)

}