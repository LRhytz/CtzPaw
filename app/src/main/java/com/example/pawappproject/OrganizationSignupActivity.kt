package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityOrganizationSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrganizationSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        // Change the reference path to "organizations"
        databaseReference = FirebaseDatabase.getInstance().getReference("organizations")

        binding.BtnSignup.setOnClickListener {
            registerOrganizationWithEmail()
        }
    }

    private fun registerOrganizationWithEmail() {
        val orgName = binding.SignOrgName.text.toString().trim()
        val email = binding.SignEmail.text.toString().trim()
        val phone = binding.SignPhoneNumber.text.toString().trim()
        val address = binding.SignAddress.text.toString().trim()
        val bio = binding.SignBio.text.toString().trim()
        val foundingYear = binding.SignFoundingYear.text.toString().trim()
        val adminName = binding.SignAdminName.text.toString().trim()
        val adminEmail = binding.SignAdminEmail.text.toString().trim()
        val password = binding.SignPassword.text.toString().trim()
        val confirmPassword = binding.SignConfirmPassword.text.toString().trim()

        if (!validateInputs(orgName, email, phone, address, bio, foundingYear, adminName, adminEmail, password, confirmPassword))
            return

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEmailVerification()
                } else {
                    Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun validateInputs(
        orgName: String, email: String, phone: String, address: String, bio: String,
        foundingYear: String, adminName: String, adminEmail: String, password: String,
        confirmPassword: String
    ): Boolean {
        if (orgName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() ||
            bio.isEmpty() || foundingYear.isEmpty() || adminName.isEmpty() || adminEmail.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent. Check your inbox.", Toast.LENGTH_LONG).show()
                // Only call saveOrganizationData() after email verification is sent
                saveOrganizationData()
            } else {
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveOrganizationData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val organizationMap = mapOf(
            "orgName" to binding.SignOrgName.text.toString().trim(),
            "email" to binding.SignEmail.text.toString().trim(),
            "phone" to binding.SignPhoneNumber.text.toString().trim(),
            "address" to binding.SignAddress.text.toString().trim(),
            "bio" to binding.SignBio.text.toString().trim(),
            "foundingYear" to binding.SignFoundingYear.text.toString().trim(),
            "adminName" to binding.SignAdminName.text.toString().trim(),
            "adminEmail" to binding.SignAdminEmail.text.toString().trim(),
            "profileImage" to "default",
            "userType" to "Organization"
        )

        // Write to "organizations" node with userId as the key
        databaseReference.child(userId).setValue(organizationMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Signup Successful! Please verify your email.", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
                // Redirect to OrganizationLoginActivity
                startActivity(Intent(this, OrganizationLoginActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save organization data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
