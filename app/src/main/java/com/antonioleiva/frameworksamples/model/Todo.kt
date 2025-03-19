package com.antonioleiva.frameworksamples.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int = 0,
    val text: String
) 