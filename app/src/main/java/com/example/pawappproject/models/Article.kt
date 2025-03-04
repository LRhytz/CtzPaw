package com.example.pawappproject.models

data class Article(
    var title: String = "",
    var category: String = "",
    var content: String = "",
    var titleImg: String = "" // URL of the image from Firebase Storage
) {
    constructor() : this("", "", "", "") // No-argument constructor for Firebase
}
