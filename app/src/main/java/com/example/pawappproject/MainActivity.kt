package com.example.pawappproject

import android.content.Intent
import android.content.SharedPreferences
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
        // User is logged in, redirect to DashboardActivity or HomeActivity
        if (isLoggedIn()) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val getStartedButton = findViewById<Button>(R.id.GetStartedBtn)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()

        if (currentFragment is ReportDetailsFragment) {
            if (!currentFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fl_wrapper)
    }

    // Check if user is logged in by getting the saved email from SharedPreferences
    private fun isLoggedIn(): Boolean {
        val userEmail = sharedPreferences.getString("userEmail", null)
        return userEmail != null && firebaseAuth.currentUser != null
    }
}
