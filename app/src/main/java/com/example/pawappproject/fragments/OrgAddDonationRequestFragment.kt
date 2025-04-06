package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawappproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.pawappproject.Donation
import java.text.SimpleDateFormat
import java.util.*

class OrgAddDonationRequestFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_org_add_donation_request, container, false)

        database = FirebaseDatabase.getInstance().getReference("donation_requests")

        val submitButton = view.findViewById<Button>(R.id.buttonSubmitRequest)
        submitButton.setOnClickListener {
            submitDonationRequest(view)
        }

        return view
    }

    private fun submitDonationRequest(view: View) {
        val orgName = view.findViewById<EditText>(R.id.editTextOrganizationName).text.toString()
        val purpose = view.findViewById<EditText>(R.id.editTextPurpose).text.toString()
        val gcashName = view.findViewById<EditText>(R.id.editTextGcashName).text.toString()
        val gcashNumber = view.findViewById<EditText>(R.id.editTextGcashNumber).text.toString()
        val details = view.findViewById<EditText>(R.id.editTextDetails).text.toString()

        if (orgName.isEmpty() || purpose.isEmpty() || gcashName.isEmpty() || gcashNumber.isEmpty() || details.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val donationTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val donationRequestId = "donation_$donationTimestamp"

        val donationRequest = Donation(
            id = donationRequestId,
            name = orgName,
            purpose = purpose,
            gcashName = gcashName,
            gcashNumber = gcashNumber,
            details = details
        )

        database.child(donationRequestId).setValue(donationRequest).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Donation Request Submitted", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to Submit Request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
