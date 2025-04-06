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
import com.google.firebase.database.FirebaseDatabase


class OrgDonationDetailsFragment : Fragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPurpose: EditText
    private lateinit var editTextGcashName: EditText
    private lateinit var editTextGcashNumber: EditText
    private lateinit var editTextDetails: EditText
    private lateinit var buttonEdit: Button
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_details, container, false)

        editTextName = view.findViewById(R.id.editTextName)
        editTextPurpose = view.findViewById(R.id.editTextPurpose)
        editTextGcashName = view.findViewById(R.id.editTextGcashName)
        editTextGcashNumber = view.findViewById(R.id.editTextGcashNumber)
        editTextDetails = view.findViewById(R.id.editTextDetails)
        buttonEdit = view.findViewById(R.id.buttonEdit)

        val name = arguments?.getString("donation_name")
        val purpose = arguments?.getString("donation_purpose")
        val gCashName = arguments?.getString("donation_gcashName")
        val gCashNumber = arguments?.getString("donation_gcashNumber")
        val details = arguments?.getString("donation_details")

        editTextName.setText(name)
        editTextPurpose.setText(purpose)
        editTextGcashName.setText(gCashName)
        editTextGcashNumber.setText(gCashNumber)
        editTextDetails.setText(details)

        buttonEdit.setOnClickListener {
            toggleEditMode()
        }

        return view
    }

    private fun toggleEditMode() {
        isEditing = !isEditing
        setEditMode(isEditing)
        buttonEdit.text = if (isEditing) "Save" else "Edit"

        if (!isEditing) {
            saveUpdatedDetails()
        }
    }

    private fun setEditMode(enabled: Boolean) {
        editTextName.isEnabled = enabled
        editTextPurpose.isEnabled = enabled
        editTextGcashName.isEnabled = enabled
        editTextGcashNumber.isEnabled = enabled
        editTextDetails.isEnabled = enabled
    }

    private fun saveUpdatedDetails() {
        val updatedName = editTextName.text.toString()
        val updatedPurpose = editTextPurpose.text.toString()
        val updatedGcashName = editTextGcashName.text.toString()
        val updatedGcashNumber = editTextGcashNumber.text.toString()
        val updatedDetails = editTextDetails.text.toString()

        val databaseReference = FirebaseDatabase.getInstance().getReference("donation_requests")

        val updatedDonation = mapOf(
            "name" to updatedName,
            "purpose" to updatedPurpose,
            "gcashName" to updatedGcashName,
            "gcashNumber" to updatedGcashNumber,
            "details" to updatedDetails
        )

        databaseReference.child(arguments?.getString("donation_id") ?: "").updateChildren(updatedDonation)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Donation details updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to update donation details", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
