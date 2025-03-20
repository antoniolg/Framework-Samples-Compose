package com.antonioleiva.frameworksamples.ui.screens.webservices.model

/**
 * Data class representing a post from the JSONPlaceholder API.
 */
data class Post(
    val id: Int = 0,
    val userId: Int = 0,
    val title: String = "",
    val body: String = ""
) 