package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // Redirect to DashboardActivity if logged in
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Set up login buttons and any other UI for initial login (not shown here)
    }

    private fun isLoggedIn(): Boolean {
        val userEmail = sharedPreferences.getString("userEmail", null)
        return userEmail != null && firebaseAuth.currentUser != null
    }
}
