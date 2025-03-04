package com.example.pawappproject.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pawappproject.R
import com.example.pawappproject.Report
import com.google.firebase.database.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class ReportDetailsFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var reportId: String

    private lateinit var statusTextView: TextView
    private lateinit var reportTypeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var reporterEmailTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var reportImageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_details, container, false)

        // Initialize views
        statusTextView = view.findViewById(R.id.statusTextView)
        reportTypeTextView = view.findViewById(R.id.reportTypeTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        reporterEmailTextView = view.findViewById(R.id.reporterEmailTextView)
        dateTextView = view.findViewById(R.id.dateTextView)
        timeTextView = view.findViewById(R.id.timeTextView)
        locationTextView = view.findViewById(R.id.locationTextView)
        reportImageView = view.findViewById(R.id.reportImageView)
        videoView = view.findViewById(R.id.videoView)
        mapView = view.findViewById(R.id.mapView)

        // Retrieve report ID from arguments
        reportId = arguments?.getString("reportId") ?: ""
        if (reportId.isEmpty()) {
            Toast.makeText(context, "Invalid report ID", Toast.LENGTH_SHORT).show()
            return view
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("reports").child(reportId)

        // Initialize OSM configuration
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE))

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
        statusTextView.text = "Status: ${report.status}"
        reportTypeTextView.text = "Report Type: ${report.reportType}"
        descriptionTextView.text = "Details: ${report.reportDescription}"
        reporterEmailTextView.text = "Reporter Email: ${report.reportUserEmail}"

        // Parse and display date and time from report ID
        val reportDateTime = report.reportId.split("_")
        if (reportDateTime.size >= 3) {
            val date = reportDateTime[1]
            val time = reportDateTime[2]
            dateTextView.text = "Date: ${formatDate(date)}"
            timeTextView.text = "Time: ${formatTime(time)}"
        }

        val latitude = report.latitude
        val longitude = report.longitude

        if (latitude != null && longitude != null) {
            locationTextView.text = "Pinned Location: $latitude, $longitude"
            displayLocationOnMap(latitude, longitude)
        } else {
            locationTextView.text = "Pinned Location: Not available"
        }

        if (!report.imageUrls.isNullOrEmpty()) {
            Glide.with(this)
                .load(report.imageUrls[0])
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.img_placeholder_error)
                .into(reportImageView)
        } else {
            reportImageView.setImageResource(R.drawable.placeholder_image)
        }

        if (!report.videoUrl.isNullOrEmpty()) {
            videoView.setVideoURI(Uri.parse(report.videoUrl))
            videoView.start()
        } else {
            videoView.visibility = View.GONE
        }
    }

    private fun displayLocationOnMap(latitude: Double, longitude: Double) {
        val reportLocation = GeoPoint(latitude, longitude)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(reportLocation)

        val marker = Marker(mapView)
        marker.position = reportLocation
        marker.icon = resources.getDrawable(R.drawable.ic_location_pin, null)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
    }

    private fun formatDate(date: String): String {
        return if (date.length == 8) {
            val year = date.substring(0, 4)
            val month = date.substring(4, 6)
            val day = date.substring(6, 8)
            "$month/$day/$year"
        } else {
            "Invalid Date"
        }
    }

    private fun formatTime(time: String): String {
        return if (time.length == 6) {
            val hour = time.substring(0, 2).toInt()
            val minute = time.substring(2, 4)
            val second = time.substring(4, 6)
            val period = if (hour >= 12) "PM" else "AM"
            val adjustedHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
            "$adjustedHour:$minute:$second $period"
        } else {
            "Invalid Time"
        }
    }
}
