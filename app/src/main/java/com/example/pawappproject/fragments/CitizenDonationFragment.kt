package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawappproject.Donation
import com.example.pawappproject.DonationAdapter
import com.example.pawappproject.R
import com.google.firebase.database.*

class CitizenDonationFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var donationsList: MutableList<Donation>
    private lateinit var filteredList: MutableList<Donation>
    private lateinit var adapter: DonationAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation, container, false)
        listView = view.findViewById(R.id.listViewDonations)
        searchView = view.findViewById(R.id.searchViewDonations)

        donationsList = mutableListOf()
        filteredList = mutableListOf()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("donation_requests")

        // Initialize the adapter and set it to the ListView
        adapter = DonationAdapter(requireContext(), R.layout.list_item_donation, filteredList)
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
            val donationDetailsFragment = DonationDetailsFragment.newInstance(
                selectedDonation.name,
                selectedDonation.purpose,
                selectedDonation.gcashName,
                selectedDonation.gcashNumber,
                selectedDonation.details
            )

            // Replace the current fragment with the DonationDetailsFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_wrapper, donationDetailsFragment) // Use fl_wrapper as the container
                .addToBackStack(null) // Add to back stack to allow navigation back
                .commit()
        }

        return view
    }

    // Function to load data from Firebase and update the ListView
    private fun loadDonationsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationsList.clear()
                snapshot.children.mapNotNullTo(donationsList) {
                    it.getValue(Donation::class.java)
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

    // Function to filter donations based on search query
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
