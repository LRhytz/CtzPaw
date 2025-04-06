package com.example.pawappproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ArticlesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        val headingArticles: TextView = findViewById(R.id.articlesHeading)
        val mainArticles: TextView = findViewById(R.id.articles)
        val imageArticle: ImageView = findViewById(R.id.image_heading)

        // Fetch data from the intent
        val heading = intent.getStringExtra("title") ?: "No Title"
        val imageUrl = intent.getStringExtra("ImageId") ?: "" // URL might be empty or null
        val articles = intent.getStringExtra("articles") ?: "No Content Available"

        // Set the text views
        headingArticles.text = heading
        mainArticles.text = articles

        // Load the image using Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                        .into(imageArticle)
    }
}
