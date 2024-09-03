package com.example.pawappproject.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
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
import com.example.pawappproject.ViewReportsActivity
import com.example.pawappproject.models.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CitizenReportingFragment : Fragment() {

    // Views
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var reportDescription: EditText
    private lateinit var submitButton: Button
    private lateinit var viewReportsButton: Button
    private lateinit var addMediasContainers: LinearLayout

    // Image handling
    private val imageUris = mutableListOf<Uri>()
    private var photoUri: Uri? = null

    // Activity Result Launchers
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private var currentImageView: ImageView? = null

    // Firebase
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
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
        viewReportsButton = view.findViewById(R.id.viewReportsButton)
        addMediasContainers = view.findViewById(R.id.addMediasContainers)

        // Initialize AutoCompleteTextView
        initAutoCompleteTextView()

        // Set click listeners for image views
        for (i in 0 until MAX_IMAGES) {
            val imageView = addMediasContainers.getChildAt(i) as ImageView
            imageView.setOnClickListener {
                currentImageView = imageView
                showImagePickerDialog()
            }
        }

        // Set click listeners for buttons
        submitButton.setOnClickListener {
            onSubmitButtonClick()
        }

        viewReportsButton.setOnClickListener {
            onViewReportsButtonClick()
        }

        return view
    }

    private fun initAutoCompleteTextView() {
        val items = arrayOf(
            "Animal Abuse/Neglect",
            "Stray Animal Sighting",
            "Injured Animal",
            "Abandoned Animal",
            "Lost/Found Pet",
            "Animal Rescue Request",
            "Animal Cruelty Suspected",
            "Animal Emergency",
            "Trap-Neuter-Return (TNR) Request",
            "Animal Shelter Support Needed"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Add Image")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera() // Open camera option
                1 -> pickImageFromGallery() // Choose from gallery option
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
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
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
        when (requestCode) {
            REQUEST_READ_STORAGE_PERMISSION, REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImagePickerDialog()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission required to add images",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (reportType.isEmpty() || description.isEmpty() || imageUris.isEmpty() || userEmail == null) {
            Toast.makeText(
                requireContext(),
                "Please complete all fields, add at least one image, and ensure you are logged in",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Generate a custom ID for the report
        val reportTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val userId = user?.uid ?: "unknownUser"
        val reportId = "report_$reportTimestamp"

        val imageUrls = mutableListOf<String>()
        var imagesUploaded = 0

        for (uri in imageUris) {
            val storageRef = storage.child("reports").child(reportId).child(uri.lastPathSegment ?: "image.jpg")
            storageRef.putFile(uri).addOnSuccessListener { uploadTask ->
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString())
                    imagesUploaded++
                    if (imagesUploaded == imageUris.size) {
                        val report = Report(reportId, reportType, description, imageUrls, userEmail)
                        database.child(reportId).setValue(report).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Report submitted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to submit report",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed to upload image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onViewReportsButtonClick() {
        val intent = Intent(requireContext(), ViewReportsActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_READ_STORAGE_PERMISSION = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
        private const val MAX_IMAGES = 5
    }
}
