package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pawappproject.R

class CitizenDonationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation, container, false)
        val listView = view.findViewById<ListView>(R.id.listViewDonations)
        val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)

        // Sample data for the ListView
        val donations = listOf(
            Donation("Animal Welfare Organization 1", "Provide medical care for rescued animals", "R***Z A.", "09123456789"),
            Donation("Animal Welfare Organization 2", "Support for shelter maintenance", "JO***H B.", "09876543210"),
            Donation("Animal Welfare Organization 3", "Feed and care for abandoned pets", "M**TH*W P.", "09121234567"),
            Donation("Animal Welfare Organization 4", "Education and awareness programs", "S***E L.", "09129876543"),
            Donation("Animal Welfare Organization 5", "Emergency response and rescue operations", "L**M T.", "09127654321")
        )

        // Adapter for the ListView
        val adapter = DonationAdapter(requireContext(), R.layout.list_item_donation, donations)
        listView.adapter = adapter

        return view
    }

    data class Donation(val name: String, val purpose: String, val gcashName: String, val gcashNumber: String)

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
