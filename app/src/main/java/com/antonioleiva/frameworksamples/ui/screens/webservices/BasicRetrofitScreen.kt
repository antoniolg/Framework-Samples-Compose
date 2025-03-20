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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.User
import com.antonioleiva.frameworksamples.ui.components.Screen

@Composable
fun BasicRetrofitScreen(onBack: () -> Unit) {
    val state = rememberBasicRetrofitState()
    
    Screen(
        title = stringResource(R.string.web_services_basic_retrofit),
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Explanation text
            Text(
                text = stringResource(R.string.retrofit_basic_explanation),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main content based on state
            when (val uiState = state.uiState) {
                is BasicRetrofitUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is BasicRetrofitUiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetry = { state.loadUsers() }
                    )
                }
                
                is BasicRetrofitUiState.Success -> {
                    UsersListContent(users = uiState.users)
                }
            }
        }
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
private fun UsersListContent(users: List<User>) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Title with count
        Text(
            text = "${stringResource(R.string.users_list_title)} (${users.size})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Users list
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserItem(user = user)
            }
        }
    }
}

@Composable
private fun UserItem(user: User) {
    var showDetails by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { showDetails = true }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    
    if (showDetails) {
        UserDetailsDialog(
            user = user,
            onDismiss = { showDetails = false }
        )
    }
}

@Composable
private fun UserDetailsDialog(
    user: User,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.user_details),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                DetailRow(
                    label = stringResource(R.string.user_id_label, user.id)
                )
                
                DetailRow(
                    label = stringResource(R.string.user_username_label, user.username)
                )
                
                DetailRow(
                    label = stringResource(R.string.user_email_label, user.email)
                )
                
                user.phone?.let {
                    DetailRow(
                        label = stringResource(R.string.user_phone_label, it)
                    )
                }
                
                user.website?.let {
                    DetailRow(
                        label = stringResource(R.string.user_website_label, it)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 