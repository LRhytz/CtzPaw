package com.example.pawappproject

// Define the request body for creating a payment intent
data class PaymentIntentRequest(
    val amount: Int,
    val currency: String,
    val customerId: String
)

// Define the response structure from the backend
data class PaymentIntentResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customerId: String
)
