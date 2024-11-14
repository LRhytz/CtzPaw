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
                } else {
                    Toast.makeText(context, "Report not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load report details", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun displayReportDetails(report: Report) {
        reportTypeTextView.text = report.reportType
        descriptionTextView.text = report.reportDescription
        reporterEmailTextView.text = report.reportUserEmail

        // Load the image if the report has an image URL
        if (!report.imageUrls.isNullOrEmpty()) {
            val imageUrl = report.imageUrls[0] // Assuming the first image is the main image to display
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.republic) // Error image in case of load failure
                .into(imageView)
        } else {
            // Set a placeholder or hide the ImageView if no image is available
            imageView.setImageResource(R.drawable.placeholder_image)
        }
    }

    fun onBackPressed(): Boolean {
        return false
    }
}
