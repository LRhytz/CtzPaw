package com.example.pawappproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DonationRequestFormActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_request_form)

        database = FirebaseDatabase.getInstance().getReference("donation_requests")

        val submitButton = findViewById<Button>(R.id.buttonSubmitRequest)
        submitButton.setOnClickListener {
            submitDonationRequest()
        }
    }

    private fun submitDonationRequest() {
        val orgName = findViewById<EditText>(R.id.editTextOrganizationName).text.toString()
        val purpose = findViewById<EditText>(R.id.editTextPurpose).text.toString()
        val gcashName = findViewById<EditText>(R.id.editTextGcashName).text.toString()
        val gcashNumber = findViewById<EditText>(R.id.editTextGcashNumber).text.toString()
        val details = findViewById<EditText>(R.id.editTextDetails).text.toString()

        // Generate unique donation ID based on timestamp and user ID
        val donationTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val donationRequestId = "donation_$donationTimestamp"  // Unique donation ID

        // Create the donation request object
        val donationRequest = Donation(
            id = donationRequestId,
            name = orgName,
            purpose = purpose,
            gcashName = gcashName,
            gcashNumber = gcashNumber,
            details = details
        )

        // Store donation request in the database
        database.child(donationRequestId).setValue(donationRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Donation Request Submitted", Toast.LENGTH_SHORT).show()
                finish() // Close the activity and return to the previous screen
            } else {
                Toast.makeText(this, "Failed to Submit Request", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class Donation(
        val id: String = "",  // Include an ID field to uniquely identify each donation
        val name: String = "",
        val purpose: String = "",
        val gcashName: String = "",
        val gcashNumber: String = "",
        val details: String = ""  // Ensure the details field is included
    )
}
