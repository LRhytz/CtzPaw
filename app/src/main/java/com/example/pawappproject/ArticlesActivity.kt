package com.example.pawappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ArticlesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        val headingArticles: TextView = findViewById(R.id.articlesHeading)
        val mainArticles: TextView = findViewById(R.id.articles)
        val imageArticle: ImageView = findViewById(R.id.image_heading)

        // Fetch data from the intent
        val bundle: Bundle? = intent.extras
        val heading = bundle?.getString("title")
        val imageUrl = bundle?.getString("ImageId") // Should be a URL
        val articles = bundle?.getString("articles")

        // Set the text views
        headingArticles.text = heading
        mainArticles.text = articles

        // Load the image using Glide
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl) // Load from URL
                .placeholder(R.drawable.placeholder_image) // Optional placeholder
                .into(imageArticle) // Set the image into ImageView
        }
    }
}
