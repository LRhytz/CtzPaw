package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        binding.BtnLogin.setOnClickListener {
            loginUser()
        }

        binding.SignupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnResendVerification.setOnClickListener {
            resendVerificationEmail()
        }
    }

    private fun loginUser() {
        val email = binding.LoginEmail.text.toString().trim()
        val password = binding.LoginPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.isEmailVerified) {
                        val uid = user.uid
                        // Read the citizen record from the "users" node
                        val dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
                        dbRef.get().addOnSuccessListener { snapshot ->
                            if (!snapshot.exists() || snapshot.child("userType").getValue(String::class.java) != "Citizen") {
                                Toast.makeText(
                                    this,
                                    "Wrong account type. Please log in with the correct account.",
                                    Toast.LENGTH_LONG
                                ).show()
                                firebaseAuth.signOut()
                                return@addOnSuccessListener
                            }
                            // Save data in SharedPreferences
                            sharedPreferences.edit().apply {
                                putString("userEmail", email)
                                putString("userType", "Citizen")
                                apply()
                            }
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        }
                    } else {
                        firebaseAuth.signOut()
                        Toast.makeText(
                            this,
                            "Please verify your email before logging in.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun resendVerificationEmail() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email resent. Check your inbox.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
