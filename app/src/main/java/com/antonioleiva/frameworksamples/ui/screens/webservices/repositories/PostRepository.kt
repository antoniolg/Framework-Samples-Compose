package com.antonioleiva.frameworksamples.ui.screens.webservices.repositories

import com.antonioleiva.frameworksamples.ui.screens.webservices.api.ApiClient
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.Post

/**
 * Repository class that handles all post-related data operations.
 * Uses coroutines to perform network operations on the IO dispatcher.
 */
class PostRepository {
    private val api = ApiClient.jsonPlaceholderApi

    /**
     * Gets all posts from the API.
     * @return Result with list of posts or error.
     */
    suspend fun getPosts(): Result<List<Post>> = try {
        Result.success(api.getPosts())
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Creates a new post.
     * @param post The post to create.
     * @return Result with the created post or error.
     */
    suspend fun createPost(post: Post): Result<Post> = try {
        Result.success(api.createPost(post))
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Updates an existing post.
     * @param postId The ID of the post to update.
     * @param post The updated post data.
     * @return Result with the updated post or error.
     */
    suspend fun updatePost(postId: Int, post: Post): Result<Post> = try {
        Result.success(api.updatePost(postId, post))
    } catch (e: Exception) {
        Result.failure(e)
    }

    /**
     * Deletes a post.
     * @param postId The ID of the post to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deletePost(postId: Int): Result<Unit> = try {
        api.deletePost(postId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
} 