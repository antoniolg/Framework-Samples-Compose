package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    try {
        coroutineScope {
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
                println("Third child will not execute this line")
            }

            println("coroutineScope is waiting for children")
        }
    } catch (e: Exception) {
        println("Caught: ${e.message}")
    }

    println("coroutineScope is completed")
}