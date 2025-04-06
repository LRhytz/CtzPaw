package com.example.pawappproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
<<<<<<< HEAD
import com.bumptech.glide.Glide

class AdoptionAdapter(
    private val context: Context,
    private var adoptionList: List<AdoptDescription>
) : RecyclerView.Adapter<AdoptionAdapter.AdoptionViewHolder>() {
=======

class AdoptionAdapter(private val context: Context, private val adoptionList: List<AdoptDescription>) :
    RecyclerView.Adapter<AdoptionAdapter.AdoptionViewHolder>() {
>>>>>>> origin/Archival_Branch

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pet_list_item, parent, false)
        return AdoptionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        val currentAdopt = adoptionList[position]
        holder.bind(currentAdopt)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AdoptionDescriptionActivity::class.java).apply {
<<<<<<< HEAD
                putExtra("imageUrl", currentAdopt.imageUrl)
                putExtra("name", currentAdopt.name)
                putExtra("gender", currentAdopt.gender)
                putExtra("age", currentAdopt.getAgeAsString()) // ✅ Safely convert to String
                putExtra("breed", currentAdopt.breed)
                putExtra("description", currentAdopt.description)
                putExtra("organization", currentAdopt.organization)
                putExtra("address", currentAdopt.address)
                putExtra("fullDescription", currentAdopt.fullDescription)
                putExtra("contactEmail", currentAdopt.contactEmail)
                putExtra("contactPhone", currentAdopt.getContactPhoneAsString()) // ✅ Safely convert
                putExtra("contactLocation", currentAdopt.contactLocation)
            }
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = adoptionList.size

    // Function to update the list when new data is fetched
    fun updateList(newList: List<AdoptDescription>) {
        adoptionList = newList
        notifyDataSetChanged()
=======
                putExtra("image_resource_id", currentAdopt.imageResourceId)
                putExtra("name", currentAdopt.name)
                putExtra("gender_resource_id", currentAdopt.genderResourceId)
                putExtra("gender", currentAdopt.gender)
                putExtra("calendar_resource_id", currentAdopt.calendarResourceID)
                putExtra("age", currentAdopt.age)
                putExtra("paw_resource_id", currentAdopt.pawResourceId)
                putExtra("breed", currentAdopt.breed)
                putExtra("description", currentAdopt.description)
                putExtra("org_resource_id", currentAdopt.orgResourceId)
                putExtra("organization", currentAdopt.organization)
                putExtra("location_resource_id", currentAdopt.locationResourceId)
                putExtra("address", currentAdopt.address)
                putExtra("full_description", currentAdopt.fullDescription)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return adoptionList.size
>>>>>>> origin/Archival_Branch
    }

    inner class AdoptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPet: ImageView = itemView.findViewById(R.id.imageViewPetImage)
        private val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
<<<<<<< HEAD
        private val textViewPetGender: TextView = itemView.findViewById(R.id.textViewGenderChip)
        private val textViewPetAge: TextView = itemView.findViewById(R.id.textViewAgeChip)

        fun bind(adopt: AdoptDescription) {
            Glide.with(itemView.context).load(adopt.imageUrl).into(imageViewPet)
            textViewPetName.text = adopt.name
            textViewPetGender.text = adopt.gender
            textViewPetAge.text = adopt.age.toString() // ✅ Convert age safely to String
        }

=======
        private val textViewPetGender: TextView = itemView.findViewById(R.id.PetGenderText)
        private val textViewPetAge: TextView = itemView.findViewById(R.id.petAge)
        private val textViewPetDescription: TextView = itemView.findViewById(R.id.textViewPetDesc)

        fun bind(adopt: AdoptDescription) {
            imageViewPet.setImageResource(adopt.imageResourceId)
            textViewPetName.text = adopt.name
            textViewPetGender.text = adopt.gender
            textViewPetAge.text = adopt.age
            textViewPetDescription.text = adopt.description
        }
>>>>>>> origin/Archival_Branch
    }
}
