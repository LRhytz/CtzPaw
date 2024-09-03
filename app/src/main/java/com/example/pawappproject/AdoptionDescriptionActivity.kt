package com.example.pawappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class AdoptionDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_list_details)

        val imageViewPet = findViewById<ImageView>(R.id.imageViewPetImage)
        val textViewPetName = findViewById<TextView>(R.id.textViewPetName)
        val textViewPetGender = findViewById<TextView>(R.id.PetGenderText)
        val textViewPetAge = findViewById<TextView>(R.id.petAge)
        val textViewPetBreed = findViewById<TextView>(R.id.textViewBreedDetails)
        val textViewPetOrganization = findViewById<TextView>(R.id.textViewOrganizationDetails)
        val textViewPetAddress = findViewById<TextView>(R.id.textViewAddressDetails)
        val textViewPetFullDescription = findViewById<TextView>(R.id.textViewFullDescriptionDetails)

        val imageResourceId = intent.getIntExtra("image_resource_id", -1)
        val name = intent.getStringExtra("name")
        val genderResourceId = intent.getIntExtra("gender_resource_id", -1)
        val gender = intent.getStringExtra("gender")
        val calendarResourceID = intent.getIntExtra("calendar_resource_id", -1)
        val age = intent.getStringExtra("age")
        val pawResourceId = intent.getIntExtra("paw_resource_id", -1)
        val breed = intent.getStringExtra("breed")
        val description = intent.getStringExtra("description")
        val orgResourceId = intent.getIntExtra("org_resource_id", -1)
        val organization = intent.getStringExtra("organization")
        val locationResourceId = intent.getIntExtra("location_resource_id", -1)
        val address = intent.getStringExtra("address")
        val fullDescription = intent.getStringExtra("full_description")

        if (imageResourceId != -1 && name != null && genderResourceId != -1 && gender != null &&
            calendarResourceID != -1 && age != null && pawResourceId != -1 && breed != null &&
            description != null && orgResourceId != -1 && organization != null &&
            locationResourceId != -1 && address != null && fullDescription != null) {

            imageViewPet.setImageResource(imageResourceId)
            textViewPetName.text = name
            textViewPetGender.text = gender
            textViewPetAge.text = age
            textViewPetBreed.text = breed
            textViewPetOrganization.text = organization
            textViewPetAddress.text = address
            textViewPetFullDescription.text = fullDescription
        }
    }
}
