package com.example.pawappproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdoptionAdapter(
    private val context: Context,
    private var adoptionList: List<AdoptDescription>
) : RecyclerView.Adapter<AdoptionAdapter.AdoptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pet_list_item, parent, false)
        return AdoptionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        val currentAdopt = adoptionList[position]
        holder.bind(currentAdopt)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AdoptionDescriptionActivity::class.java).apply {
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
    }

    inner class AdoptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPet: ImageView = itemView.findViewById(R.id.imageViewPetImage)
        private val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
        private val textViewPetGender: TextView = itemView.findViewById(R.id.textViewGenderChip)
        private val textViewPetAge: TextView = itemView.findViewById(R.id.textViewAgeChip)

        fun bind(adopt: AdoptDescription) {
            Glide.with(itemView.context).load(adopt.imageUrl).into(imageViewPet)
            textViewPetName.text = adopt.name
            textViewPetGender.text = adopt.gender
            textViewPetAge.text = adopt.age.toString() // ✅ Convert age safely to String
        }

    }
}
