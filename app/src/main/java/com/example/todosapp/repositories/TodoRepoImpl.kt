package com.example.todosapp.repositories

import com.example.todosapp.database.TodoDatabase
import com.example.todosapp.database.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoRepoImpl(private val database: TodoDatabase) : TodoRepo {

    private val dao = database.todoDao()

    override suspend fun getTodos(): Flow<List<TodoEntity>> = dao.getTodos()

    override fun addTodo(todo: TodoEntity) = dao.addTodo(todo)

    override fun updateTodo(todo: TodoEntity) = dao.updateTodo(todo)

    override fun deleteTodo(todo: TodoEntity) = dao.deleteTodo(todo)
}