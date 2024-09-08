package com.example.pawappproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pawappproject.DonationDetailsActivity
import com.example.pawappproject.DonationRequestFormActivity
import com.example.pawappproject.R
import com.google.firebase.database.*

class CitizenDonationFragment : Fragment() {

    private lateinit var buttonAddDonation: Button
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
        buttonAddDonation = view.findViewById(R.id.buttonAddDonation)

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

        buttonAddDonation.setOnClickListener {
            val intent = Intent(requireContext(), DonationRequestFormActivity::class.java)
            startActivity(intent)
        }

        // Set up the item click listener for the ListView to show donation details
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDonation = filteredList[position]
            val intent = Intent(requireContext(), DonationDetailsActivity::class.java).apply {
                putExtra("donation_id", selectedDonation.id)  // Pass the donation ID
                putExtra("donation_name", selectedDonation.name)
                putExtra("donation_purpose", selectedDonation.purpose)
                putExtra("donation_gcashName", selectedDonation.gcashName)
                putExtra("donation_gcashNumber", selectedDonation.gcashNumber)
                putExtra("donation_details", selectedDonation.details)
            }
            startActivity(intent)
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
                // Show all donations by default (i.e., when no search is made)
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

    data class Donation(
        val id: String = "",  // Include an ID field to uniquely identify each donation
        val name: String = "",
        val purpose: String = "",
        val gcashName: String = "",
        val gcashNumber: String = "",
        val details: String = ""
    )

    class DonationAdapter(
        context: android.content.Context,
        resource: Int,
        objects: List<Donation>
    ) : ArrayAdapter<Donation>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_donation, parent, false)

            val organizationName = view.findViewById<TextView>(R.id.textViewOrganizationName)
            val purpose = view.findViewById<TextView>(R.id.textViewPurpose)
            val gcashName = view.findViewById<TextView>(R.id.textViewGcashName)
            val gcashNumber = view.findViewById<TextView>(R.id.textViewGcashNumber)

            val donation = getItem(position)!!

            organizationName.text = donation.name
            purpose.text = "Purpose/Details: ${donation.purpose}"
            gcashName.text = "GCash Name: ${donation.gcashName}"
            gcashNumber.text = "GCash Number: ${donation.gcashNumber}"

            return view
        }
    }
}
