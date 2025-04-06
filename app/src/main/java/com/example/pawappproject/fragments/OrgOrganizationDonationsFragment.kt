package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.example.pawappproject.OrgDonationAdapter
import com.example.pawappproject.OrgDonation
import com.example.pawappproject.OrgDonationDetailsFragmentFactory
import com.example.pawappproject.R

class OrgOrganizationDonationsFragment : Fragment() {

    private lateinit var buttonAddDonation: ImageButton
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var donationsList: MutableList<OrgDonation>
    private lateinit var filteredList: MutableList<OrgDonation>
    private lateinit var adapter: OrgDonationAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_org_organization_donations, container, false)
        listView = view.findViewById(R.id.listViewDonations)
        searchView = view.findViewById(R.id.searchViewDonations)
        buttonAddDonation = view.findViewById(R.id.buttonAddDonation)

        donationsList = mutableListOf()
        filteredList = mutableListOf()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("donation_requests")

        // Initialize the adapter and set it to the ListView
        adapter = OrgDonationAdapter(requireContext(), R.layout.list_item_donation, filteredList)
        listView.adapter = adapter

        // Load data from Firebase
        loadDonationsFromFirebase()

        // Set up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDonations(newText)
                return true
            }
        })

        // Set up the item click listener for the ListView to show donation details
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDonation = filteredList[position]
            val donationDetailsFragment = OrgDonationDetailsFragmentFactory.newInstance(
                selectedDonation.name,
                selectedDonation.purpose,
                selectedDonation.gcashName,
                selectedDonation.gcashNumber,
                selectedDonation.details,
                selectedDonation.id
            )

            // Replace the current fragment with DonationDetailsFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper, donationDetailsFragment)
                .addToBackStack(null)
                .commit()
        }

        // Navigate to AddDonationRequestFragment when Add Donation button is clicked
        buttonAddDonation.setOnClickListener {
            val addDonationRequestFragment = OrgAddDonationRequestFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper, addDonationRequestFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadDonationsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationsList.clear()
                snapshot.children.mapNotNullTo(donationsList) {
                    it.getValue(OrgDonation::class.java)
                }
                filteredList.clear()
                filteredList.addAll(donationsList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterDonations(query: String?) {
        val searchQuery = query?.lowercase() ?: ""

        filteredList.clear()
        donationsList.forEach { donation ->
            if (donation.name.lowercase().contains(searchQuery) ||
                donation.purpose.lowercase().contains(searchQuery) ||
                donation.gcashName.lowercase().contains(searchQuery) ||
                donation.gcashNumber.lowercase().contains(searchQuery)
            ) {
                filteredList.add(donation)
            }
        }
        adapter.notifyDataSetChanged()
    }
}
