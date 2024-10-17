package com.example.pawappproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.7:3000/"
    // Replace 3000 with your backend port


    // Create Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Expose the service for API requests
    val apiService: StripeService by lazy {
        retrofit.create(StripeService::class.java)
    }
}
