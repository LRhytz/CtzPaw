package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EducationAwarenessActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Articles>
    private lateinit var filteredList: ArrayList<Articles>
    private lateinit var adapter: ArticleAdapter
    private lateinit var searchView: SearchView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_awareness)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("articles")

        // Set up RecyclerView and SearchView
        newRecyclerView = findViewById(R.id.awarenessRecyclerView)
        searchView = findViewById(R.id.searchView)

        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        // Initialize the lists and adapter
        newArrayList = arrayListOf()
        filteredList = arrayListOf()
        adapter = ArticleAdapter(filteredList)
        newRecyclerView.adapter = adapter

        // Set up the adapter's click listener to open articles
        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedArticle = filteredList[position]
                val intent = Intent(this@EducationAwarenessActivity, ArticlesActivity::class.java).apply {
                    putExtra("title", selectedArticle.title)
                    putExtra("ImageId", selectedArticle.titleImg)
                    putExtra("articles", selectedArticle.content)
                }
                startActivity(intent)
            }
        })

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
                newArrayList.clear()

                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Articles::class.java)
                    if (article != null) {
                        newArrayList.add(article)
                    }
                }

                filteredList.clear()
                filteredList.addAll(newArrayList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EducationAwarenessActivity, "Failed to fetch data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filter(text: String) {
        filteredList.clear()

        if (text.isEmpty()) {
            filteredList.addAll(newArrayList)
        } else {
            val query = text.lowercase()
            for (article in newArrayList) {
                if (article.title.lowercase().contains(query)) {
                    filteredList.add(article)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }
}
