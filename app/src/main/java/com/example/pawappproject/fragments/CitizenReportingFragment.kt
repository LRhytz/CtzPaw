package com.example.pawappproject.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.pawappproject.R
import com.example.pawappproject.Report
import com.example.pawappproject.ReportingUtils
import androidx.recyclerview.widget.LinearLayoutManager           // UPDATED: For RecyclerView layout manager
import androidx.recyclerview.widget.RecyclerView                  // UPDATED: For RecyclerView
import com.example.pawappproject.R
import com.example.pawappproject.Report
import com.example.pawappproject.ReportingUtils
import com.example.pawappproject.ImagesAdapter                   // UPDATED: Using revised ImagesAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import org.osmdroid.bonuspack.location.GeocoderNominatim
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class CitizenReportingFragment : Fragment() {

    // Views
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var reportDescription: EditText
    private lateinit var submitButton: Button
    private lateinit var viewReportHistoryButton: ImageButton
    private lateinit var addMediasContainers: LinearLayout
    private lateinit var imageViews: List<ImageView>
    private lateinit var mapView: MapView

    private lateinit var videoPreview: VideoView
    private lateinit var selectVideoButton: Button
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var mapView: MapView

    private lateinit var videoPreview: VideoView
    private lateinit var selectVideoButton: ImageButton
    private var selectedVideoUri: Uri? = null
    private val MAX_VIDEO_SIZE_MB = 30
    private val MIN_DURATION_SECONDS = 3
    private val MAX_DURATION_SECONDS = 60
    private lateinit var videoFrame: FrameLayout
    private lateinit var removeVideoButton: ImageButton

    // Location
    private var selectedLocation: GeoPoint? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Image handling
    private val imageUris = mutableListOf<Uri>()
    private var photoUri: Uri? = null

    // UPDATED: For thumbnail and "Play" button
    private lateinit var videoThumbnail: ImageView
    private lateinit var playButton: ImageButton

    // Activity Result Launchers
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private var currentImageView: ImageView? = null

    // Firebase
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    // NEW: Adapter for RecyclerView & max images count
    private lateinit var imagesAdapter: ImagesAdapter
    private val MAX_IMAGES = 5

    // UPDATED: Optional ProgressBar for video upload (make sure to add one in your layout if you use this)
    private lateinit var videoUploadProgressBar: ProgressBar // UPDATED

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize Activity Result Launchers
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri: Uri? = data?.data
                    if (uri != null) {
                        addImageUri(uri)
                    }
                }
            }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (photoUri != null) {
                        saveImageToGallery(photoUri!!)
                        addImageUri(photoUri!!)
                    }
                }
            }

        // Request appropriate permissions
        requestAppropriatePermissions()

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference.child("reports")
        storage = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance().getReference("reports")
        storage = FirebaseStorage.getInstance().getReference()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        // Initialize map configuration
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reporting, container, false)

        // Initialize views
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView)
        reportDescription = view.findViewById(R.id.reportDescription)
        submitButton = view.findViewById(R.id.submitButton)
        viewReportHistoryButton = view.findViewById(R.id.viewReportHistoryButton)
        addMediasContainers = view.findViewById(R.id.addMediasContainers)
        mapView = view.findViewById(R.id.mapView)

        // Initialize AutoCompleteTextView
        ReportingUtils.initAutoCompleteTextView(requireContext(), autoCompleteTextView)

        // Initialize ImageViews and set click listeners
        imageViews = List(MAX_IMAGES) { index ->
            val imageView = addMediasContainers.getChildAt(index) as ImageView
            imageView.setOnClickListener {
                currentImageView = imageView
                showImagePickerDialog()
            }
            imageView
        }

        // Initialize video views
        videoPreview = view.findViewById(R.id.videoPreview)
        selectVideoButton = view.findViewById(R.id.selectVideoButton)

        // Handle video selection
        selectVideoButton.setOnClickListener {
            pickVideo()
        imageRecyclerView = view.findViewById(R.id.imageRecyclerView)
        mapView = view.findViewById(R.id.mapView)

        videoPreview = view.findViewById(R.id.videoPreview)
        selectVideoButton = view.findViewById(R.id.selectVideoButton)

        // In onCreateView, after inflating the layout:
        videoThumbnail = view.findViewById(R.id.videoThumbnail) // UPDATED
        playButton = view.findViewById(R.id.playButton)         // UPDATED
        videoFrame = view.findViewById(R.id.videoFrame)  // Correctly link the videoFrame here
        removeVideoButton = view.findViewById(R.id.removeVideoButton)


        val openLargeMapButton = view.findViewById<Button>(R.id.openLargeMapButton)

        // Initialize the remove video button click listener
        removeVideoButton.setOnClickListener {
            // Reset video URI and clear video preview
            selectedVideoUri = null
            videoPreview.setVideoURI(null)
            videoPreview.stopPlayback()

            // Hide video frame, play button, and thumbnail
            videoFrame.visibility = View.GONE
            playButton.visibility = View.GONE
            videoThumbnail.visibility = View.GONE

            // Show the select video button again
            selectVideoButton.visibility = View.VISIBLE

            // Call the function to update the visibility of the select video button
            updateSelectVideoButtonVisibility()
        }

        // In onCreateView or after you initialize the playButton:
        playButton.setOnClickListener { // UPDATED
            if (selectedVideoUri != null) {
                // Show the VideoView, hide thumbnail & button
                videoPreview.visibility = View.VISIBLE
                videoThumbnail.visibility = View.GONE
                playButton.visibility = View.GONE

                // Set the URI and start playback
                videoPreview.setVideoURI(selectedVideoUri)
                videoPreview.requestFocus()
                videoPreview.start()
            }
        }

        // UPDATED: Add media controls to the VideoView
        val mediaController = MediaController(requireContext())
        videoPreview.setMediaController(mediaController)
        // Instead of anchoring to the VideoView, anchor it to the FrameLayout that contains the VideoView
        mediaController.setAnchorView(view.findViewById(R.id.videoFrame))


        // UPDATED: Initialize optional ProgressBar (if present in your layout)
        videoUploadProgressBar = view.findViewById(R.id.videoUploadProgressBar) // UPDATED
        videoUploadProgressBar.visibility = View.GONE // UPDATED

        // Initialize AutoCompleteTextView
        ReportingUtils.initAutoCompleteTextView(requireContext(), autoCompleteTextView)

        // Set up RecyclerView with horizontal layout
        imageRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter(
            imageUrls = imageUris.map { it.toString() }.toMutableList(),
            maxImages = MAX_IMAGES,
            onAddClick = { showImagePickerDialog() },
            onRemoveClick = { position ->
                imageUris.removeAt(position)
                refreshImagesAdapter()
            }
        )
        imageRecyclerView.adapter = imagesAdapter

        // UPDATED: Let user choose to record or pick a video
        selectVideoButton.setOnClickListener {
            showVideoPickerOptions() // UPDATED
        }

        // Set up map
        setupMap()

        // Set click listeners for buttons
        submitButton.setOnClickListener {
            onSubmitButtonClick()
        }

        viewReportHistoryButton.setOnClickListener {
            onViewReportsButtonClick()
        }

        return view
    }

    // Pick video from gallery or record a new one
        openLargeMapButton.setOnClickListener {
            showLargeMapDialog() // Open the larger map dialog
        }

        // Check if there is a selected video and update button visibility accordingly
        updateSelectVideoButtonVisibility()

        return view
    }


    // Launchers for picking or recording video
    private val recordVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val videoUri = result.data?.data
            if (videoUri != null && validateVideo(videoUri)) {
                selectedVideoUri = videoUri
                showVideoThumbnail(videoUri)
            }
        }
    }

    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val videoUri = result.data?.data
            if (videoUri != null && validateVideo(videoUri)) {
                selectedVideoUri = videoUri
                showVideoThumbnail(videoUri)
            }
        }
    }

    // UPDATED: Offer user a choice between recording a video or picking from gallery
    private fun showVideoPickerOptions() {
        val options = arrayOf("Record Video", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Add Video")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCameraForVideo() // UPDATED
                1 -> pickVideo()          // existing method
            }
        }
        builder.show()
    }

    // UPDATED: Let the user record a video using camera
    private fun openCameraForVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        // Optionally limit duration or size: intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE) // UPDATED
    }

    // Pick video from gallery
    private fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        videoPickerLauncher.launch(intent)
    }

    // We still have onActivityResult for older logic
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_VIDEO_PICKER -> {
                    selectedVideoUri = data?.data
                    selectedVideoUri?.let { uri ->
                        videoPreview.setVideoURI(uri) // Set the video URI to the VideoView
                        videoPreview.start() // Start playing the video
                    val videoUri = data?.data
                    if (videoUri != null && validateVideo(videoUri)) {
                        // UPDATED: Set the URI and show thumbnail
                        selectedVideoUri = videoUri
                        showVideoThumbnail(videoUri)  // <-- call your thumbnail function here
                    } else {
                        Toast.makeText(requireContext(), "Invalid video selected", Toast.LENGTH_SHORT).show()
                    }
                }
                REQUEST_VIDEO_CAPTURE -> {
                    val recordedVideoUri = data?.data
                    if (recordedVideoUri != null && validateVideo(recordedVideoUri)) {
                        // UPDATED: Set the URI and show thumbnail
                        selectedVideoUri = recordedVideoUri
                        showVideoThumbnail(recordedVideoUri)  // <-- same idea here
                    } else {
                        Toast.makeText(requireContext(), "Invalid recorded video", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Register video picker result
    private val videoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val videoUri = result.data?.data
                if (videoUri != null && validateVideo(videoUri)) {
                    selectedVideoUri = videoUri
                    videoPreview.setVideoURI(videoUri)
                    videoPreview.start()
                    // Instead of directly calling videoPreview.setVideoURI(...)
                    // show the thumbnail first
                    showVideoThumbnail(videoUri)
                } else {
                    Toast.makeText(requireContext(), "Invalid video selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // UPDATED: Helper function to create and display a thumbnail
    private fun showVideoThumbnail(videoUri: Uri) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(requireContext(), videoUri)
            val bitmap = retriever.getFrameAtTime(500000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            retriever.release()

            if (bitmap != null) {
                // Show thumbnail
                videoThumbnail.setImageBitmap(bitmap)
                videoThumbnail.visibility = View.VISIBLE

                // Hide the VideoView and show the Play button
                videoPreview.visibility = View.GONE
                playButton.visibility = View.VISIBLE
            } else {
                videoThumbnail.visibility = View.GONE
                videoPreview.visibility = View.VISIBLE
            }

            // Show the video frame when a video is selected
            videoFrame.visibility = View.VISIBLE

            // Hide the select video button once a video is selected
            updateSelectVideoButtonVisibility()

        } catch (e: Exception) {
            e.printStackTrace()
            videoThumbnail.visibility = View.GONE
            videoPreview.visibility = View.VISIBLE
        }
    }

    private fun updateSelectVideoButtonVisibility() {
        if (selectedVideoUri != null) {
            selectVideoButton.visibility = View.GONE // Hide the select video button if video is selected
        } else {
            selectVideoButton.visibility = View.VISIBLE // Show the select video button if no video is selected
        }
    }

    // Validate video constraints
    private fun validateVideo(videoUri: Uri): Boolean {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(requireContext(), videoUri)

        // Get video duration
        val durationMillis =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        val durationSeconds = durationMillis / 1000

        // Get video resolution
        val videoWidth =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
        val videoHeight =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0

        // Get file size
        val fileSizeBytes = requireContext().contentResolver.openFileDescriptor(videoUri, "r")?.statSize
        val fileSizeMB = (fileSizeBytes ?: 0) / (1024 * 1024)

        retriever.release()

        // Check constraints
        return when {
            fileSizeMB > MAX_VIDEO_SIZE_MB -> {
                Toast.makeText(requireContext(), "Video size exceeds 30MB", Toast.LENGTH_SHORT).show()
                false
            }
            durationSeconds < MIN_DURATION_SECONDS || durationSeconds > MAX_DURATION_SECONDS -> {
                Toast.makeText(requireContext(), "Video duration must be between 10-60 seconds", Toast.LENGTH_SHORT).show()
            // UPDATED: Consistent error message with MIN_DURATION_SECONDS
            durationSeconds < MIN_DURATION_SECONDS || durationSeconds > MAX_DURATION_SECONDS -> {
                Toast.makeText(requireContext(), "Video duration must be between $MIN_DURATION_SECONDS-$MAX_DURATION_SECONDS seconds", Toast.LENGTH_SHORT).show() // UPDATED
                false
            }
            videoWidth > 1280 || videoHeight > 1280 -> {
                Toast.makeText(requireContext(), "Video resolution exceeds 1280x1280", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
    // Setup Maps
    private fun setupMap() {
        mapView.setMultiTouchControls(true)
        val startPoint = GeoPoint(14.5995, 120.9842) // Example starting location (Manila)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(startPoint)

        // Add a draggable marker to represent the user’s location
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.isDraggable = true
        mapView.overlays.add(marker)

        // Handle touch events directly on the map
        mapView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disable parent scroll when touching the map
                    mapView.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP -> {
                    // Re-enable parent scroll when releasing touch
                    mapView.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            mapView.onTouchEvent(event) // Let map handle the touch event
        }

        // Optional: Marker drag listener to update location when dragged
        marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker?) {}

            override fun onMarkerDragEnd(marker: Marker?) {
                selectedLocation = marker?.position
                Toast.makeText(requireContext(), "Location selected: ${selectedLocation?.latitude}, ${selectedLocation?.longitude}", Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragStart(marker: Marker?) {}
        })
    // UPDATED: Setup Maps with immediate retrieval of user's current location using location updates
    private fun setupMap() {
        mapView.setMultiTouchControls(true)

        // Request the current location
        requestCurrentLocation { location ->
            val userPoint = GeoPoint(location.latitude, location.longitude)
            selectedLocation = userPoint
            mapView.overlays.clear()
            mapView.controller.setCenter(userPoint)
            mapView.controller.setZoom(15.0)

            // Add a draggable marker at the user's current location
            val marker = Marker(mapView)
            marker.position = userPoint
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.isDraggable = true
            mapView.overlays.add(marker)

            // Update selectedLocation when marker is dragged
            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker?) {}
                override fun onMarkerDragEnd(marker: Marker?) {
                    selectedLocation = marker?.position
                    if (selectedLocation != null) {
                        getAddressFromLocation(selectedLocation!!.latitude, selectedLocation!!.longitude) { address ->
                            Toast.makeText(requireContext(), "Selected: $address", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onMarkerDragStart(marker: Marker?) {}
            })

            mapView.invalidate()
        }

        // Fallback: if current location isn't obtained in time, use default coordinates
        if (selectedLocation == null) {
            val defaultPoint = GeoPoint(10.339693309759678, 123.91085491157197)
            selectedLocation = defaultPoint
            mapView.controller.setCenter(defaultPoint)
            mapView.controller.setZoom(15.0)

            // Add a marker at the default location
            val marker = Marker(mapView)
            marker.position = defaultPoint
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.isDraggable = true
            mapView.overlays.add(marker)

            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker?) {}
                override fun onMarkerDragEnd(marker: Marker?) {
                    selectedLocation = marker?.position
                    if (selectedLocation != null) {
                        getAddressFromLocation(selectedLocation!!.latitude, selectedLocation!!.longitude) { address ->
                            Toast.makeText(requireContext(), "Selected: $address", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onMarkerDragStart(marker: Marker?) {}
            })
        }
    }


    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, get the last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    callback(location)
                } else {
                    // Handle the case where location is null (e.g., GPS is off or no recent location)
                    Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    // UPDATED: Request current location using location updates
    private fun requestCurrentLocation(callback: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Create a location request with high accuracy
            val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                interval = 10000 // 10 seconds
                fastestInterval = 5000 // 5 seconds
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                numUpdates = 1 // Only need one update
            }

            val locationCallback = object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    if (location != null) {
                        callback(location)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                android.os.Looper.getMainLooper()
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // UPDATED: Reverse geocoding - get an address from latitude/longitude
    private fun getAddressFromLocation(latitude: Double, longitude: Double, callback: (String) -> Unit) {
        Thread {
            try {
                val geocoder = GeocoderNominatim("MyUserAgent")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    requireActivity().runOnUiThread {
                        callback(address)
                    }
                } else {
                    requireActivity().runOnUiThread {
                        callback("Unknown location")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    callback("Error retrieving address")
                }
            }
        }.start()
    }

    // UPDATED: Function to show a large map dialog with an integrated search bar
    private fun showLargeMapDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_large_map, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Select Location")
            .setPositiveButton("OK") { _, _ ->
                // When confirmed, update selectedLocation and sync the small map
                // Here, selectedLocation is already updated via the marker's drag listener in the dialog
                updateSmallMap()
            }
            .setNegativeButton("Cancel", null)
            .create()

        // Initialize the large map view
        val largeMapView = dialogView.findViewById<MapView>(R.id.largeMapView)
        largeMapView.setMultiTouchControls(true)
        // Use the shared selectedLocation or default if null
        val initialPoint = selectedLocation ?: GeoPoint(10.339693309759678, 123.91085491157197)
        largeMapView.controller.setCenter(initialPoint)
        largeMapView.controller.setZoom(15.0)

        // Add a marker to the large map at the current location
        val marker = Marker(largeMapView)
        marker.position = initialPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.isDraggable = true
        largeMapView.overlays.add(marker)

        // Update selectedLocation when marker is dragged in the dialog
        marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker?) {}
            override fun onMarkerDragEnd(marker: Marker?) {
                selectedLocation = marker?.position
                Toast.makeText(requireContext(), "Selected: ${selectedLocation?.latitude}, ${selectedLocation?.longitude}", Toast.LENGTH_SHORT).show()
            }
            override fun onMarkerDragStart(marker: Marker?) {}
        })

        // Setup search within the dialog (code omitted here for brevity; keep your existing search code)

        dialog.show()
    }

    // UPDATED: Helper function to update the small map with the current selectedLocation
    private fun updateSmallMap() {
        selectedLocation?.let { location ->
            // Clear existing overlays
            mapView.overlays.clear()
            // Center the map on the selected location
            mapView.controller.setCenter(location)
            mapView.controller.setZoom(15.0)

            // Add a draggable marker at the selected location
            val marker = Marker(mapView)
            marker.position = location
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_pin)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.isDraggable = true
            mapView.overlays.add(marker)

            // Marker drag listener to update location when moved
            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker?) {}
                override fun onMarkerDragEnd(marker: Marker?) {
                    selectedLocation = marker?.position
                    if (selectedLocation != null) {
                        getAddressFromLocation(selectedLocation!!.latitude, selectedLocation!!.longitude) { address ->
                            Toast.makeText(requireContext(), "Selected: $address", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onMarkerDragStart(marker: Marker?) {}
            })

            mapView.invalidate()
        }
    }

    // Show a dialog for picking an image from camera or gallery
    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Add Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera() // Open camera option
                1 -> pickImageFromGallery() // Choose from gallery option
                0 -> openCamera()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        if (hasCameraPermission()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoUri = createImageFile()?.let { file ->
                FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        } else {
            requestCameraPermission()
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
        } catch (ex: IOException) {
            null
        }
    }

    private fun pickImageFromGallery() {
        if (hasReadPermission()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        } else {
            requestStoragePermission()
        }
    }

    private fun hasReadPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAppropriatePermissions() {
        val permissions = mutableListOf<String>()
        if (!hasReadPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        if (!hasCameraPermission()) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions.toTypedArray(),
                REQUEST_READ_STORAGE_PERMISSION
            )
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_STORAGE_PERMISSION
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE_PERMISSION
            )
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission was granted, retry getting the current location
                getCurrentLocation { location ->
                    selectedLocation = GeoPoint(location.latitude, location.longitude)
                    // Update the map and marker position
                    mapView.controller.setCenter(selectedLocation)
                    // Move the marker to the new location (if you added one)
                }
            } else {
                // Permission was denied, show a message or handle accordingly
                getCurrentLocation { location ->
                    selectedLocation = GeoPoint(location.latitude, location.longitude)
                    mapView.controller.setCenter(selectedLocation)
                }
            } else {
                Toast.makeText(requireContext(), "Location permission required to select location", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addImageUri(uri: Uri) {
        if (imageUris.size < MAX_IMAGES) {
            imageUris.add(uri)
            currentImageView?.setImageURI(uri)
        } else {
            Toast.makeText(
                requireContext(),
                "You can add up to $MAX_IMAGES images",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun saveImageToGallery(uri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val resolver = requireContext().contentResolver
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { galleryUri ->
            resolver.openOutputStream(galleryUri)?.use { outputStream ->
                resolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun onSubmitButtonClick() {
        val reportType = autoCompleteTextView.text.toString()
        val description = reportDescription.text.toString()
        val user = firebaseAuth.currentUser
    // Declare this flag at the class level
    private var isSubmitting = false

    private fun onSubmitButtonClick() {
        if (isSubmitting) return
        isSubmitting = true
        submitButton.isEnabled = false

        val reportType = autoCompleteTextView.text.toString()
        val description = reportDescription.text.toString()
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (reportType.isEmpty() || description.isEmpty() || imageUris.isEmpty() || userEmail == null || selectedLocation == null) {
            Toast.makeText(
                requireContext(),
                "Please complete all fields, add at least one image, and ensure you are logged in and location selected",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

            isSubmitting = false
            submitButton.isEnabled = true
            return
        }
        // Generate a custom ID for the report
        val reportTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val reportId = "report_$reportTimestamp"

        val imageUrls = mutableListOf<String>()
        var imagesUploaded = 0

        for (uri in imageUris) {
            val storageRef = storage.child("reports").child(reportId).child(uri.lastPathSegment ?: "image.jpg")
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString())
                    imagesUploaded++
                    if (imagesUploaded == imageUris.size) {
                        val report = Report(
                            reportId = reportId,
                            reportType = reportType,
                            reportDescription = description,
                            imageUrls = imageUrls,
                            reportUserEmail = userEmail,
                            latitude = selectedLocation?.latitude,
                            longitude = selectedLocation?.longitude
                        )

                        // Check if there's a video to upload
                        if (selectedVideoUri != null) {
                            val videoRef = storage.child("reports").child(reportId).child("report_video.mp4")
                            videoRef.putFile(selectedVideoUri!!).addOnSuccessListener {
                                videoRef.downloadUrl.addOnSuccessListener { videoUrl ->
                                    // Add video URL to the report and save it to the database
                                    report.videoUrl = videoUrl.toString()
                                    saveReportToDatabase(report)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to upload video", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // No video, save the report directly
                        if (selectedVideoUri != null) {
                            val videoRef = storage.child("reports").child(reportId).child("report_video.mp4")

                            // UPDATED: Show progress bar before upload
                            videoUploadProgressBar.visibility = View.VISIBLE // UPDATED

                            videoRef.putFile(selectedVideoUri!!)
                                .addOnProgressListener { taskSnapshot -> // UPDATED
                                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                                    videoUploadProgressBar.progress = progress // UPDATED
                                }
                                .addOnSuccessListener {
                                    videoRef.downloadUrl.addOnSuccessListener { videoUrl ->
                                        report.videoUrl = videoUrl.toString()
                                        saveReportToDatabase(report)
                                        videoUploadProgressBar.visibility = View.GONE // UPDATED
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed to upload video", Toast.LENGTH_SHORT).show()
                                    videoUploadProgressBar.visibility = View.GONE // UPDATED
                                    isSubmitting = false
                                    submitButton.isEnabled = true
                                }
                        } else {
>>>>>>> origin/Archival_Branch
                            saveReportToDatabase(report)
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                isSubmitting = false
                submitButton.isEnabled = true
            }
        }
    }

    private fun showReportNotification(context: Context) {
        val channelId = "report_submission_channel"

        // Check for notification permission on Android 13+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted; you might choose to request it or simply skip posting the notification
            return
        }

        try {
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_send) // Replace with your custom icon if available
                .setContentTitle("Report Submitted")
                .setContentText("Your report has been successfully submitted.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(1001, builder.build())
            }
        } catch (e: SecurityException) {
            // Handle the SecurityException gracefully if it occurs
            e.printStackTrace()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    private fun saveReportToDatabase(report: Report) {
        database.child(report.reportId).setValue(report).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Report submitted successfully", Toast.LENGTH_SHORT).show()
                // Clear fields
                autoCompleteTextView.text.clear()
                reportDescription.text.clear()
                imageUris.clear()
                imageViews.forEach { imageView -> imageView.setImageURI(null) }
            } else {
                Toast.makeText(requireContext(), "Failed to submit report", Toast.LENGTH_SHORT).show()
            }
        }
    }
                // Trigger the notification here
                showReportNotification(requireContext())
                autoCompleteTextView.text.clear()
                reportDescription.text.clear()
                imageUris.clear()
                // Refresh your images adapter if needed
            } else {
                Toast.makeText(requireContext(), "Failed to submit report", Toast.LENGTH_SHORT).show()
            }
            isSubmitting = false
            submitButton.isEnabled = true
        }
    }

    private fun onViewReportsButtonClick() {
        val fragment = ViewReportsFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_wrapper, fragment)
            .addToBackStack(null)
            .commit()
    }
    // UPDATED: (Optional) Let user remove a selected video if you add a "removeVideoButton" in layout
    /*
    private lateinit var removeVideoButton: Button // UPDATED

    // In onCreateView, after initializing videoPreview:
    removeVideoButton = view.findViewById(R.id.removeVideoButton) // UPDATED
    removeVideoButton.setOnClickListener {
        removeSelectedVideo()
    }

    private fun removeSelectedVideo() {
        selectedVideoUri = null
        videoPreview.setVideoURI(null)
        videoPreview.stopPlayback()
    }
    */

    private fun refreshImagesAdapter() {
        val newList = imageUris.map { it.toString() }.toMutableList()
        imagesAdapter.updateImages(newList)
    }

    private fun addImageUri(uri: Uri) {
        if (imageUris.size < MAX_IMAGES) {
            imageUris.add(uri)
            refreshImagesAdapter()
        } else {
            Toast.makeText(requireContext(), "You can only add up to $MAX_IMAGES images", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        private const val REQUEST_VIDEO_PICKER = 1001
        private const val REQUEST_READ_STORAGE_PERMISSION = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
        private const val MAX_IMAGES = 5
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100

        // UPDATED: Add request code for recording video
        private const val REQUEST_VIDEO_CAPTURE = 2001 // UPDATED
    }
}
