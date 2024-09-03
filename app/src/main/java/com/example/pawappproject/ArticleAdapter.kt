package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class ArticleAdapter (private val articleList: ArrayList<Articles>) : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_awareness,
            parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {

        return articleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = articleList[position]
        holder.TitleImg.setImageResource(currentItem.TitleImg)
        holder.tvHeading.text = currentItem.heading
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val TitleImg : ShapeableImageView = itemView.findViewById(R.id.TitleImg)
        val tvHeading : TextView = itemView.findViewById(R.id.tvHeading)

        init {

            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)
            }
        }

    }

}