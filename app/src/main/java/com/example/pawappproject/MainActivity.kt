package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pawappproject.fragments.ReportDetailsFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // Read stored userType to decide which dashboard to open
            val userType = sharedPreferences.getString("userType", null)
            if (userType != null) {
                if (userType == "Organization") {
                    startActivity(Intent(this, OrgDashboardActivity::class.java))
                } else { // Default to Citizen
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                finish()
                return
            }
        }

        setContentView(R.layout.activity_main)

        // Handle back press properly
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = getCurrentFragment()
                if (currentFragment is ReportDetailsFragment) {
                    if (!currentFragment.onBackPressed()) {
                        finish()
                    }
                } else {
                    finish()
                }
            }
        })

        // Setup "Login as Citizen" button
        val citizenButton = findViewById<Button>(R.id.CitizenBtn)
        citizenButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Setup "Login as Organization" button
        val orgButton = findViewById<Button>(R.id.OrgBtn)
        orgButton.setOnClickListener {
            val intent = Intent(this, OrganizationLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fragmentContainer)
    }

    private fun isLoggedIn(): Boolean {
        val userEmail = sharedPreferences.getString("userEmail", null)
        return userEmail != null && firebaseAuth.currentUser != null
    }
}
