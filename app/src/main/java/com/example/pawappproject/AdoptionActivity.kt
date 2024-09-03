package com.example.pawappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdoptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption)

        val recyclerViewAdoption = findViewById<RecyclerView>(R.id.petRecyclerView)
        val adoptionList = listOf(
            AdoptDescription(
                R.drawable.dog1, "Courage", R.drawable.male, "Male", R.drawable.calendar,
                "2 Years Old", R.drawable.paw, "Aspin", "I am a good boy",
                R.drawable.urname, "Animal Shelter", R.drawable.locationpin, "123 Main St", "This is Courage, a sweet and forgiving dog who has overcome a traumatic past. Found injured on the streets, he has been nursed back to health and is eager for a loving home. He enjoys cuddles, walks, and playtime, and would thrive in a family with children or other dogs.\n" +
                        "\n" +
                        "Courage is medium-sized, 1 year old, and house-trained. He deserves a forever home where he can finally feel safe and loved.\n" +
                        "\n" +
                        "Interested in giving Courage a second chance? Contact us for more information!"),
            AdoptDescription(
                R.drawable.dog2, "Shanti", R.drawable.male, "Male", R.drawable.calendar,
                "1 Year Old", R.drawable.paw, "Pug", "I am a strong boy",
                R.drawable.urname, "Animal Rescue", R.drawable.locationpin, "456 Elm St", "This is Shanti, a sweet and forgiving dog who has overcome a traumatic past. Found injured on the streets, he has been nursed back to health and is eager for a loving home. He enjoys cuddles, walks, and playtime, and would thrive in a family with children or other dogs.\n" +
                        "\n" +
                        "Courage is medium-sized, 1 year old, and house-trained. He deserves a forever home where he can finally feel safe and loved.\n" +
                        "\n" +
                        "Interested in giving Courage a second chance? Contact us for more information!"),
            AdoptDescription(
                R.drawable.dog3, "Princess", R.drawable.female, "Female", R.drawable.calendar,
                "9 Months Old", R.drawable.paw, "Labrador", "I am a good girl",
                R.drawable.urname, "Pet Haven", R.drawable.locationpin, "789 Oak St", "This is Princess, a sweet and forgiving dog who has overcome a traumatic past. Found injured on the streets, he has been nursed back to health and is eager for a loving home. He enjoys cuddles, walks, and playtime, and would thrive in a family with children or other dogs.\n" +
                        "\n" +
                        "Courage is medium-sized, 1 year old, and house-trained. He deserves a forever home where he can finally feel safe and loved.\n" +
                        "\n" +
                        "Interested in giving Courage a second chance? Contact us for more information!")
        )

        val adapter = AdoptDescriptionAdapter(this, adoptionList)
        recyclerViewAdoption.adapter = adapter
        recyclerViewAdoption.layoutManager = LinearLayoutManager(this)
    }
}
