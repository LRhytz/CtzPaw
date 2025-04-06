package com.example.pawappproject

import android.os.Bundle
import com.example.pawappproject.fragments.DonationDetailsFragment

object OrgDonationDetailsFragmentFactory {
    fun newInstance(
        name: String,
        purpose: String,
        gcashName: String,
        gcashNumber: String,
        details: String,
        id: String
    ): DonationDetailsFragment {
        val fragment = DonationDetailsFragment()
        val args = Bundle()
        args.putString("donation_name", name)
        args.putString("donation_purpose", purpose)
        args.putString("donation_gcashName", gcashName)
        args.putString("donation_gcashNumber", gcashNumber)
        args.putString("donation_details", details)
        args.putString("donation_id", id)
        fragment.arguments = args
        return fragment
    }
}