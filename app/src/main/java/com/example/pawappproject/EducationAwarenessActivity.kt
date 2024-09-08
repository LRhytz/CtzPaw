package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.*

class EducationAwarenessActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Articles>
    private lateinit var filteredList: ArrayList<Articles>
    private lateinit var adapter: ArticleAdapter
    private lateinit var searchView: SearchView
    private lateinit var database: DatabaseReference  // Firebase Database Reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_awareness)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("articles")

        newRecyclerView = findViewById(R.id.awarenessRecyclerView)
        searchView = findViewById(R.id.searchView)

        // Set up RecyclerView layout manager
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf()
        filteredList = arrayListOf()

        // Initialize adapter right away with an empty list to avoid uninitialized access
        adapter = ArticleAdapter(filteredList)
        newRecyclerView.adapter = adapter

        // Set the click listener after initializing the adapter
        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EducationAwarenessActivity, ArticlesActivity::class.java)
                intent.putExtra("title", filteredList[position].title)
                intent.putExtra("ImageId", filteredList[position].TitleImg)
                intent.putExtra("articles", filteredList[position].content)
                startActivity(intent)
            }
        })

        fetchArticlesFromFirebase()  // Fetch articles from Firebase

        // Setup search functionality
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
                newArrayList.clear()

                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Articles::class.java)
                    if (article != null) {
                        newArrayList.add(article)
                    }
                }

                // Copy all articles to the filtered list initially
                filteredList.clear()
                filteredList.addAll(newArrayList)

                // Notify adapter of data changes
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EducationAwarenessActivity, "Failed to fetch data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Filter method for search functionality
    private fun filter(text: String) {
        // Ensure that the adapter is initialized before filtering
        if (::adapter.isInitialized) {
            filteredList.clear()

            if (text.isEmpty()) {
                filteredList.addAll(newArrayList)
            } else {
                val query = text.lowercase()

                for (article in newArrayList) {
                    if (article.title?.lowercase()?.contains(query) == true) {
                        filteredList.add(article)
                    }
                }
            }

            // Notify adapter of the data changes
            adapter.notifyDataSetChanged()
        }
    }
}
