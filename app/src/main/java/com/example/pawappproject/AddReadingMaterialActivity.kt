package com.example.pawappproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class AddReadingMaterialActivity : Activity() {

    private lateinit var database: DatabaseReference
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reading_material)

        database = FirebaseDatabase.getInstance().getReference("articles")

        val titleEditText: EditText = findViewById(R.id.editTextTitle)
        val contentEditText: EditText = findViewById(R.id.editTextContent)
        imageView = findViewById(R.id.imageView)
        val chooseImageButton: Button = findViewById(R.id.buttonChooseImage)
        val saveButton: Button = findViewById(R.id.buttonSave)

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val imageUrl = imageUri?.toString() ?: "" // Handle image URL if you have image upload functionality

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val articleId = database.push().key ?: return@setOnClickListener
                val article = Articles(articleId, title, content, imageUrl)
                database.child(articleId).setValue(article).addOnCompleteListener { task ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
