package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityOrganizationSignupBinding
import com.google.firebase.auth.FirebaseAuth

class OrganizationSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.organizationSignupButton.setOnClickListener {
            val email = binding.organizationSignupEmailEditText.text.toString().trim()
            val password = binding.organizationSignupPasswordEditText.text.toString().trim()

            if (isValidEmail(email) && isValidPassword(password)) {
                registerOrganization(email, password)
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectOrg.setOnClickListener {
            val intent = Intent(this, OrganizationLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerOrganization(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                    navigateToOrganizationLogin()
                } else {
                    handleSignupError(task.exception?.message)
                }
            }
    }

    private fun handleSignupError(errorMessage: String?) {
        when {
            errorMessage?.contains("email address is already in use") == true -> {
                Toast.makeText(this, "This email is already registered. Please log in instead.", Toast.LENGTH_SHORT).show()
            }
            errorMessage?.contains("Recaptcha") == true -> {
                Toast.makeText(this, "Suspicious sign-up activity detected. Please try again later.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Signup failed: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToOrganizationLogin() {
        val intent = Intent(this, OrganizationLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }
}
