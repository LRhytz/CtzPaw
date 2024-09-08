package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ArticleAdapter(private val articleList: ArrayList<Articles>) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    private var mListener: onItemClickListener? = null  // Make nullable to avoid uninitialized error

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_awareness, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = articleList[position]

        // Use Glide to load the image from the URL
        Glide.with(holder.itemView.context)
            .load(currentItem.TitleImg)  // Assuming TitleImg is a URL
            .override(600, 400)  // Set fixed size for loading images
            .into(holder.TitleImg)  // Load the image into the ShapeableImageView

        holder.tvHeading.text = currentItem.title
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val TitleImg: ShapeableImageView = itemView.findViewById(R.id.TitleImg)
        val tvHeading: TextView = itemView.findViewById(R.id.tvHeading)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)  // Safely call the listener if it's set
            }
        }
    }
}
