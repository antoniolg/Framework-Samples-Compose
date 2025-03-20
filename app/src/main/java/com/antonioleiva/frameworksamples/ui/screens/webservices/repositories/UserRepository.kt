package com.antonioleiva.frameworksamples.ui.screens.webservices.repositories

import com.antonioleiva.frameworksamples.ui.screens.webservices.api.ApiClient
import com.antonioleiva.frameworksamples.ui.screens.webservices.model.User

/**
 * Repository class that handles all user-related data operations.
 * Uses coroutines to perform network operations on the IO dispatcher.
 */
class UserRepository {
    private val api = ApiClient.jsonPlaceholderApi

    /**
     * Gets all users from the API.
     * @return Result with list of users or error.
     */
    suspend fun getUsers(): Result<List<User>> = try {
        Result.success(api.getUsers())
    } catch (e: Exception) {
        Result.failure(e)
    }
}