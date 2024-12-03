package com.example.pawappproject

data class Articles(
    val title: String = "",       // Title of the article
    val category: String? = null, // Category of the article (nullable)
    val content: String? = null,  // Content of the article (nullable)
    val titleImg: String? = null  // Image URL for the article (nullable)
)
