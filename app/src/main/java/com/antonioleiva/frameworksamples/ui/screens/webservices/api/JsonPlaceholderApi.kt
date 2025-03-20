package com.antonioleiva.frameworksamples.ui.screens.webservices.api

import com.antonioleiva.frameworksamples.ui.screens.webservices.model.User
import retrofit2.http.GET

/**
 * Retrofit interface for the JSONPlaceholder API.
 * This interface defines all the endpoints we'll use in our examples.
 */
interface JsonPlaceholderApi {

    // User endpoints
    @GET("users")
    suspend fun getUsers(): List<User>
}