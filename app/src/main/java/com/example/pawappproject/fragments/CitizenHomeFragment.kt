package com.example.pawappproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
<<<<<<< HEAD
import com.example.pawappproject.EducationAwarenessCitizenActivity
import com.example.pawappproject.AdoptionListActivity
import com.example.pawappproject.R
=======
import com.example.pawappproject.AdoptionActivity
import com.example.pawappproject.R
import com.example.pawappproject.EducationAwarenessActivity
>>>>>>> origin/Archival_Branch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CitizenHomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find all the cards
        val adoptionCard: CardView = view.findViewById(R.id.adoptionCard)
        val educationCard: CardView = view.findViewById(R.id.educationCard)

        // Set click listeners for each card
        adoptionCard.setOnClickListener {
            showToast("Adoption Card Clicked")
<<<<<<< HEAD
            val intent = Intent(activity, AdoptionListActivity::class.java)
=======
            val intent = Intent(activity, AdoptionActivity::class.java)
>>>>>>> origin/Archival_Branch
            startActivity(intent)
        }


        educationCard.setOnClickListener {
            showToast("Education and Awareness Card Clicked")
            // Start the EducationAwarenessActivity
<<<<<<< HEAD
            val intent = Intent(activity, EducationAwarenessCitizenActivity::class.java)
=======
            val intent = Intent(activity, EducationAwarenessActivity::class.java)
>>>>>>> origin/Archival_Branch
            startActivity(intent)
        }

        return view
    }

    // Function to show toast
    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CitizenHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
