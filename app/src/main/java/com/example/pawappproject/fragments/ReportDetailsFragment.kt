package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawappproject.R
import com.example.pawappproject.models.Report
import com.google.firebase.database.*

class ReportDetailsFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_report_details, container, false)

        reportTypeTextView = view.findViewById(R.id.reportTypeTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        reporterEmailTextView = view.findViewById(R.id.reporterEmailTextView)
        imageView = view.findViewById(R.id.reportImageView)

        // Get report ID from arguments
        reportId = requireArguments().getString("reportId") ?: ""

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("reports").child(reportId)

        // Fetch report details from Firebase
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val report = snapshot.getValue(Report::class.java)
                    report?.let {
                        displayReportDetails(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                // For example:
                Toast.makeText(context, "Failed to load report details", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun displayReportDetails(report: Report) {
        reportTypeTextView.text = report.reportType
        descriptionTextView.text = report.reportDescription
        reporterEmailTextView.text = report.reportUserEmail

        // Load image from Firebase storage (if you have stored images)
        // For example:
        // Picasso.get().load(report.imageUrl).into(imageView)
    }

    fun onBackPressed(): Boolean {
        // Handle back press logic here
        // For example, if you want to consume the back press:
        // return true
        // If you want to let the activity handle the back press:
        return false
    }
}
