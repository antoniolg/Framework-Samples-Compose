package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main(): Unit = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    supervisorScope {
        launch(handler) {
            throw RuntimeException("Error in task 1")
        }
        launch {
            delay(100)
            println("Task 2 completed")
        }
    }
}