package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.example.pawappproject.adapters.ArticleAdapter
import com.example.pawappproject.models.Article
import com.google.firebase.database.*

class EducationAwarenessActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var articleList: ArrayList<Article>
    private lateinit var adapter: ArticleAdapter
    private lateinit var searchView: SearchView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_awareness)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("articles")

        // Set up ViewPager2
        viewPager = findViewById(R.id.viewPager)
        searchView = findViewById(R.id.searchView)

        // Initialize the article list
        articleList = arrayListOf()

        // Fetch articles from Firebase
        fetchArticlesFromFirebase()

        // Set up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText ?: "")
                return true
            }
        })
    }

    private fun fetchArticlesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleList.clear()

                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Article::class.java)
                    if (article != null) {
                        articleList.add(article)
                    }
                }

                adapter = ArticleAdapter(this@EducationAwarenessActivity, articleList)
                viewPager.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EducationAwarenessActivity, "Failed to fetch data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = if (text.isEmpty()) {
            articleList
        } else {
            articleList.filter { it.title.lowercase().contains(text.lowercase()) }
        }

        adapter = ArticleAdapter(this@EducationAwarenessActivity, ArrayList(filteredList))
        viewPager.adapter = adapter
    }
}
