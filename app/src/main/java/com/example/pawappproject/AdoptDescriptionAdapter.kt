package com.example.pawappproject

import android.content.Context
<<<<<<< HEAD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class AdoptDescriptionAdapter(
    private val context: Context,
    private var adoptionList: MutableList<AdoptDescription>,
    private val onItemClick: (AdoptDescription) -> Unit
) : RecyclerView.Adapter<AdoptDescriptionAdapter.AdoptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pet_list_item, parent, false)
        return AdoptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        val pet = adoptionList[position]
        holder.bind(pet)
        holder.itemView.setOnClickListener { onItemClick(pet) }
=======
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdoptDescriptionAdapter(private val context: Context, private val adoptionList: List<AdoptDescription>) :
    RecyclerView.Adapter<AdoptDescriptionAdapter.AdoptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pet_list_item, parent, false)
        return AdoptionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        val currentAdopt = adoptionList[position]
        holder.bind(currentAdopt)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AdoptionDescriptionActivity::class.java).apply {
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
>>>>>>> origin/Archival_Branch
    }

    override fun getItemCount(): Int = adoptionList.size

<<<<<<< HEAD
    fun updateList(newList: MutableList<AdoptDescription>) {
        adoptionList = newList
        reorderList() // Reorder the list to show favorited pets first
        notifyDataSetChanged()
    }

    fun reorderList() {
        adoptionList.sortByDescending { it.isFavorited } // Sort favorited pets to the top
        notifyDataSetChanged() // Notify that the data has changed
    }

    inner class AdoptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImageView: ImageView = itemView.findViewById(R.id.imageViewPetImage)
        private val petNameTextView: TextView = itemView.findViewById(R.id.textViewPetName)
        private val textViewBreed: TextView = itemView.findViewById(R.id.textViewBreed)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val heartButton: ImageButton = itemView.findViewById(R.id.heartButton)

        fun bind(pet: AdoptDescription) {
            Glide.with(itemView.context).load(pet.imageUrl).into(petImageView)
            petNameTextView.text = pet.name
            textViewBreed.text = pet.breed
            textViewDescription.text = pet.description // Set the description correctly

            // Set the heart icon based on favorite status.
            val heartIconRes = if (pet.isFavorited) {
                R.drawable.ic_heart_filled
            } else {
                R.drawable.ic_heart_outline
            }
            heartButton.setImageResource(heartIconRes)

            // Handle heart button click and update Firebase
            heartButton.setOnClickListener {
                pet.isFavorited = !pet.isFavorited // Toggle the favorite status

                // Update heart icon
                val newIconRes = if (pet.isFavorited) {
                    R.drawable.ic_heart_filled
                } else {
                    R.drawable.ic_heart_outline
                }
                heartButton.setImageResource(newIconRes)

                // Update the heart status in Firebase
                val databaseRef = FirebaseDatabase.getInstance().getReference("adoptions").child(pet.id)
                databaseRef.child("isFavorited").setValue(pet.isFavorited)

                // Reorder the list to prioritize favorited pets
                reorderList()
                notifyItemChanged(adapterPosition) // Refresh the item view
            }
=======
    inner class AdoptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewPet: ImageView = itemView.findViewById(R.id.imageViewPetImage)
        private val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
        private val textViewPetGender: TextView = itemView.findViewById(R.id.PetGenderText)
        private val textViewPetAge: TextView = itemView.findViewById(R.id.petAge)
        private val textViewPetDescription: TextView = itemView.findViewById(R.id.textViewPetDesc)

        fun bind(adopt: AdoptDescription) {
            imageViewPet.setImageResource(adopt.imageResourceId)
            textViewPetName.text = adopt.name
            textViewPetGender.text = adopt.gender
            textViewPetAge.text = adopt.age
            textViewPetDescription.text = adopt.description
>>>>>>> origin/Archival_Branch
        }
    }
}
