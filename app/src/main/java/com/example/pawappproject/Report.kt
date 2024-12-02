package com.example.pawappproject

data class Report(
    var reportId: String = "",
    var reportType: String = "",
    var reportDescription: String = "",
    val imageUrls: List<String> = listOf(),
    var reportUserEmail: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var videoUrl: String? = null,
) {
    // For no-argument constructor required by Firebase
    constructor() : this("", "", "", listOf(), null, null, null, null)
}
