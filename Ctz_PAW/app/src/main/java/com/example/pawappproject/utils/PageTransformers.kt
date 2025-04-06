package com.example.pawappproject.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ExtremeZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val scaleFactor = 0.75f.coerceAtLeast(1 - abs(position))
        view.scaleX = scaleFactor
        view.scaleY = scaleFactor
        view.alpha = if (position < -1 || position > 1) 0f else 1f
    }
}

