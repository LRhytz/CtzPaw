package com.example.pawappproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pawappproject.CitizenSubscriptionActivity
import com.example.pawappproject.DialogActivity
import com.example.pawappproject.EditProfileActivity
import com.example.pawappproject.LoginActivity
import com.example.pawappproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView


class CitizenProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var profileImageView: CircleImageView
    private lateinit var txtUsername: TextView
    private lateinit var txtPhoneNumber: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtBio: TextView

    // ✅ New way to handle result after editing profile
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            fetchUserData() // Refresh user profile after editing
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        profileImageView = view.findViewById(R.id.profileImageView) // ✅ Updated for circular image
        txtUsername = view.findViewById(R.id.txtUsername)
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtBio = view.findViewById(R.id.txtBio)

        fetchUserData()

        val logoutButton: Button = view.findViewById(R.id.LogoutBtn)
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Logging out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        //val aboutUsBtn: LinearLayout = view.findViewById(R.id.AboutUs)
        //aboutUsBtn.setOnClickListener {
            //startActivity(Intent(requireActivity(), DialogActivity::class.java))
        //}

        val citizenPremiumSubBtn: LinearLayout = view.findViewById(R.id.PremiumSubBtn)
        citizenPremiumSubBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), CitizenSubscriptionActivity::class.java))
        }

        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            editProfileLauncher.launch(intent) // ✅ Corrected: Use new method to handle result
        }

        return view
    }

    /**
     * Fetches user data from Firebase and updates the UI
     */
    private fun fetchUserData() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded || view == null) return

                    if (snapshot.exists()) {
                        val username = snapshot.child("username").getValue(String::class.java) ?: "No Username"
                        val phone = snapshot.child("phone").getValue(String::class.java) ?: "No Phone Number"
                        val email = snapshot.child("email").getValue(String::class.java) ?: "No Email"
                        val bio = snapshot.child("bio").getValue(String::class.java) ?: "No Bio"
                        val profileImageUrl = snapshot.child("profileImage").getValue(String::class.java) ?: ""

                        view?.let {
                            txtUsername.text = username
                            txtPhoneNumber.text = phone
                            txtEmail.text = email
                            txtBio.text = bio

                            // ✅ Load profile image with Glide
                            if (profileImageUrl.isNotEmpty()) {
                                Glide.with(requireContext())
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.ic_profile) // Default profile icon
                                    .into(profileImageView)
                            }
                        }
                    } else {
                        showToast("User data not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isAdded || activity == null) return
                    showToast("Failed to load profile")
                }
            })
        } else {
            showToast("User not logged in")
        }
    }

    /**
     * Safely displays a Toast message
     */
    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
