package com.example.pawappproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdoptionActivity : AppCompatActivity() {

    private lateinit var recommendedPetsRecyclerView: RecyclerView
    private lateinit var recommendedAdapter: AdoptDescriptionAdapter
    private val recommendedPetsList = mutableListOf<AdoptDescription>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_list_details)

        // Initialize primary pet detail views
        val imageViewPet = findViewById<ImageView>(R.id.imageViewPetImage)
        val textViewPetName = findViewById<TextView>(R.id.textViewPetName)
        val petGenderText = findViewById<TextView>(R.id.textViewGenderChip)
        val petAgeText = findViewById<TextView>(R.id.textViewAgeChip)
        val textViewBreedDetails = findViewById<TextView>(R.id.textViewBreedDetails)
        val textViewOrganizationDetails = findViewById<TextView>(R.id.textViewOrganizationDetails)
        val textViewAddressDetails = findViewById<TextView>(R.id.textViewAddressDetails)
        val textViewFullDescriptionDetails = findViewById<TextView>(R.id.textViewFullDescriptionDetails)
        val adoptionButton = findViewById<Button>(R.id.adoptionButton)

        // Initialize Contact Us views
        val contactEmailText = findViewById<TextView>(R.id.contactEmail)
        val contactPhoneText = findViewById<TextView>(R.id.contactPhone)
        val contactLocationText = findViewById<TextView>(R.id.contactLocation)

        // Setup RecyclerView for recommended pets with onItemClick lambda
        recommendedPetsRecyclerView = findViewById(R.id.recommendedPetsRecyclerView)
        recommendedAdapter = AdoptDescriptionAdapter(this, recommendedPetsList) { recommendedPet ->
            // When a recommended pet is clicked, launch the details screen with its info.
            val intent = Intent(this, AdoptionActivity::class.java).apply {
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

        // Retrieve selected pet details from Intent extras and build selectedPet object
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

        // Set the pet details in the UI
        Glide.with(this).load(selectedPet.imageUrl).into(imageViewPet)
        textViewPetName.text = selectedPet.name
        petGenderText.text = selectedPet.gender
        petAgeText.text = selectedPet.getAgeAsString()
        textViewBreedDetails.text = selectedPet.breed
        textViewOrganizationDetails.text = selectedPet.organization
        textViewAddressDetails.text = selectedPet.address
        textViewFullDescriptionDetails.text = selectedPet.fullDescription

        // Set Contact Us details from pet data
        contactEmailText.text = selectedPet.contactEmail.ifEmpty { "Not Available" }
        contactPhoneText.text = selectedPet.contactPhone.ifEmpty { "Not Available" }
        contactLocationText.text = selectedPet.contactLocation.ifEmpty { "Not Available" }

        // Make contact info clickable
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

        // Set Adoption button action (currently shows a Toast)
        adoptionButton.setOnClickListener {
            Toast.makeText(this, "Adoption button clicked", Toast.LENGTH_SHORT).show()
            // Future: Implement adoption request flow here
        }

        // Fetch recommended pets (excluding the selected pet) based on breed or gender
        FirebaseUtils.fetchAdoptionPets(selectedPet) { recommendedPets: List<AdoptDescription> ->
            recommendedPetsList.clear()
            recommendedPetsList.addAll(recommendedPets)
            recommendedAdapter.notifyDataSetChanged()
        }
    }
}
