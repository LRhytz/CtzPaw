package com.example.pawappproject.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawappproject.ArticleDetailActivity
import com.example.pawappproject.R
import com.example.pawappproject.models.Article

class ArticleAdapter(private val context: Context, private val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.articleImage)
        val title: TextView = itemView.findViewById(R.id.articleTitle)
        val sneakPeek: TextView = itemView.findViewById(R.id.articleSneakPeek)
        val readMore: Button = itemView.findViewById(R.id.readMoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_awareness, parent, false)

        // Ensure ViewPager2 child items fill the whole page
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = layoutParams

        return ArticleViewHolder(view)
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        // Load image from URL using Glide
        Glide.with(context)
            .load(article.titleImg) // URL from Firebase
            .placeholder(R.drawable.placeholder_image) // Default image if loading fails
            .into(holder.image)

        holder.title.text = article.title
        holder.sneakPeek.text = article.content.take(50) + "..." // Show preview

        // Open full article on "Read More" click
        holder.readMore.setOnClickListener {
            val intent = Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("imageUrl", article.titleImg)
                putExtra("fullContent", article.content)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.d("ViewPagerDebug", "Total Articles in Adapter: ${articles.size}") // ✅ Log article count
        return articles.size
    }

}
