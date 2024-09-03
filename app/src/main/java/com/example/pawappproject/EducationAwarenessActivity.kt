package com.example.pawappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EducationAwarenessActivity : AppCompatActivity() {

    private lateinit var newRecyclerView : RecyclerView
    private lateinit var newArrayList : ArrayList<Articles>
    lateinit var imageId : Array<Int>
    lateinit var heading : Array<String>
    lateinit var Articles : Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_awareness)

        imageId = arrayOf(
            R.drawable.republic,
            R.drawable.saveanimals,
            R.drawable.petcare
        )

        heading = arrayOf(
            "Republic Act No. 10631 ",
            "Save Animals ",
            "Pet Care Tips "
        )

        Articles = arrayOf(

                    getString(R.string.articles_a),
                    getString(R.string.articles_b),
                    getString(R.string.articles_c)
        )

        newRecyclerView = findViewById(R.id.awarenessRecyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Articles>()
        getUserdata()

    }

    private fun getUserdata() {

        for (i in imageId.indices){

            val articles = Articles(imageId[i],heading[i])
            newArrayList.add(articles)
        }

        var adapter = ArticleAdapter(newArrayList)
        newRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                //Toast.makeText(this@EducationAwarenessActivity, "You clicked on Article no. $position", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@EducationAwarenessActivity,ArticlesActivity::class.java)
                intent.putExtra("heading", newArrayList[position].heading)
                intent.putExtra("ImageId", newArrayList[position].TitleImg)
                intent.putExtra("articles", Articles[position])
                startActivity(intent)
            }


        })
    }
}