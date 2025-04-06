package com.example.pawappproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteDialogFragment : DialogFragment() {

    interface DeleteDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    private lateinit var listener: DeleteDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DeleteDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DeleteDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_delete_dialog, null)

        builder.setView(dialogView)

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            listener.onDialogPositiveClick()
            dialog?.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            listener.onDialogNegativeClick()
            dialog?.dismiss()
        }

        return builder.create()
    }
}