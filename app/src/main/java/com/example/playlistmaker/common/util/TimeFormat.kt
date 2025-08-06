package com.example.playlistmaker.common.util

import java.text.SimpleDateFormat
import java.util.IllegalFormatException

class TimeFormat(private val simpleDateFormat: SimpleDateFormat) {

    fun getTimeMM_SS(timeMillis: Long) = getTimeMM_SS(timeMillis.toString())
    fun getTimeMM_SS(timeMillis: Int) = getTimeMM_SS(timeMillis.toString())

    fun getTimeMM_SS(timeMillis: String): String {
        return try {
            simpleDateFormat.format(timeMillis.toInt())
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> "error01"
                is IllegalFormatException -> "error02"
                else -> "error00"
            }
        }
    }
}