package com.example.pawappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ArticlesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        val headingArticles : TextView = findViewById(R.id.articlesHeading)
        val mainArticles : TextView = findViewById(R.id.articles)
        val imageArticle : ImageView = findViewById(R.id.image_heading)

        val bundle : Bundle?= intent.extras
        val heading = bundle!!.getString("heading")
        val imageId = bundle.getInt("ImageId")
        val articles = bundle.getString("articles")

        headingArticles.text = heading
        mainArticles.text = articles
        imageArticle.setImageResource(imageId)
    }
}