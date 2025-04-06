package com.example.pawappproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrgUpdateArticleActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_update_article)

        val titleEditText: EditText = findViewById(R.id.updateArticleTitleInput)
        val contentEditText: EditText = findViewById(R.id.updateArticleContentInput)
        val categoryEditText: EditText = findViewById(R.id.updateArticleCategoryInput)
        val imageUrlEditText: EditText = findViewById(R.id.updateArticleImageUrlInput)
        val updateButton: Button = findViewById(R.id.updateArticleButton)

        databaseReference = FirebaseDatabase.getInstance().getReference("articles")

        firebaseKey = intent.getStringExtra("firebaseKey") ?: ""
        if (firebaseKey.isEmpty()) {
            Toast.makeText(this, "Invalid article key.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleEditText.setText(intent.getStringExtra("title") ?: "")
        contentEditText.setText(intent.getStringExtra("content") ?: "")
        categoryEditText.setText(intent.getStringExtra("category") ?: "")
        imageUrlEditText.setText(intent.getStringExtra("imageUrl") ?: "")

        updateButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString().trim()
            val updatedContent = contentEditText.text.toString().trim()
            val updatedCategory = categoryEditText.text.toString().trim()
            val updatedImageUrl = imageUrlEditText.text.toString().trim()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty() || updatedCategory.isEmpty() || updatedImageUrl.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedArticle = OrgArticles(
                title = updatedTitle,
                content = updatedContent,
                category = updatedCategory,
                titleImg = updatedImageUrl
            )

            databaseReference.child(firebaseKey).setValue(updatedArticle)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Article updated successfully.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update article.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}