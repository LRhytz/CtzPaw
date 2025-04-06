package com.example.pawappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pawappproject.fragments.CitizenDonationFragment
import com.example.pawappproject.fragments.CitizenHomeFragment
import com.example.pawappproject.fragments.CitizenProfileFragment
import com.example.pawappproject.fragments.CitizenReportingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Load CitizenHomeFragment as the initial fragment
        makeCurrentFragment(CitizenHomeFragment())

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.Ctz_bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeBtn -> makeCurrentFragment(CitizenHomeFragment())
                R.id.reportBtn -> makeCurrentFragment(CitizenReportingFragment())
                R.id.donationsBtn -> makeCurrentFragment(CitizenDonationFragment())
                R.id.profilBtn -> makeCurrentFragment(CitizenProfileFragment())
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
