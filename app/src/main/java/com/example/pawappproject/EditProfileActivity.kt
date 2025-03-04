package com.example.pawappproject

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: CircleImageView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var saveProfileButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: FirebaseStorage
    private var profileImageUri: Uri? = null

    private val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.profileImageView)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        bioEditText = findViewById(R.id.bioEditText)
        birthdateEditText = findViewById(R.id.birthdateEditText)
        saveProfileButton = findViewById(R.id.saveProfileButton)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        storageReference = FirebaseStorage.getInstance()

        loadUserProfile()

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

    private fun loadUserProfile() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    firstNameEditText.setText(snapshot.child("firstName").getValue(String::class.java) ?: "")
                    lastNameEditText.setText(snapshot.child("lastName").getValue(String::class.java) ?: "")
                    usernameEditText.setText(snapshot.child("username").getValue(String::class.java) ?: "")
                    phoneEditText.setText(snapshot.child("phone").getValue(String::class.java) ?: "")
                    bioEditText.setText(snapshot.child("bio").getValue(String::class.java) ?: "")
                    birthdateEditText.setText(snapshot.child("birthdate").getValue(String::class.java) ?: "")

                    val profileImageUrl = snapshot.child("profileImage").getValue(String::class.java)
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(this@EditProfileActivity)
                            .load(profileImageUrl)
                            .into(profileImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })
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
        val userId = firebaseAuth.currentUser?.uid ?: return

        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val bio = bioEditText.text.toString().trim()
        val birthdate = birthdateEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || phone.isEmpty() || bio.isEmpty() || birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val userProfile = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "username" to username,
            "phone" to phone,
            "bio" to bio,
            "birthdate" to birthdate
        )

        databaseReference.child(userId).updateChildren(userProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (profileImageUri != null) {
                    uploadProfileImage(userId)
                } else {
                    Toast.makeText(this, "Profile updated.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadProfileImage(userId: String) {
        val imageRef = storageReference.getReference("profile_images/$userId.jpg")
        profileImageUri?.let { uri ->
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    databaseReference.child(userId).child("profileImage").setValue(downloadUri.toString())
                        .addOnCompleteListener {
                            Toast.makeText(this, "Profile updated with image.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show()
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
