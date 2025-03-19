package com.antonioleiva.frameworksamples.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "todos")
@Serializable
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
) 