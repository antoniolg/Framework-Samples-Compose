package com.antonioleiva.frameworksamples.ui.screens.webservices.api

import com.antonioleiva.frameworksamples.ui.screens.webservices.model.Post
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit interface for the JSONPlaceholder API.
 * This interface defines all the endpoints we'll use in our examples.
 */
interface JsonPlaceholderApi {

    // User endpoints
    @GET("users")
    suspend fun getUsers(): List<User>

    // Post endpoints
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: Int,
        @Body post: Post
    ): Post

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int)
}