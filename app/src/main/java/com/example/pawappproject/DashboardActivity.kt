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

        val citizenHomeFragment = CitizenHomeFragment()
        val citizenReportingFragment = CitizenReportingFragment()
        val citizenDonationFragment = CitizenDonationFragment()
        val citizenProfileFragment = CitizenProfileFragment()

        makeCurrentFragment(citizenHomeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeBtn -> makeCurrentFragment(citizenHomeFragment)
                R.id.reportBtn -> makeCurrentFragment(citizenReportingFragment)
                R.id.donationsBtn -> makeCurrentFragment(citizenDonationFragment)
                R.id.profilBtn -> makeCurrentFragment(citizenProfileFragment)
            }
            true
        }
    }

    private fun  makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace (R.id.fl_wrapper, fragment)
            commit()
        }
}