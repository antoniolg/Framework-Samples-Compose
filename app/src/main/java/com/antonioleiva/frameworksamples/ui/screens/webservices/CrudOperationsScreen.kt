package com.antonioleiva.frameworksamples.ui.screens.webservices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.Post

@Composable
fun CrudOperationsScreen(onBack: () -> Unit) {
    val state = rememberCrudOperationsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // For tracking if we're creating a new post
    var isCreatingPost by remember { mutableStateOf(false) }
    
    // For delete confirmation dialog
    var postToDelete by remember { mutableStateOf<Post?>(null) }
    
    // Manage snackbar messages with a key to trigger effects
    var messageKey by remember { mutableIntStateOf(0) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    
    // Update snackbar message when state changes
    LaunchedEffect(state.uiState.errorMessage, state.uiState.successMessage) {
        state.uiState.errorMessage?.let { 
            snackbarMessage = it
            messageKey++
            state.clearMessages()
        }
        
        state.uiState.successMessage?.let {
            snackbarMessage = it
            messageKey++
            state.clearMessages()
        }
    }
    
    // Show the snackbar when message changes
    LaunchedEffect(messageKey) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    
    Screen(
        title = stringResource(R.string.web_services_crud),
        onBack = onBack
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = { 
                    state.startCreatingPost() 
                    isCreatingPost = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.create)
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Explanation text
                Text(
                    text = stringResource(R.string.retrofit_crud_explanation),
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Main content based on state
                if (state.uiState.isLoading && state.uiState.posts.isEmpty()) {
                    // Only show loading indicator if we don't have posts yet
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.uiState.posts.isEmpty()) {
                    // Only show error if we have no posts to display
                    state.uiState.errorMessage?.let { errorMsg ->
                        ErrorContent(
                            message = errorMsg,
                            onRetry = { state.loadPosts() }
                        )
                    }
                } else {
                    // Show posts list
                    PostsList(
                        posts = state.uiState.posts,
                        isLoading = state.uiState.isLoading,
                        onEditClick = { state.startEditingPost(it) },
                        onDeleteClick = { post -> postToDelete = post }
                    )
                }
            }
        }
    }
    
    // Show create/edit post dialog
    if (state.uiState.editingPostId != null || isCreatingPost) {
        PostFormDialog(
            isEditing = state.uiState.editingPostId != null,
            title = state.uiState.formTitle,
            body = state.uiState.formBody,
            userId = state.uiState.formUserId,
            isLoading = state.uiState.isLoading,
            onTitleChange = { state.updatePostTitle(it) },
            onBodyChange = { state.updatePostBody(it) },
            onUserIdChange = { state.updateUserId(it) },
            onSaveClick = { 
                state.createOrUpdatePost()
                isCreatingPost = false
            },
            onCancelClick = { 
                state.cancelEditing()
                isCreatingPost = false
            }
        )
    }
    
    // Show delete confirmation dialog
    postToDelete?.let { post ->
        DeleteConfirmationDialog(
            post = post,
            onConfirm = { 
                state.deletePost(post.id)
                postToDelete = null
            },
            onDismiss = { postToDelete = null }
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
private fun PostsList(
    posts: List<Post>,
    isLoading: Boolean,
    onEditClick: (Post) -> Unit,
    onDeleteClick: (Post) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Title with loading indicator if needed
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "${stringResource(R.string.users_list_title)} (${posts.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
        
        // Posts list
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onEditClick = { onEditClick(post) },
                    onDeleteClick = { onDeleteClick(post) }
                )
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title row with edit/delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit)
                    )
                }
                
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Post content
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // User ID
            Text(
                text = stringResource(R.string.user_id_format, post.userId),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PostFormDialog(
    isEditing: Boolean,
    title: String,
    body: String,
    userId: String,
    isLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onUserIdChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog title
                Text(
                    text = if (isEditing) stringResource(R.string.edit) else stringResource(R.string.create),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // User ID field
                OutlinedTextField(
                    value = userId,
                    onValueChange = onUserIdChange,
                    label = { Text(stringResource(R.string.user_id_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text(stringResource(R.string.title_hint)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Body field
                OutlinedTextField(
                    value = body,
                    onValueChange = onBodyChange,
                    label = { Text(stringResource(R.string.body_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    
                    Button(
                        onClick = onSaveClick,
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isEditing) 
                                    stringResource(R.string.update) 
                                else 
                                    stringResource(R.string.create)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    post: Post,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.delete)) },
        text = { Text(text = stringResource(R.string.confirm_delete_message, post.title)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
} 