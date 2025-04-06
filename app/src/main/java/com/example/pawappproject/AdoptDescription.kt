package com.example.pawappproject

data class AdoptDescription(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val gender: String = "", // Add gender field
    val size: String = "", // Add size field
    val age: Any = "", // Allow any type (String or Long)
    val breed: String = "",
    val species: String = "",
    val description: String = "",
    val organization: String = "",
    val address: String = "",
    val fullDescription: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val contactLocation: String = "", // Add location field here
    val userViews: Long = 0,
    val globalViews: Long = 0,
    var isFavorited: Boolean = false // Defaults to false
) {
    fun getAgeAsString(): String = when(age) {
        is Long -> age.toString()
        is String -> age
        else -> "Unknown"
    }

    fun getContactPhoneAsString(): String = contactPhone.toString()

    // Add this method if you want to filter by location in the list
    fun getLocation(): String = contactLocation
}
