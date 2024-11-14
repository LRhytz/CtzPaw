package com.example.pawappproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawappproject.fragments.ReportDetailsFragment
import com.example.pawappproject.models.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.Locale

class ViewReportsFragment : Fragment(), ViewReportsAdapter.OnDeleteButtonClickListener,
    ViewReportsAdapter.OnItemClickListener, DeleteDialogFragment.DeleteDialogListener {

    private lateinit var database: DatabaseReference
    private lateinit var reportsArrayList: ArrayList<Report>
    private lateinit var filteredReportsList: ArrayList<Report>
    private lateinit var reportsRecyclerView: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var adapter: ViewReportsAdapter

    private var selectedReportId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_reports, container, false)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // Initialize RecyclerView
        reportsRecyclerView = view.findViewById(R.id.reportsRecyclerView)
        reportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reportsRecyclerView.setHasFixedSize(true)

        // Initialize Lists and Adapter
        reportsArrayList = arrayListOf()
        filteredReportsList = arrayListOf()
        adapter = ViewReportsAdapter(filteredReportsList, this, this)
        reportsRecyclerView.adapter = adapter

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("reports")

        // Load the report data
        getReportData()

        // Setup SearchView for filtering reports
        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterReports(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterReports(newText)
                return true
            }
        })

        return view
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
                    filterReports(null) // Refresh the filtered list with all data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load reports", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterReports(query: String?) {
        filteredReportsList.clear()
        if (query.isNullOrEmpty()) {
            filteredReportsList.addAll(reportsArrayList)
        } else {
            val queryLowerCase = query.toLowerCase(Locale.getDefault())
            for (report in reportsArrayList) {
                if (report.reportId?.toLowerCase(Locale.getDefault())?.contains(queryLowerCase) == true ||
                    report.reportDescription?.toLowerCase(Locale.getDefault())?.contains(queryLowerCase) == true) {
                    filteredReportsList.add(report)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDeleteButtonClick(reportId: String) {
        selectedReportId = reportId
        val dialog = DeleteDialogFragment()
        dialog.show(parentFragmentManager, "DeleteDialogFragment")
    }

    override fun onItemClick(reportId: String) {
        val fragment = ReportDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("reportId", reportId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_wrapper, fragment) // Use fl_wrapper as the container ID
            .addToBackStack(null)
            .commit()
    }

    override fun onDialogPositiveClick() {
        database.child(selectedReportId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Report deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to delete report", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDialogNegativeClick() {
        // Used to cancel deletion
    }
}
