package com.example.playlistmaker.presentation.util

import android.content.Context
import android.util.TypedValue

class MyDisplayMetrics {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}