package com.example.pawappproject

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var saveProfileButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users").child("USER_ID") // Replace with user ID logic

    private val PICK_IMAGE = 1
    private var profileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.profileImageView)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        bioEditText = findViewById(R.id.bioEditText)
        birthdateEditText = findViewById(R.id.birthdateEditText)
        saveProfileButton = findViewById(R.id.saveProfileButton)

        // Load current user data into the fields (Optional)

        profileImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        birthdateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        saveProfileButton.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            birthdateEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveUserProfile() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val bio = bioEditText.text.toString().trim()
        val birthdate = birthdateEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || bio.isEmpty() || birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the profile details to Firebase
        val userProfile = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "bio" to bio,
            "birthdate" to birthdate,
            "profileImage" to profileImageUri.toString()
        )

        userRef.setValue(userProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile updated.", Toast.LENGTH_SHORT).show()
                finish() // Go back to the profile fragment
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.data
            profileImageView.setImageURI(profileImageUri)
        }
    }
}
