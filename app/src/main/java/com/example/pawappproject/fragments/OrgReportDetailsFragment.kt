package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.example.pawappproject.Report
import com.example.pawappproject.R

class OrgReportDetailsFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var reportId: String

    private lateinit var reportTypeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var reporterEmailTextView: TextView
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_org_report_details, container, false)

        // Initialize views
        reportTypeTextView = view.findViewById(R.id.reportTypeTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        reporterEmailTextView = view.findViewById(R.id.reporterEmailTextView)
        imageView = view.findViewById(R.id.reportImageView)

        // Retrieve report ID from arguments
        reportId = arguments?.getString("reportId") ?: ""

        if (reportId.isEmpty()) {
            Toast.makeText(context, "Invalid report ID", Toast.LENGTH_SHORT).show()
            return view
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("reports").child(reportId)

        // Fetch report details
        fetchReportDetails()

        return view
    }

    private fun fetchReportDetails() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val report = snapshot.getValue(Report::class.java)
                    report?.let { displayReportDetails(it) }
                } else {
                    Toast.makeText(context, "Report not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load report details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayReportDetails(report: Report) {
        reportTypeTextView.text = report.reportType
        descriptionTextView.text = report.reportDescription
        reporterEmailTextView.text = report.reportUserEmail

        // Display image if available
        if (!report.imageUrls.isNullOrEmpty()) {
            val imageUrl = report.imageUrls[0] // Display the first image
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.img_placeholder_error) // Error image in case of load failure
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)
        }
    }

    fun onBackPressed(): Boolean {
        // Override this method if custom back navigation logic is needed
        return false
    }
}
