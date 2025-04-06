package com.example.pawappproject

import android.content.Intent
<<<<<<< HEAD
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdoptionDescriptionActivity : AppCompatActivity() {

    private lateinit var recommendedPetsRecyclerView: RecyclerView
    private lateinit var recommendedAdapter: RecommendedPetsAdapter
    private val recommendedPetsList = mutableListOf<AdoptDescription>()

=======
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class AdoptionDescriptionActivity : AppCompatActivity() {
>>>>>>> origin/Archival_Branch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_list_details)

<<<<<<< HEAD
        // Enable ActionBar back button.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pet Details"

        // Bind primary views.
        val imageViewPet = findViewById<ImageView>(R.id.imageViewPetImage)
        val textViewPetName = findViewById<TextView>(R.id.textViewPetName)
        val textViewPetLocation = findViewById<TextView>(R.id.textViewPetLocation)
        val breedDetailsTextView = findViewById<TextView>(R.id.textViewBreedDetails)
        val orgDetailsTextView = findViewById<TextView>(R.id.textViewOrganizationDetails)
        val addressDetailsTextView = findViewById<TextView>(R.id.textViewAddressDetails)
        val genderChipTextView = findViewById<TextView>(R.id.textViewGenderChip)
        val ageChipTextView = findViewById<TextView>(R.id.textViewAgeChip)
        val textViewFullDescriptionDetails = findViewById<TextView>(R.id.textViewFullDescriptionDetails)

        // Recommended pets RecyclerView.
        recommendedPetsRecyclerView = findViewById(R.id.recommendedPetsRecyclerView)
        recommendedAdapter = RecommendedPetsAdapter(this, recommendedPetsList) { recommendedPet ->
            val intent = Intent(this, AdoptionDescriptionActivity::class.java).apply {
                putExtra("id", recommendedPet.id)
                putExtra("imageUrl", recommendedPet.imageUrl)
                putExtra("name", recommendedPet.name)
                putExtra("gender", recommendedPet.gender)
                putExtra("age", recommendedPet.getAgeAsString())
                putExtra("breed", recommendedPet.breed)
                putExtra("description", recommendedPet.description)
                putExtra("organization", recommendedPet.organization)
                putExtra("address", recommendedPet.address)
                putExtra("fullDescription", recommendedPet.fullDescription)
                putExtra("contactEmail", recommendedPet.contactEmail)
                putExtra("contactPhone", recommendedPet.getContactPhoneAsString())
                putExtra("contactLocation", recommendedPet.contactLocation)
            }
            startActivity(intent)
        }
        recommendedPetsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        recommendedPetsRecyclerView.adapter = recommendedAdapter

        // Retrieve pet details from Intent extras.
        val selectedPet = AdoptDescription(
            id = intent.getStringExtra("id") ?: "",
            imageUrl = intent.getStringExtra("imageUrl") ?: "",
            name = intent.getStringExtra("name") ?: "",
            gender = intent.getStringExtra("gender") ?: "",
            age = intent.getStringExtra("age") ?: "",
            breed = intent.getStringExtra("breed") ?: "",
            description = intent.getStringExtra("description") ?: "",
            organization = intent.getStringExtra("organization") ?: "",
            address = intent.getStringExtra("address") ?: "",
            fullDescription = intent.getStringExtra("fullDescription") ?: "",
            contactEmail = intent.getStringExtra("contactEmail") ?: "",
            contactPhone = intent.getStringExtra("contactPhone") ?: "",
            contactLocation = intent.getStringExtra("contactLocation") ?: ""
        )

        // Bind data to views.
        Glide.with(this).load(selectedPet.imageUrl).into(imageViewPet)
        textViewPetName.text = selectedPet.name
        textViewPetLocation.text = selectedPet.address  // Or use a dedicated location field if available
        breedDetailsTextView.text = selectedPet.breed
        orgDetailsTextView.text = selectedPet.organization
        addressDetailsTextView.text = selectedPet.address
        genderChipTextView.text = selectedPet.gender
        ageChipTextView.text = selectedPet.getAgeAsString()
        textViewFullDescriptionDetails.text = selectedPet.fullDescription

        // Contact Us Section.
        val contactEmailText = findViewById<TextView>(R.id.contactEmail)
        val contactPhoneText = findViewById<TextView>(R.id.contactPhone)
        val contactLocationText = findViewById<TextView>(R.id.contactLocation)
        contactEmailText.text = selectedPet.contactEmail.ifEmpty { "Not Available" }
        contactPhoneText.text = selectedPet.contactPhone.ifEmpty { "Not Available" }
        contactLocationText.text = selectedPet.contactLocation.ifEmpty { "Not Available" }

        contactEmailText.setOnClickListener {
            if (selectedPet.contactEmail.isNotEmpty()) {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${selectedPet.contactEmail}"))
                startActivity(Intent.createChooser(emailIntent, "Send Email"))
            }
        }
        contactPhoneText.setOnClickListener {
            if (selectedPet.contactPhone.isNotEmpty()) {
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${selectedPet.contactPhone}"))
                startActivity(phoneIntent)
            }
        }
        contactLocationText.setOnClickListener {
            if (selectedPet.contactLocation.isNotEmpty()) {
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${selectedPet.contactLocation}"))
                startActivity(mapIntent)
            }
        }

        // Fetch recommended pets (excluding the selected pet) via your Firebase logic.
        FirebaseUtils.fetchAdoptionPets(selectedPet) { recommendedPets: List<AdoptDescription> ->
            recommendedPetsList.clear()
            recommendedPetsList.addAll(recommendedPets)
            recommendedAdapter.updateList(recommendedPetsList)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
=======
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
>>>>>>> origin/Archival_Branch
        }
    }
}
