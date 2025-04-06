package com.example.pawappproject

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

object ReportingUtils {
    fun initAutoCompleteTextView(context: Context, autoCompleteTextView: AutoCompleteTextView) {
        val items = arrayOf(
            "Animal Abuse/Neglect",
            "Stray Animal Sighting",
            "Injured Animal",
            "Abandoned Animal",
            "Lost/Found Pet",
            "Animal Rescue Request",
            "Animal Cruelty Suspected",
            "Animal Emergency",
            "Trap-Neuter-Return (TNR) Request",
            "Animal Shelter Support Needed"
        )

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        autoCompleteTextView.setAdapter(adapter)
    }
}
