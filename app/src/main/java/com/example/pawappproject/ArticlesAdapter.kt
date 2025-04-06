package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.widget.TextView
import android.widget.ImageView

class ArticlesAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount() = articles.size

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        private val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)

        fun bind(article: Article) {
            titleTextView.text = article.title
            contentTextView.text = article.content

            // Check if titleImg is not empty or null
            if (article.titleImg.isNotEmpty()) {
                Picasso.get().load(article.titleImg).into(articleImageView)
            } else {
                // Optional: set a placeholder image if the image URL is empty
                articleImageView.setImageResource(R.drawable.placeholder_image) // Replace with your placeholder image
            }
        }
    }
}
