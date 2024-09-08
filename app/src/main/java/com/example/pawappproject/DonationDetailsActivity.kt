package com.example.pawappproject

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class DonationDetailsActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPurpose: EditText
    private lateinit var editTextGcashName: EditText
    private lateinit var editTextGcashNumber: EditText
    private lateinit var editTextDetails: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_details)

        editTextName = findViewById(R.id.editTextName)
        editTextPurpose = findViewById(R.id.editTextPurpose)
        editTextGcashName = findViewById(R.id.editTextGcashName)
        editTextGcashNumber = findViewById(R.id.editTextGcashNumber)
        editTextDetails = findViewById(R.id.editTextDetails)

        // Get donation data from the intent
        val name = intent.getStringExtra("donation_name")
        val purpose = intent.getStringExtra("donation_purpose")
        val gcashName = intent.getStringExtra("donation_gcashName")
        val gcashNumber = intent.getStringExtra("donation_gcashNumber")
        val details = intent.getStringExtra("donation_details")

        // Populate the EditTexts with the donation data
        editTextName.setText(name)
        editTextPurpose.setText(purpose)
        editTextGcashName.setText(gcashName)
        editTextGcashNumber.setText(gcashNumber)
        editTextDetails.setText(details)

        // Disable editing for all fields
        setEditMode(false)
    }

    // Method to disable editing for the EditText fields
    private fun setEditMode(enabled: Boolean) {
        editTextName.isEnabled = enabled
        editTextPurpose.isEnabled = enabled
        editTextGcashName.isEnabled = enabled
        editTextGcashNumber.isEnabled = enabled
        editTextDetails.isEnabled = enabled
    }
}
