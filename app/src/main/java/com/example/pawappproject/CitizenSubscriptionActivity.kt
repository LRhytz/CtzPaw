package com.example.pawappproject

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class CitizenSubscriptionActivity : AppCompatActivity() {

    private lateinit var monthlyCard: CardView
    private lateinit var yearlyCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_subscription)

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Finish the activity to go back to the previous screen
            finish()
        }

        // Initialize CardViews
        monthlyCard = findViewById(R.id.monthlyCard)
        yearlyCard = findViewById(R.id.yearlyCard)

        // Set click listeners for cards
        monthlyCard.setOnClickListener {
            selectPlan(true)
        }

        yearlyCard.setOnClickListener {
            selectPlan(false)
        }
    }

    private fun selectPlan(isMonthly: Boolean) {
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
    }
}
