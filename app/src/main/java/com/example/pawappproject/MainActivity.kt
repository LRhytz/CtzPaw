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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        createNotificationChannel()

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

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "report_submission_channel"
            val channelName = "Report Submissions"
            val channelDescription = "Notifies when a user successfully submits a report"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}