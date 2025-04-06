package com.example.pawappproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EducationAwarenessCitizenActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articlesList: ArrayList<Article>
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_awareness_citizen)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize list and adapter
        articlesList = ArrayList()
        articlesAdapter = ArticlesAdapter(articlesList)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = articlesAdapter

        // Fetch articles from Firebase
        fetchArticles()
    }

    private fun fetchArticles() {
        database = FirebaseDatabase.getInstance().reference.child("articles")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                articlesList.clear()  // Clear existing data

                // Loop through the Firebase snapshot and fetch article data
                for (dataSnapshot in snapshot.children) {
                    val article = dataSnapshot.getValue(Article::class.java)
                    article?.let { articlesList.add(it) }
                }

                // Notify adapter that data has changed
                articlesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }
}
