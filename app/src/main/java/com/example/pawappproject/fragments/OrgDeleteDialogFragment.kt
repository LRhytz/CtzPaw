package com.example.pawappproject.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pawappproject.R


class OrgDeleteDialogFragment : DialogFragment() {

    interface DeleteDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    private var listener: DeleteDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is DeleteDialogListener) {
            listener = parentFragment
        } else {
            throw ClassCastException("$context must implement DeleteDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_delete_dialog, null)

        builder.setView(dialogView)

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            listener?.onDialogPositiveClick()
            dialog?.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            listener?.onDialogNegativeClick()
            dialog?.dismiss()
        }

        return builder.create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
