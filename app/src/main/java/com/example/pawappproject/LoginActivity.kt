package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityLoginBinding
import com.example.pawappproject.fragments.CitizenHomeFragment
import com.google.firebase.auth.FirebaseAuth

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
            val loginEmail = binding.LoginEmail.text.toString().trim()
            val loginPassword = binding.LoginPassword.text.toString().trim()
            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveEmailToPreferences(loginEmail)
                        val intent = Intent(this, DashboardActivity::class.java) // Redirect to Citizen Dashboard
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SignupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveEmailToPreferences(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userEmail", email)
        editor.apply()
    }
}
