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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Todo
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object TodoScreen

@Composable
fun TodoScreen(onBack: () -> Unit) {
    var todos by remember { mutableStateOf<List<Todo>>(emptyList()) }
    var nextId by remember { mutableIntStateOf(1) }
    var newTodoText by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<Todo?>(null) }
    var editText by remember { mutableStateOf("") }

    Screen(
        title = stringResource(R.string.room_todo),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Input para añadir nuevas tareas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { newTodoText = it },
                    label = { Text(stringResource(R.string.todo_hint)) },
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        if (newTodoText.isNotEmpty()) {
                            val newTodo = Todo(id = nextId, text = newTodoText)
                            todos = todos + newTodo
                            nextId++
                            newTodoText = ""
                        }
                    },
                    enabled = newTodoText.isNotEmpty(),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.add_todo))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lista de tareas
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (todos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.todo_empty),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(todos, key = { it.id }) { todo ->
                            TodoItem(
                                todo = todo,
                                onEditClick = {
                                    todoToEdit = todo
                                    editText = todo.text
                                    showEditDialog = true
                                },
                                onDeleteClick = {
                                    todos = todos.filter { it.id != todo.id }
                                },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
            
            // Texto de instrucciones
            Text(
                text = stringResource(R.string.room_todo_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        // Diálogo para editar tareas
        if (showEditDialog && todoToEdit != null) {
            Dialog(
                onDismissRequest = { showEditDialog = false }
            ) {
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
                            value = editText,
                            onValueChange = { editText = it },
                            label = { Text(stringResource(R.string.todo_hint)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { showEditDialog = false }
                            ) {
                                Text(stringResource(R.string.cancel))
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Button(
                                onClick = {
                                    if (editText.isNotEmpty() && todoToEdit != null) {
                                        todos = todos.map { 
                                            if (it.id == todoToEdit!!.id) it.copy(text = editText) else it 
                                        }
                                        showEditDialog = false
                                    }
                                },
                                enabled = editText.isNotEmpty()
                            ) {
                                Text(stringResource(R.string.update))
                            }
                        }
                    }
                }
            }
        }
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