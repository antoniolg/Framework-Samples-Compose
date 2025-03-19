package com.antonioleiva.frameworksamples.ui.screens.persistence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antonioleiva.frameworksamples.App
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Todo
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object TodoScreen

@Composable
fun TodoScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as App
    val todoDao = app.database.todoDao()
    val scope = rememberCoroutineScope()
    
    var newTodoText by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<Todo?>(null) }
    var editText by remember { mutableStateOf("") }
    
    val todos by todoDao.getAllTodos().collectAsState(emptyList())

    Screen(
        title = stringResource(R.string.room_todo),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TodoInput(
                newTodoText = newTodoText,
                onNewTodoTextChange = { newTodoText = it },
                onAddTodo = {
                    scope.launch {
                        val todo = Todo(text = newTodoText)
                        todoDao.insertTodo(todo)
                        newTodoText = ""
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TodoList(
                todos = todos,
                onEditTodo = { todo ->
                    todoToEdit = todo
                    editText = todo.text
                    showEditDialog = true
                },
                onDeleteTodo = { todo ->
                    scope.launch {
                        todoDao.deleteTodo(todo)
                    }
                },
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = stringResource(R.string.room_todo_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        if (showEditDialog && todoToEdit != null) {
            EditTodoDialog(
                initialText = editText,
                onTextChange = { editText = it },
                onDismiss = { showEditDialog = false },
                onConfirm = {
                    scope.launch {
                        val updatedTodo = todoToEdit!!.copy(text = editText)
                        todoDao.updateTodo(updatedTodo)
                        showEditDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun TodoInput(
    newTodoText: String,
    onNewTodoTextChange: (String) -> Unit,
    onAddTodo: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = newTodoText,
            onValueChange = onNewTodoTextChange,
            label = { Text(stringResource(R.string.todo_hint)) },
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Button(
            onClick = onAddTodo,
            enabled = newTodoText.isNotEmpty(),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.add_todo))
        }
    }
}

@Composable
fun TodoList(
    todos: List<Todo>,
    onEditTodo: (Todo) -> Unit,
    onDeleteTodo: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (todos.isEmpty()) {
            EmptyTodoList()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoItem(
                        todo = todo,
                        onEditClick = { onEditTodo(todo) },
                        onDeleteClick = { onDeleteTodo(todo) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTodoList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.todo_empty),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = todo.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_todo)
            )
        }
        
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_todo)
            )
        }
    }
}

@Composable
fun EditTodoDialog(
    initialText: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.update),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = initialText,
                    onValueChange = onTextChange,
                    label = { Text(stringResource(R.string.todo_hint)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = onConfirm,
                        enabled = initialText.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.update))
                    }
                }
            }
        }
    }
} 