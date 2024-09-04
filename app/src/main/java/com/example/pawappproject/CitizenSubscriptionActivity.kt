package com.example.pawappproject

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CitizenSubscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_subscription)

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Finish the activity to go back to the previous screen
            finish()
        }

        // Here you can set up your logic for handling subscriptions, like setting click listeners
        // on monthly and yearly cards, or handling the subscribe button click
    }
}
