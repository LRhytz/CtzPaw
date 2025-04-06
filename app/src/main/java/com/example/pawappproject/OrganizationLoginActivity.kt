package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawappproject.databinding.ActivityOrganizationLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrganizationLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        binding.organizationLoginButton.setOnClickListener {
            val email = binding.organizationEmailEditText.text.toString().trim()
            val password = binding.organizationPasswordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            // Read the organization record from the "organizations" node
                            val orgRef = FirebaseDatabase.getInstance().getReference("organizations").child(uid)
                            orgRef.get().addOnSuccessListener { snapshot ->
                                if (!snapshot.exists() || snapshot.child("userType").getValue(String::class.java) != "Organization") {
                                    Toast.makeText(
                                        this,
                                        "Wrong account type. Please log in with the correct account.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    auth.signOut()
                                    return@addOnSuccessListener
                                }
                                // Save email and userType in SharedPreferences
                                sharedPreferences.edit().apply {
                                    putString("userEmail", email)
                                    putString("userType", "Organization")
                                    apply()
                                }
                                startActivity(Intent(this, OrgDashboardActivity::class.java))
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "Unable to retrieve user information.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirecOrg.setOnClickListener {
            val intent = Intent(this, OrganizationSignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}