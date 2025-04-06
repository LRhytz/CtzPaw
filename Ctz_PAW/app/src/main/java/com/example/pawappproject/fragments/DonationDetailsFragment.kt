package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pawappproject.R

class DonationDetailsFragment : Fragment() {

    private lateinit var textViewName: TextView
    private lateinit var textViewPurpose: TextView
    private lateinit var textViewGcashName: TextView
    private lateinit var textViewGcashNumber: TextView
    private lateinit var textViewDetails: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_details, container, false)

        // Initialize views
        textViewName = view.findViewById(R.id.textViewName)
        textViewPurpose = view.findViewById(R.id.textViewPurpose)
        textViewGcashName = view.findViewById(R.id.textViewGcashName)
        textViewGcashNumber = view.findViewById(R.id.textViewGcashNumber)
        textViewDetails = view.findViewById(R.id.textViewDetails)

        // Get donation data from arguments
        val name = arguments?.getString("donation_name") ?: "Not specified"
        val purpose = arguments?.getString("donation_purpose") ?: "Not specified"
        val gCashName = arguments?.getString("donation_gcashName") ?: "Not specified"
        val gCashNumber = arguments?.getString("donation_gcashNumber") ?: "Not specified"
        val details = arguments?.getString("donation_details") ?: "Not specified"

        // Populate TextViews dynamically
        textViewName.text = name
        textViewPurpose.text = "Purpose: $purpose"
        textViewGcashName.text = "GCash Name: $gCashName"
        textViewGcashNumber.text = "GCash Number: $gCashNumber"
        textViewDetails.text = "Additional Details: $details"

        return view
    }

    companion object {
        fun newInstance(
            name: String,
            purpose: String,
            gcashName: String,
            gcashNumber: String,
            details: String
        ): DonationDetailsFragment {
            val fragment = DonationDetailsFragment()
            val args = Bundle().apply {
                putString("donation_name", name)
                putString("donation_purpose", purpose)
                putString("donation_gcashName", gcashName)
                putString("donation_gcashNumber", gcashNumber)
                putString("donation_details", details)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
