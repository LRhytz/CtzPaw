package com.example.pawappproject

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseUtils {
    fun fetchAdoptionPets(selectedPet: AdoptDescription, callback: (List<AdoptDescription>) -> Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("adoptions")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recommendedList = mutableListOf<AdoptDescription>()
                for (petSnapshot in snapshot.children) {
                    val pet = petSnapshot.getValue(AdoptDescription::class.java)
                    if (pet != null && pet.id != selectedPet.id) {
                        // Recommend if same breed, gender, or organization.
                        if (pet.breed.equals(selectedPet.breed, ignoreCase = true)
                            || pet.gender.equals(selectedPet.gender, ignoreCase = true)
                            || pet.organization.equals(selectedPet.organization, ignoreCase = true)) {
                            recommendedList.add(pet)
                        }
                    }
                }
                callback(recommendedList)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}
