package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        repeat(5) { i ->
            println("Coroutine is working: $i")
            delay(500)
        }
    }

    println("Main: Waiting for the coroutine to finish...")
    job.join()
    println("Main: Now I can continue!")
}