package com.example.pawappproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitizenSubscriptionActivity : AppCompatActivity() {

    private lateinit var monthlyCard: CardView
    private lateinit var yearlyCard: CardView
    private lateinit var subscribeButton: Button
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    private var isMonthlySelected: Boolean? = null  // To track if a plan is selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_subscription)

        // Initialize Stripe SDK with your publishable key
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51PF6cwGtKecti7APZnyCxO4ZRvkqDkqmNVIW1BHPpFdmif3lhWImV4jcPxZZbFHGzaXeP2NNg64uU2U8vzK9GW7c00kZrjQxSY"
        )

        // Initialize the PaymentSheet instance
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Initialize CardViews and Subscribe button
        monthlyCard = findViewById(R.id.monthlyCard)
        yearlyCard = findViewById(R.id.yearlyCard)
        subscribeButton = findViewById(R.id.subscribeButton)

        // Set click listeners for cards
        monthlyCard.setOnClickListener {
            selectPlan(true)  // Monthly selected
        }

        yearlyCard.setOnClickListener {
            selectPlan(false)  // Yearly selected
        }

        // Trigger payment when subscribe button is clicked
        subscribeButton.setOnClickListener {
            if (isMonthlySelected != null) {
                // Check if a plan is selected
                if (isMonthlySelected == true) {
                    // Fetch Payment Intent for Monthly Plan
                    fetchPaymentIntent(30000, "php", "customer_id_example")
                } else {
                    // Fetch Payment Intent for Yearly Plan
                    fetchPaymentIntent(300000, "php", "customer_id_example")
                }
            } else {
                Toast.makeText(this, "Please select a plan first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPlan(isMonthly: Boolean) {
        // Update UI based on selection
        if (isMonthly) {
            // Set monthly card as selected
            monthlyCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            yearlyCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            monthlyCard.cardElevation = 8f
            yearlyCard.cardElevation = 4f
        } else {
            // Set yearly card as selected
            yearlyCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            monthlyCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            yearlyCard.cardElevation = 8f
            monthlyCard.cardElevation = 4f
        }

        // Store the selection
        isMonthlySelected = isMonthly
    }

    // Function to fetch Payment Intent and Customer Configuration from backend
    private fun fetchPaymentIntent(amount: Int, currency: String, customerId: String) {
        val request = PaymentIntentRequest(
            amount = amount,
            currency = currency,
            customerId = customerId
        )

        // Make the API call using Retrofit
        ApiClient.apiService.createPaymentIntent(request).enqueue(object : Callback<PaymentIntentResponse> {
            override fun onResponse(
                call: Call<PaymentIntentResponse>,
                response: Response<PaymentIntentResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        paymentIntentClientSecret = responseBody.paymentIntent
                        customerConfig = PaymentSheet.CustomerConfiguration(
                            responseBody.customerId,
                            responseBody.ephemeralKey
                        )
                        // Present the payment sheet after successfully fetching Payment Intent
                        presentPaymentSheet()
                    } else {
                        Toast.makeText(this@CitizenSubscriptionActivity, "Response is empty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CitizenSubscriptionActivity, "Failed to fetch payment intent", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentIntentResponse>, t: Throwable) {
                Toast.makeText(this@CitizenSubscriptionActivity, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to present the Stripe Payment Sheet
    private fun presentPaymentSheet() {
        val configuration = PaymentSheet.Configuration(
            "Your Business Name",
            customer = customerConfig
        )

        // Present the PaymentSheet with the Payment Intent client secret and configuration
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            configuration
        )
    }

    // Function to handle the result of the Payment Sheet
    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment complete!", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Payment failed: ${paymentSheetResult.error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
