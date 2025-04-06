package com.example.pawappproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.pawappproject.R
import com.example.pawappproject.OrgEducAndAwarenessActivity


class OrgOrganizationHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_org_organization_home, container, false)

        val adoptionCard: CardView = view.findViewById(R.id.adoptionCard)
        val educationCard: CardView = view.findViewById(R.id.educationCard)

        adoptionCard.setOnClickListener {
            showToast("Adoption Card Clicked")
        }

        educationCard.setOnClickListener {
            val intent = Intent(activity, OrgEducAndAwarenessActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
