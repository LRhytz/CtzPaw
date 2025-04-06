package com.example.pawappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.pawappproject.fragments.OrgOrganizationHomeFragment
import com.example.pawappproject.fragments.OrgOrganizationReportsFragment
import com.example.pawappproject.fragments.OrgOrganizationDonationsFragment
import com.example.pawappproject.fragments.OrgOrganizationProfileFragment

class OrgDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_dashboard)

        // Load Org Home Fragment as the initial fragment
        makeCurrentFragment(OrgOrganizationHomeFragment())

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.Org_bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Org_homeBtn -> makeCurrentFragment(OrgOrganizationHomeFragment())
                R.id.Org_reportsBtn -> makeCurrentFragment(OrgOrganizationReportsFragment())
                R.id.Org_donationsBtn -> makeCurrentFragment(OrgOrganizationDonationsFragment())
                R.id.Org_profileBtn -> makeCurrentFragment(OrgOrganizationProfileFragment())
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