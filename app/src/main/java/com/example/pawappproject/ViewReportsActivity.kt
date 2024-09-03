package com.example.pawappproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawappproject.models.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ViewReportsActivity : AppCompatActivity(), ViewReportsAdapter.OnDeleteButtonClickListener,
    DeleteDialogFragment.DeleteDialogListener {

    private lateinit var database: DatabaseReference
    private lateinit var reportsArrayList: ArrayList<Report>
    private lateinit var reportsRecyclerView: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var adapter: ViewReportsAdapter

    private var selectedReportId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reports)

        // To initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // To initialize the RecyclerView
        reportsRecyclerView = findViewById(R.id.reportsRecyclerView)
        reportsRecyclerView.layoutManager = LinearLayoutManager(this)
        reportsRecyclerView.setHasFixedSize(true)

        // To initialize the list and adapter
        reportsArrayList = arrayListOf()
        adapter = ViewReportsAdapter(reportsArrayList, this)
        reportsRecyclerView.adapter = adapter

        // To initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("reports")

        // To Get data from Firebase
        getReportData()
    }

    private fun getReportData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reportsArrayList.clear()
                    for (reportSnapshot in snapshot.children) {
                        val report = reportSnapshot.getValue(Report::class.java)
                        if (report != null && report.reportUserEmail == currentUser.email) {
                            reportsArrayList.add(report)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewReportsActivity, "Failed to load reports", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteButtonClick(reportId: String) {
        selectedReportId = reportId
        val dialog = DeleteDialogFragment()
        dialog.show(supportFragmentManager, "DeleteDialogFragment")
    }

    override fun onDialogPositiveClick() {
        database.child(selectedReportId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Report deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete report", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDialogNegativeClick() {
        // Used to cancel deletion
    }
}
