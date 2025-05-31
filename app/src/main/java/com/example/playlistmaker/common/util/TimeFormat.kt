package com.example.playlistmaker.common.util

import java.text.SimpleDateFormat
import java.util.IllegalFormatException
import java.util.Locale

class TimeFormat(private val timeMillis: String) {
    constructor(timeMillisLong: Long) : this(timeMillisLong.toString())
    constructor(timeMillisInt: Int) : this(timeMillisInt.toString())

    fun getTimeMM_SS(): String {
        return try {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis.toInt())
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> "error01"
                is IllegalFormatException -> "error02"
                else -> "error00"
            }
        }
    }
}