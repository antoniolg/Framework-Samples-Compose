package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() = runBlocking {
    supervisorScope {
        launch {
            delay(500)
            println("First child is completing")
        }

        launch {
            delay(1000)
            println("Second child throws an exception")
            throw RuntimeException("Oops")
        }

        launch {
            delay(2000)
            println("Third child completes successfully")
        }

        println("supervisorScope is waiting for children")
    }

    println("supervisorScope is completed")
}