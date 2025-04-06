package com.example.pawappproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class OrgAddArticleActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImageUri: Uri
    private lateinit var progressBar: ProgressBar

    private var isImageSelected = false // To track if an image was selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_add_article)

        val titleEditText: EditText = findViewById(R.id.articleTitleInput)
        val contentEditText: EditText = findViewById(R.id.articleContentInput)
        val categoryEditText: EditText = findViewById(R.id.articleCategoryInput)
        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        val addButton: Button = findViewById(R.id.addArticleButton)
        val previewImageView: ImageView = findViewById(R.id.previewImageView)
        progressBar = findViewById(R.id.progressBar)

        database = FirebaseDatabase.getInstance().getReference("articles")
        storage = FirebaseStorage.getInstance()

        // Handle Image Selection
        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        // Handle Add Article
        addButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val content = contentEditText.text.toString().trim()
            val category = categoryEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isImageSelected) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = ProgressBar.VISIBLE

            // Upload the selected image to Firebase Storage
            uploadImageToFirebase { imageUrl ->
                // Create article object and save it to Firebase Realtime Database
                val articleId = database.push().key ?: return@uploadImageToFirebase
                val article = OrgArticles(
                    title = title,
                    content = content,
                    category = category,
                    titleImg = imageUrl
                )

                database.child(articleId).setValue(article).addOnCompleteListener { task ->
                    progressBar.visibility = ProgressBar.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Article added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add article", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Filter for images only
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                isImageSelected = true

                // Show the selected image in the preview
                val previewImageView: ImageView = findViewById(R.id.previewImageView)
                previewImageView.setImageURI(uri)
            }
        }
    }

    private fun uploadImageToFirebase(onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("articles/${UUID.randomUUID()}.jpg")

        storageRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot ->
            // Get the URL of the uploaded image
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString()) // Return the image URL
            }.addOnFailureListener {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            progressBar.visibility = ProgressBar.GONE
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}