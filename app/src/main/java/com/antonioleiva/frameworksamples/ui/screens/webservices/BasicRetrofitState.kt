package com.antonioleiva.frameworksamples.ui.screens.webservices

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.antonioleiva.frameworksamples.ui.screens.webservices.repositories.UserRepository
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object BasicRetrofitScreen

sealed interface BasicRetrofitUiState {
    data object Loading : BasicRetrofitUiState
    data class Error(val message: String) : BasicRetrofitUiState
    data class Success(val users: List<User>) : BasicRetrofitUiState
}

@Composable
fun rememberBasicRetrofitState(
    userRepository: UserRepository = remember { UserRepository() },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(userRepository, scope) {
    BasicRetrofitState(userRepository, scope)
}

class BasicRetrofitState(
    private val userRepository: UserRepository,
    private val scope: CoroutineScope
) {
    var uiState by mutableStateOf<BasicRetrofitUiState>(BasicRetrofitUiState.Loading)
        private set
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        uiState = BasicRetrofitUiState.Loading
        
        scope.launch {
            userRepository.getUsers()
                .onSuccess { users ->
                    uiState = if (users.isEmpty()) {
                        BasicRetrofitUiState.Error("No data available")
                    } else {
                        BasicRetrofitUiState.Success(users)
                    }
                }
                .onFailure { error ->
                    uiState = BasicRetrofitUiState.Error("Error loading data: ${error.message}")
                }
        }
    }
} 