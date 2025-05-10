package com.example.playlistmaker.domain

import java.text.SimpleDateFormat
import java.util.IllegalFormatException
import java.util.Locale

class TimeFormat(private val timeMillis: String) {
    fun getTimeMM_SS(): String {
        return try {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis.toInt())
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> "01"
                is IllegalFormatException -> "02"
                else -> "00"
            }
        }
    }
}