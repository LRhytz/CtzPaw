package com.example.pawappproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import androidx.appcompat.widget.SearchView


class OrgEducAndAwarenessActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleList: ArrayList<OrgArticles>
    private lateinit var articleKeys: HashMap<OrgArticles, String> // Map articles to their Firebase keys
    private lateinit var filteredArticleList: ArrayList<OrgArticles>
    private lateinit var adapter: OrgArticleAdapter
    private lateinit var database: DatabaseReference
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_educ_and_awareness)

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("articles")

        // Setup RecyclerView
        recyclerView = findViewById(R.id.orgRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialize article lists and adapter
        articleList = arrayListOf()
        filteredArticleList = arrayListOf()
        articleKeys = hashMapOf()
        adapter = OrgArticleAdapter(filteredArticleList)
        recyclerView.adapter = adapter

        // Fetch articles from Firebase
        fetchArticlesFromFirebase()

        // Floating Action Button for adding a new article
        val addButton: FloatingActionButton = findViewById(R.id.addArticleButton)
        addButton.setOnClickListener {
            val intent = Intent(this, OrgAddArticleActivity::class.java)
            startActivity(intent)
        }

        // Set up SearchView for filtering
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText ?: "")
                return true
            }
        })

        // Set click listeners for article items
        adapter.setOnItemClickListener(object : OrgArticleAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                openArticleDetails(filteredArticleList[position])
            }

            override fun onEditClick(position: Int) {
                val article = filteredArticleList[position]
                val key = articleKeys[article] ?: ""
                if (key.isNotEmpty()) {
                    openEditArticle(article, key)
                } else {
                    Toast.makeText(this@OrgEducAndAwarenessActivity, "Unable to find article key.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun fetchArticlesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleList.clear()
                articleKeys.clear()

                for (snapshot in dataSnapshot.children) {
                    try {
                        val article = snapshot.getValue(OrgArticles::class.java)
                        if (article != null) {
                            articleList.add(article)
                            articleKeys[article] = snapshot.key ?: ""
                        } else {
                            Log.e("FirebaseError", "Null article for key: ${snapshot.key}")
                        }
                    } catch (e: Exception) {
                        Log.e("FirebaseError", "Error parsing article for key: ${snapshot.key}", e)
                    }
                }

                // Update filtered list and refresh the adapter
                filteredArticleList.clear()
                filteredArticleList.addAll(articleList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrgEducAndAwarenessActivity, "Failed to load articles.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filter(query: String) {
        filteredArticleList.clear()
        if (query.isEmpty()) {
            filteredArticleList.addAll(articleList)
        } else {
            val lowerCaseQuery = query.lowercase()
            for (article in articleList) {
                if (article.title.lowercase().contains(lowerCaseQuery)) {
                    filteredArticleList.add(article)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun openArticleDetails(article: OrgArticles) {
        val intent = Intent(this@OrgEducAndAwarenessActivity, OrgArticlesActivity::class.java).apply {
            putExtra("title", article.title)
            putExtra("ImageId", article.titleImg)
            putExtra("content", article.content)
        }
        startActivity(intent)
    }

    private fun openEditArticle(article: OrgArticles, firebaseKey: String) {
        val intent = Intent(this, OrgUpdateArticleActivity::class.java).apply {
            putExtra("firebaseKey", firebaseKey) // Pass the Firebase key
            putExtra("title", article.title)
            putExtra("content", article.content)
            putExtra("category", article.category)
            putExtra("imageUrl", article.titleImg)
        }
        startActivity(intent)
    }
}