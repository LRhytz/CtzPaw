package com.example.pawappproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pawappproject.OrganizationLoginActivity
import com.example.pawappproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrgOrganizationProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var contactNumberTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var editProfileButton: View
    private lateinit var logoutButton: View

    // Use a variable for the listener so you can remove it later.
    private var profileDataListener: ValueEventListener? = null

    // Replace this with the actual organization ID from your authenticated user.
    private val organizationId = "ORG_ID"

    private lateinit var orgRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_org_organization_profile, container, false)

        profileImageView = rootView.findViewById(R.id.profileImageView)
        nameTextView = rootView.findViewById(R.id.nameTextView)
        contactNumberTextView = rootView.findViewById(R.id.contactNumberTextView)
        emailTextView = rootView.findViewById(R.id.emailTextView)
        editProfileButton = rootView.findViewById(R.id.editProfileButton)
        logoutButton = rootView.findViewById(R.id.OrgLogoutBtn)

        editProfileButton.setOnClickListener {
            //navigateToEditProfile()
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }

        // Initialize the reference.
        orgRef = FirebaseDatabase.getInstance().getReference("organizations").child(organizationId)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        loadProfileData()
    }

    override fun onStop() {
        super.onStop()
        // Remove the listener to avoid callbacks when the fragment is detached.
        profileDataListener?.let {
            orgRef.removeEventListener(it)
        }
    }

    private fun loadProfileData() {
        profileDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java) ?: "Not provided"
                    val contactNumber = snapshot.child("contactNumber").getValue(String::class.java)
                    val profileImageUri = snapshot.child("profileImage").getValue(String::class.java)

                    nameTextView.text = name
                    emailTextView.text = email
                    contactNumberTextView.text = contactNumber

                    if (!profileImageUri.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(profileImageUri)
                            .into(profileImageView)
                    } else {
                        profileImageView.setImageResource(R.drawable.ic_profile)
                    }
                } else {
                    if (isAdded && context != null) {
                        Toast.makeText(context, "No profile data found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Check if fragment is still added before showing a Toast.
                if (isAdded && context != null) {
                    Toast.makeText(context, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        orgRef.addValueEventListener(profileDataListener as ValueEventListener)
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        // Optionally remove the listener immediately here if needed.
        profileDataListener?.let {
            orgRef.removeEventListener(it)
        }
        if (isAdded && context != null) {
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(requireContext(), OrganizationLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
