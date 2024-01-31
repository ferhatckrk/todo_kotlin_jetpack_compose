package com.example.todosapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todosapp.database.TodoEntity
import com.example.todosapp.database.addDate
import com.example.todosapp.ui.theme.ubuntuFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val todos by viewModel.todos.collectAsState()

    val (dialogOpen, setDialogOpen) = remember {
        mutableStateOf(false)
    }

    if (dialogOpen) {

        val (title, setTitle) = remember {
            mutableStateOf("")
        }
        val (subTitle, setSubTitle) = remember {
            mutableStateOf("")
        }


        Dialog(onDismissRequest = { setDialogOpen(false) }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        setTitle(it)
                    },
                    label = {
                        Text(text = "Todo Title")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        unfocusedTextColor = Color.White

                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = subTitle,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        setSubTitle(it)

                    },
                    label = {
                        Text(text = "Todo Title")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )

                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        if (title.isNotEmpty() && subTitle.isNotEmpty()) {
                            viewModel.addTodo(TodoEntity(title = title, subTitle = subTitle))
                        }
                        setDialogOpen(false)

                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,

                        )
                ) {
                    Text(text = "Submit", color = Color.White, fontFamily = ubuntuFont)

                }
            }
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.secondary, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                setDialogOpen(true)
            }, contentColor = Color.White, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }) { paddings ->
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = todos.isEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Text(text = "No Todo Yet.", color = Color.White, fontSize = 22.sp)

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = paddings.calculateBottomPadding() + 8.dp,
                        top = 8.dp,
                        end = 8.dp,
                        start = 8.dp
                    ), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(todos.sortedBy { it.done }, key = {
                    it.id
                }) { todo ->
                    TodoItem(todo = todo, onclick = {
                        viewModel.updateTodo(todo.copy(done = !todo.done))
                    }, onDelete = {
                        viewModel.deleteTodo(todo)
                    })
                }

            }
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.TodoItem(todo: TodoEntity, onclick: () -> Unit, onDelete: () -> Unit) {
    val color by animateColorAsState(
        targetValue = if (todo.done) Color(0xff24d65f) else Color(0xFF868886),
        animationSpec = tween(500),
        label = ""
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color)
            .clickable { onclick() }
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Row(

                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),

                    ) {

                    AnimatedVisibility(
                        visible = todo.done,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = color)
                    }


                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                    Text(
                        todo.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        todo.subTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xffebebeb)
                    )
                }


            }
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp),
                contentAlignment = Alignment.CenterEnd,

                ) {
                Icon(Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier.clickable { onDelete() })
            }

        }
        Text(
            text = todo.addDate,
            modifier = Modifier.padding(4.dp),
            color = Color(0xffebebeb),
            fontSize = 12.sp
        )
    }


}







