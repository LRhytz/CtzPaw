package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class OrgArticleAdapter(private val articleList: List<OrgArticles>) : RecyclerView.Adapter<OrgArticleAdapter.ArticleViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onEditClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ShapeableImageView = itemView.findViewById(R.id.TitleImg)
        val articleTitle: TextView = itemView.findViewById(R.id.tvHeading)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
            editButton.setOnClickListener {
                listener?.onEditClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.org_list_item_awareness, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]

        holder.articleTitle.text = article.title

        Glide.with(holder.itemView.context)
            .load(article.titleImg)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.articleImage)
    }

    override fun getItemCount(): Int = articleList.size
}