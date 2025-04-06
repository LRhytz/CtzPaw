package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class AdoptionListActivity : AppCompatActivity() {

    private lateinit var petListRecyclerView: RecyclerView
    private lateinit var petListAdapter: AdoptDescriptionAdapter
    private val originalPetList = mutableListOf<AdoptDescription>()
    private val displayList = mutableListOf<AdoptDescription>()

    // Default species filter: "all", "dog", or "cat"
    private var speciesFilter: String = "all"

    private lateinit var buttonAll: MaterialButton
    private lateinit var buttonDogs: MaterialButton
    private lateinit var buttonCats: MaterialButton

    // New ImageButton for the back button in top bar
    private lateinit var backButton: ImageButton
    private lateinit var filterButton: ImageButton  // Filter button reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption_list)

        // Enable back button using ImageButton and set the logic
        backButton = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()  // Go back to the previous screen when back button is pressed
        }

        // Set the Filter button onClickListener to open FilterActivity
        filterButton = findViewById(R.id.buttonFilter)
        filterButton.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivity(intent)
        }

        // Enable ActionBar back button for compatibility
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adoptable Pets"

        // Initialize the RecyclerView and Adapter
        petListRecyclerView = findViewById(R.id.petListRecyclerView)
        petListAdapter = AdoptDescriptionAdapter(this, displayList) { selectedPet ->
            val intent = Intent(this, AdoptionDescriptionActivity::class.java).apply {
                putExtra("id", selectedPet.id)
                putExtra("imageUrl", selectedPet.imageUrl)
                putExtra("name", selectedPet.name)
                putExtra("gender", selectedPet.gender)
                putExtra("age", selectedPet.getAgeAsString())
                putExtra("breed", selectedPet.breed)
                putExtra("description", selectedPet.description)
                putExtra("organization", selectedPet.organization)
                putExtra("address", selectedPet.address)
                putExtra("fullDescription", selectedPet.fullDescription)
                putExtra("contactEmail", selectedPet.contactEmail)
                putExtra("contactPhone", selectedPet.getContactPhoneAsString())
                putExtra("contactLocation", selectedPet.contactLocation)
            }
            startActivity(intent)
        }
        petListRecyclerView.layoutManager = LinearLayoutManager(this)
        petListRecyclerView.adapter = petListAdapter

        // Button references for filter selection
        buttonAll = findViewById(R.id.buttonAll)
        buttonDogs = findViewById(R.id.buttonDogs)
        buttonCats = findViewById(R.id.buttonCats)

        // Set click listeners for buttons
        buttonAll.setOnClickListener {
            speciesFilter = "all"
            applyFilterAndSort()
            updateButtonSelection(buttonAll, buttonCats, buttonDogs)
        }

        buttonDogs.setOnClickListener {
            speciesFilter = "dog"
            applyFilterAndSort()
            updateButtonSelection(buttonDogs, buttonAll, buttonCats)
        }

        buttonCats.setOnClickListener {
            speciesFilter = "cat"
            applyFilterAndSort()
            updateButtonSelection(buttonCats, buttonAll, buttonDogs)
        }

        // Fetch all pets from Firebase and update the RecyclerView
        val databaseRef = FirebaseDatabase.getInstance().getReference("adoptions")
        databaseRef.get().addOnSuccessListener { snapshot ->
            originalPetList.clear() // Clear the list before adding new data
            for (petSnapshot in snapshot.children) {
                petSnapshot.getValue(AdoptDescription::class.java)?.let { pet ->
                    originalPetList.add(pet)
                }
            }
            applyFilterAndSort()
        }
    }

    // Handle ActionBar back button (the default Android one)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()  // Go back to the previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Apply filter and sort pets list
    private fun applyFilterAndSort() {
        val filtered = when (speciesFilter.lowercase()) {
            "dog" -> originalPetList.filter { it.species.equals("dog", ignoreCase = true) }
            "cat" -> originalPetList.filter { it.species.equals("cat", ignoreCase = true) }
            else -> originalPetList // Show all pets if "all" is selected
        }

        // Sort alphabetically by pet name
        val sorted = filtered.sortedBy { it.name }
        displayList.clear()
        displayList.addAll(sorted)
        petListAdapter.notifyDataSetChanged()

        // Show a toast with the number of pets displayed
        Toast.makeText(this, "Showing ${displayList.size} pets", Toast.LENGTH_SHORT).show()
    }

    // Update button states (selected/unselected)
    private fun updateButtonSelection(selectedButton: MaterialButton, vararg unselectedButtons: MaterialButton) {
        selectedButton.isSelected = true
        unselectedButtons.forEach { it.isSelected = false }
    }
}
