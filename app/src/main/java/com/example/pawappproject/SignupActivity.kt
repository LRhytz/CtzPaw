package com.example.pawappproject

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        setupDateOfBirthPicker()

        binding.BtnSignup.setOnClickListener {
            registerUserWithEmail()
        }
    }

<<<<<<< HEAD
=======
    /**
     * Function to open DatePickerDialog when Date of Birth field is clicked
     */
>>>>>>> origin/Archival_Branch
    private fun setupDateOfBirthPicker() {
        val dobTextView: TextView = binding.SignDob
        val calendar = Calendar.getInstance()

        dobTextView.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
<<<<<<< HEAD
=======

>>>>>>> origin/Archival_Branch
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dobTextView.text = dateFormat.format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
<<<<<<< HEAD
=======

>>>>>>> origin/Archival_Branch
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

<<<<<<< HEAD
=======
    /**
     * Register user with email verification
     */
>>>>>>> origin/Archival_Branch
    private fun registerUserWithEmail() {
        val firstName = binding.SignFirstName.text.toString().trim()
        val lastName = binding.SignLastName.text.toString().trim()
        val username = binding.SignUsername.text.toString().trim()
        val email = binding.SignEmail.text.toString().trim()
        val phone = binding.SignPhoneNumber.text.toString().trim()
        val password = binding.SignPassword.text.toString().trim()
        val confirmPassword = binding.SignConfirmPassword.text.toString().trim()
        val birthdate = binding.SignDob.text.toString().trim()

<<<<<<< HEAD
        if (!validateInputs(firstName, lastName, username, email, phone, password, confirmPassword, birthdate))
            return
=======
        if (!validateInputs(firstName, lastName, username, email, phone, password, confirmPassword, birthdate)) return
>>>>>>> origin/Archival_Branch

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEmailVerification()
                } else {
                    Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

<<<<<<< HEAD
=======
    /**
     * Validate input fields
     */
>>>>>>> origin/Archival_Branch
    private fun validateInputs(
        firstName: String, lastName: String, username: String, email: String,
        phone: String, password: String, confirmPassword: String, birthdate: String
    ): Boolean {
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthdate.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
<<<<<<< HEAD
=======

>>>>>>> origin/Archival_Branch
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }
<<<<<<< HEAD
=======

>>>>>>> origin/Archival_Branch
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
<<<<<<< HEAD
=======

>>>>>>> origin/Archival_Branch
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
<<<<<<< HEAD
        return true
    }

=======

        return true
    }

    /**
     * Send email verification link
     */
>>>>>>> origin/Archival_Branch
    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent. Check your inbox.", Toast.LENGTH_LONG).show()
<<<<<<< HEAD
                registerUser() // Save user data after verification email is sent
=======
                registerUser()
>>>>>>> origin/Archival_Branch
                firebaseAuth.signOut() // Force user to verify before logging in
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_LONG).show()
            }
        }
    }

<<<<<<< HEAD
=======
    /**
     * Save user data to Firebase Database
     */
>>>>>>> origin/Archival_Branch
    private fun registerUser() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userMap = mapOf(
            "firstName" to binding.SignFirstName.text.toString().trim(),
            "lastName" to binding.SignLastName.text.toString().trim(),
            "username" to binding.SignUsername.text.toString().trim(),
            "email" to binding.SignEmail.text.toString().trim(),
            "phone" to binding.SignPhoneNumber.text.toString().trim(),
            "birthdate" to binding.SignDob.text.toString().trim(),
            "profileImage" to "default",
<<<<<<< HEAD
            "bio" to "New user",
            "userType" to "Citizen"
=======
            "bio" to "New user"
>>>>>>> origin/Archival_Branch
        )

        databaseReference.child(userId).setValue(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Signup Successful! Please verify your email.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
