package com.example.pawappproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class FilterActivity : AppCompatActivity() {

    private lateinit var radioButtonAll: RadioButton
    private lateinit var radioButtonCat: RadioButton
    private lateinit var radioButtonDog: RadioButton
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var sizeSeekBar: SeekBar
    private lateinit var editTextLocation: EditText
    private lateinit var applyButton: Button
    private lateinit var clearButton: Button

    private var selectedPetType: String = "all"
    private var selectedGender: String = "all"
    private var selectedSize: Int = 1 // Default size medium
    private var location: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        radioButtonAll = findViewById(R.id.radioButtonAll)
        radioButtonCat = findViewById(R.id.radioButtonCat)
        radioButtonDog = findViewById(R.id.radioButtonDog)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        sizeSeekBar = findViewById(R.id.sizeSeekBar)
        editTextLocation = findViewById(R.id.editTextLocation)
        applyButton = findViewById(R.id.applyFilters)
        clearButton = findViewById(R.id.clearFilters)

        // Set default filter values if available from Intent
        // You can pass data back from AdoptionListActivity if needed

        // Apply filters logic
        applyButton.setOnClickListener {
            selectedPetType = when {
                radioButtonCat.isChecked -> "cat"
                radioButtonDog.isChecked -> "dog"
                else -> "all"
            }

            selectedGender = when {
                radioButtonMale.isChecked -> "male"
                radioButtonFemale.isChecked -> "female"
                else -> "all"
            }

            selectedSize = sizeSeekBar.progress
            location = editTextLocation.text.toString()

            // Create the result intent to return to AdoptionListActivity
            val resultIntent = intent.apply {
                putExtra("selectedPetType", selectedPetType)
                putExtra("selectedGender", selectedGender)
                putExtra("selectedSize", selectedSize)
                putExtra("location", location)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Clear filters logic
        clearButton.setOnClickListener {
            radioButtonAll.isChecked = true
            radioButtonMale.isChecked = true
            sizeSeekBar.progress = 1
            editTextLocation.text.clear()
        }
    }
}
