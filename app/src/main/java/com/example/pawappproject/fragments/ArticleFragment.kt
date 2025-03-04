package com.example.pawappproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.pawappproject.R
import com.example.pawappproject.adapters.ArticleAdapter
import com.example.pawappproject.models.Article
import com.example.pawappproject.utils.ExtremeZoomOutPageTransformer
import com.google.firebase.database.*

class ArticleFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        viewPager = view.findViewById(R.id.viewPager)

        database = FirebaseDatabase.getInstance().getReference("articles")
        articleList = arrayListOf()
        articleAdapter = ArticleAdapter(requireContext(), articleList)

        viewPager.adapter = articleAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 3

        // ✅ Log when ViewPager2 is attached
        Log.d("ViewPagerDebug", "ViewPager2 Adapter is Set")

        // ✅ Step 4: Log Touch Events to Detect Swipes
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> Log.d("ViewPagerDebug", "Touch detected: ACTION_DOWN")
                MotionEvent.ACTION_MOVE -> Log.d("ViewPagerDebug", "Touch detected: ACTION_MOVE")
                MotionEvent.ACTION_UP -> Log.d("ViewPagerDebug", "Touch detected: ACTION_UP")
            }
            false // Let ViewPager2 handle the touch event
        }

        // ✅ Step 5: Ensure ViewPager2 Registers Page Changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPagerDebug", "Page Changed: $position") // ✅ Log page change
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.d("ViewPagerDebug", "Scroll State: $state") // ✅ Log scroll state
            }
        })

        // ✅ Apply PageTransformer AFTER ViewPager2 is initialized
        viewPager.post {
            viewPager.setPageTransformer(ExtremeZoomOutPageTransformer())
        }

        fetchArticlesFromFirebase()
        return view
    }

    private fun fetchArticlesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleList.clear()

                for (articleSnapshot in dataSnapshot.children) {
                    val article = articleSnapshot.getValue(Article::class.java)
                    if (article != null) {
                        articleList.add(article)
                        Log.d("ViewPagerDebug", "Loaded Article: ${article.title}") // ✅ Log each article
                    }
                }

                articleAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ViewPagerDebug", "Failed to load articles: ${databaseError.message}")
            }
        })
    }
}
