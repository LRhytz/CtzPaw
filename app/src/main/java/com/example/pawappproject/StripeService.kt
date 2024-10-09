package com.example.pawappproject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StripeService {
    @POST("create-payment-intent")
    fun createPaymentIntent(@Body request: PaymentIntentRequest): Call<PaymentIntentResponse>
}
