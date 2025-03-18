package com.antonioleiva.frameworksamples.ui.screens.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Starting to fetch user data")
    val userData = fetchUserData()
    println("User data received: $userData")
}

suspend fun fetchUserData(): String {
    delay(2000L) // Simulating network call
    return "User Data"
}