package com.example.pawappproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendedPetsAdapter(
    private val context: Context,
    private var recommendedPets: List<AdoptDescription>,
    private val onItemClick: (AdoptDescription) -> Unit
) : RecyclerView.Adapter<RecommendedPetsAdapter.RecommendedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_pet_item, parent, false)
        return RecommendedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val pet = recommendedPets[position]
        holder.bind(pet)
        holder.itemView.setOnClickListener { onItemClick(pet) }
    }

    override fun getItemCount(): Int = recommendedPets.size

    fun updateList(newList: List<AdoptDescription>) {
        recommendedPets = newList
        notifyDataSetChanged()
    }

    inner class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val petImageView: ImageView = itemView.findViewById(R.id.recommendedPetImage)
        private val petNameTextView: TextView = itemView.findViewById(R.id.recommendedPetName)
        fun bind(pet: AdoptDescription) {
            Glide.with(itemView.context).load(pet.imageUrl).into(petImageView)
            petNameTextView.text = pet.name
        }
    }
}
