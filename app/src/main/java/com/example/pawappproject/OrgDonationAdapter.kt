package com.example.pawappproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter

class OrgDonationAdapter(
    context: Context,
    resource: Int,
    objects: List<OrgDonation>
) : ArrayAdapter<OrgDonation>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.org_list_item_donation, parent, false)

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