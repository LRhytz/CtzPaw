package com.example.pawappproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.pawappproject.R

class ImageDialogFragment : DialogFragment() {

    companion object {
        private const val IMAGE_URL_KEY = "image_url"

        fun newInstance(imageUrl: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            val args = Bundle()
            args.putString(IMAGE_URL_KEY, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_dialog, container, false)
        val imageView = view.findViewById<ImageView>(R.id.fullScreenImageView)

        val imageUrl = arguments?.getString(IMAGE_URL_KEY)
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.ic_image_load_failed)
            .into(imageView)

        view.setOnClickListener {
            dismiss()
        }

        return view
    }
}
