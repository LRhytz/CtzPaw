package com.example.pawappproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewReportsAdapter(
    private val reportsRecyclerView: ArrayList<Report>,
    private val deleteListener: OnDeleteButtonClickListener,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewReportsAdapter.MyViewHolder>() {

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(reportId: String)
    }

    interface OnItemClickListener {
        fun onItemClick(reportId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reports_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = reportsRecyclerView[position]

        holder.reportId.text = currentItem.reportId
        holder.reportType.text = currentItem.reportType
        holder.description.text = currentItem.reportDescription
        holder.reporterEmail.text = currentItem.reportUserEmail

        holder.btnDelete.setOnClickListener {
            deleteListener.onDeleteButtonClick(currentItem.reportId)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem.reportId)
        }
    }

    override fun getItemCount(): Int {
        return reportsRecyclerView.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportId: TextView = itemView.findViewById(R.id.reportIdTextView)
        val reportType: TextView = itemView.findViewById(R.id.reportTypeIdTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val reporterEmail: TextView = itemView.findViewById(R.id.reporterEmailTextView)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
