package com.example.pawappproject.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawappproject.CitizenSubscriptionActivity
import com.example.pawappproject.DialogActivity
import com.example.pawappproject.LoginActivity
import com.example.pawappproject.MainActivity
import com.example.pawappproject.R
import com.google.firebase.auth.FirebaseAuth

class CitizenProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", 0)

        val logoutButton: LinearLayout = view.findViewById(R.id.LogoutBtn)
        logoutButton.setOnClickListener {
            clearUserSession()

            firebaseAuth.signOut()

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            Toast.makeText(activity, "Logging out.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            requireActivity().finish()
        }

        val aboutUsBtn: LinearLayout = view.findViewById(R.id.Aboutbtn)
        aboutUsBtn.setOnClickListener {
            val intent = Intent(requireActivity(), DialogActivity::class.java)
            Toast.makeText(activity, "Proceeding..", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        val CitizenPremiumSubBtn: LinearLayout = view.findViewById(R.id.PremiumSubBtn)
        CitizenPremiumSubBtn.setOnClickListener {
            val intent = Intent(requireActivity(), CitizenSubscriptionActivity::class.java)
            Toast.makeText(activity, "Proceeding..", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        return view
    }

    private fun clearUserSession() {
        val editor = sharedPreferences.edit()
        editor.remove("userEmail") // Remove the saved email
        editor.apply()
    }
}