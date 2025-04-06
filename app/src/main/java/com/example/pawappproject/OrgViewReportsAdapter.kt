package com.example.pawappproject


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class OrgViewReportsAdapter(
    private val reportsList: ArrayList<OrgReport>,
    private val deleteListener: OnDeleteButtonClickListener,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<OrgViewReportsAdapter.MyViewHolder>() {

    // Interface for delete button click listener
    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(reportId: String)
    }

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(reportId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reports_list_item, parent, false) // Use the organization layout file
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = reportsList[position]

        // Bind data to views
        holder.reportId.text = currentItem.reportId
        holder.reportType.text = currentItem.reportType
        holder.description.text = currentItem.reportDescription
        holder.reporterEmail.text = currentItem.reportUserEmail

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            deleteListener.onDeleteButtonClick(currentItem.reportId ?: "")
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem.reportId ?: "")
        }
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportId: TextView = itemView.findViewById(R.id.reportIdTextView)
        val reportType: TextView = itemView.findViewById(R.id.reportTypeIdTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val reporterEmail: TextView = itemView.findViewById(R.id.reporterEmailTextView)
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete) // Use MaterialButton
    }
}
