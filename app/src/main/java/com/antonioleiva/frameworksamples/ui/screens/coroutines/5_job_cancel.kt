package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("Coroutine is working: $i")
            delay(500)
        }
    }

    delay(1500) // Let the coroutine run for a while
    println("Main: I'm tired of waiting!")
    job.cancel() // Cancel the coroutine
    job.join() // Wait for the coroutine to finish cancellation
    println("Main: Now I can continue!")
}