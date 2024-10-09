package com.example.pawappproject

data class Articles(
    val title: String = "",       // Default value for title
    val category: String?  = null,    // Default value for category
    val content: String? = null,     // Default value for content
    val TitleImg: String? = null        // Default value for TitleImg (assuming 0 as default image resource)
)
