package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
=======
import android.widget.ImageButton
>>>>>>> origin/Archival_Branch
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

<<<<<<< HEAD
class ImagesAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.imageView.context).load(imageUrl).into(holder.imageView)
    }

    override fun getItemCount() = imageUrls.size
}

=======
class ImagesAdapter(
    private val imageUrls: MutableList<String>, // UPDATED: Changed to MutableList for updating content
    private val maxImages: Int = 5,
    private val onAddClick: () -> Unit,
    private val onRemoveClick: (Int) -> Unit,  // UPDATED: New parameter for remove click
    private val showCloseButton: Boolean = true  // NEW: Flag to control visibility of the close button
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 1
        private const val VIEW_TYPE_ADD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < imageUrls.size) VIEW_TYPE_IMAGE else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_IMAGE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false) // Ensure your layout file is named item_image.xml
            ImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_image, parent, false) // Ensure your layout file is named item_add_image.xml
            AddViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            val imageUrl = imageUrls[position]
            holder.bind(imageUrl, position)
        } else if (holder is AddViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (imageUrls.size < maxImages) imageUrls.size + 1 else imageUrls.size
    }

    // ViewHolder for actual images
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val removeButton: ImageButton = itemView.findViewById(R.id.removeButton) // Ensure item_image.xml has this view

        fun bind(imageUrl: String, position: Int) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)

            // Conditionally show or hide the remove button based on showCloseButton flag
            if (showCloseButton) {
                removeButton.visibility = View.VISIBLE
                removeButton.setOnClickListener {
                    onRemoveClick(position) // Invoke callback with the adapter position
                }
            } else {
                removeButton.visibility = View.GONE // Hide the close button in view-only mode
            }
        }
    }

    // ViewHolder for the "Add" placeholder
    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addImageButton: ImageView = itemView.findViewById(R.id.addImageButton)

        fun bind() {
            addImageButton.setOnClickListener {
                onAddClick() // Invoke callback for adding a new image
            }
        }
    }

    // UPDATED: Helper method to update the adapter's data
    fun updateImages(newImages: MutableList<String>) {
        imageUrls.clear()
        imageUrls.addAll(newImages)
        notifyDataSetChanged()
    }
}
>>>>>>> origin/Archival_Branch
