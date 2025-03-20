package com.antonioleiva.frameworksamples.ui.screens.webservices.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton class that provides a configured Retrofit instance.
 */
object ApiClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * Creates an OkHttpClient with logging and timeouts configured.
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Creates a Retrofit instance with Gson converter and configured OkHttpClient.
     */
    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Creates and returns a JsonPlaceholderApi instance.
     */
    val jsonPlaceholderApi: JsonPlaceholderApi =
        retrofit.create(JsonPlaceholderApi::class.java)
} 