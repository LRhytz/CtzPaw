package com.example.pawappproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val imageView = findViewById<ImageView>(R.id.detailImage)
        val titleView = findViewById<TextView>(R.id.detailTitle)
        val contentView = findViewById<TextView>(R.id.detailContent)

        // Get Data from Intent
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("fullContent")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set Data
        titleView.text = title
        contentView.text = content

        // Load Image from Firebase Storage URL
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(imageView)
    }
}
