package com.example.pawappproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class OrgArticlesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        // Retrieve Intent extras safely
        val title = intent.getStringExtra("title") ?: "No Title"
        val imageUrl = intent.getStringExtra("ImageId")
        val content = intent.getStringExtra("content") ?: "No Content Available"

        val titleTextView: TextView = findViewById(R.id.articleTitle)
        val contentTextView: TextView = findViewById(R.id.articleContent)
        val imageView: ImageView = findViewById(R.id.articleImage)

        // Set text for title and content
        titleTextView.text = title
        contentTextView.text = content

        // Use Glide to load the image if URL is not null
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Use a placeholder image
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)
        }
    }
}