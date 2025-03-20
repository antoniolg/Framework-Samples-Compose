package com.antonioleiva.frameworksamples.ui.screens.webservices.model

/**
 * Data class representing a user from the JSONPlaceholder API.
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String? = null,
    val website: String? = null,
) 