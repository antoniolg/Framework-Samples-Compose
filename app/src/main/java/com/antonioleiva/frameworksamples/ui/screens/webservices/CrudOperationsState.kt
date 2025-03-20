package com.antonioleiva.frameworksamples.ui.screens.webservices

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.Post
import com.antonioleiva.frameworksamples.ui.screens.webservices.repositories.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object CrudOperationsScreen

/**
 * Represents the complete UI state for the CRUD operations screen.
 * This allows multiple state elements to be shown simultaneously.
 */
data class CrudOperationsUIState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    
    // Form state
    val formTitle: String = "",
    val formBody: String = "",
    val formUserId: String = "1",
    val editingPostId: Int? = null
)

@Composable
fun rememberCrudOperationsState(
    postRepository: PostRepository = remember { PostRepository() },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(postRepository, scope) {
    CrudOperationsState(postRepository, scope)
}

class CrudOperationsState(
    private val postRepository: PostRepository,
    private val scope: CoroutineScope
) {
    var uiState by mutableStateOf(CrudOperationsUIState(isLoading = true))
        private set
    
    init {
        loadPosts()
    }
    
    // Form updating functions
    fun updatePostTitle(title: String) {
        uiState = uiState.copy(formTitle = title)
    }
    
    fun updatePostBody(body: String) {
        uiState = uiState.copy(formBody = body)
    }
    
    fun updateUserId(id: String) {
        uiState = uiState.copy(formUserId = id)
    }
    
    // Action functions
    fun startCreatingPost() {
        uiState = uiState.copy(
            editingPostId = null,
            formTitle = "",
            formBody = "",
            formUserId = "1"
        )
    }
    
    fun startEditingPost(post: Post) {
        uiState = uiState.copy(
            editingPostId = post.id,
            formTitle = post.title,
            formBody = post.body,
            formUserId = post.userId.toString()
        )
    }
    
    fun cancelEditing() {
        uiState = uiState.copy(
            editingPostId = null,
            formTitle = "",
            formBody = "",
            formUserId = "1"
        )
    }
    
    fun clearMessages() {
        uiState = uiState.copy(
            errorMessage = null,
            successMessage = null
        )
    }
    
    // CRUD operations
    fun loadPosts() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        
        scope.launch {
            postRepository.getPosts()
                .onSuccess { posts ->
                    uiState = if (posts.isEmpty()) {
                        uiState.copy(
                            isLoading = false,
                            posts = emptyList(),
                            errorMessage = "No posts available"
                        )
                    } else {
                        uiState.copy(
                            isLoading = false,
                            posts = posts,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Error loading posts: ${error.message}"
                    )
                }
        }
    }
    
    fun createOrUpdatePost() {
        // Validate inputs
        val userIdInt = uiState.formUserId.toIntOrNull()
        if (userIdInt == null) {
            uiState = uiState.copy(errorMessage = "Invalid user ID")
            return
        }
        
        if (uiState.formTitle.isBlank()) {
            uiState = uiState.copy(errorMessage = "Title cannot be empty")
            return
        }
        
        if (uiState.formBody.isBlank()) {
            uiState = uiState.copy(errorMessage = "Body cannot be empty")
            return
        }
        
        val post = Post(
            id = uiState.editingPostId ?: 0,
            userId = userIdInt,
            title = uiState.formTitle,
            body = uiState.formBody
        )
        
        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)
        
        scope.launch {
            val result = if (uiState.editingPostId != null) {
                // Update existing post
                postRepository.updatePost(uiState.editingPostId!!, post)
                    .map { "Post updated successfully" }
            } else {
                // Create new post
                postRepository.createPost(post)
                    .map { "Post created successfully" }
            }
            
            result.onSuccess { message ->
                // Get updated posts list
                val updatedPostsResult = postRepository.getPosts()
                
                updatedPostsResult.onSuccess { posts ->
                    uiState = uiState.copy(
                        isLoading = false,
                        posts = posts,
                        successMessage = message,
                        // Clear form
                        editingPostId = null,
                        formTitle = "",
                        formBody = "",
                        formUserId = "1"
                    )
                }.onFailure { error ->
                    // Even if getting updated posts fails, still show success message
                    uiState = uiState.copy(
                        isLoading = false,
                        successMessage = message,
                        // Clear form
                        editingPostId = null,
                        formTitle = "",
                        formBody = "",
                        formUserId = "1"
                    )
                    // And reload posts again
                    loadPosts()
                }
            }.onFailure { error ->
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Operation failed: ${error.message}"
                )
            }
        }
    }
    
    fun deletePost(postId: Int) {
        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)
        
        scope.launch {
            postRepository.deletePost(postId)
                .onSuccess {
                    // Get updated posts list
                    val updatedPostsResult = postRepository.getPosts()
                    
                    updatedPostsResult.onSuccess { posts ->
                        uiState = uiState.copy(
                            isLoading = false,
                            posts = posts,
                            successMessage = "Post deleted successfully"
                        )
                    }.onFailure { _ ->
                        uiState = uiState.copy(
                            isLoading = false,
                            successMessage = "Post deleted successfully"
                        )
                        // And reload posts again
                        loadPosts()
                    }
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Failed to delete post: ${error.message}"
                    )
                }
        }
    }
} 