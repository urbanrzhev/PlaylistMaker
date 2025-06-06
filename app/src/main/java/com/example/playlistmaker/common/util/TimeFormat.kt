package com.example.playlistmaker.common.util

import java.text.SimpleDateFormat
import java.util.IllegalFormatException

class TimeFormat(private val timeMillis: String,private val simpleDateFormat: SimpleDateFormat) {
    constructor(timeMillisLong: Long,  simpleDateFormat: SimpleDateFormat) : this(timeMillisLong.toString(),simpleDateFormat)
    constructor(timeMillisInt: Int,  simpleDateFormat: SimpleDateFormat) : this(timeMillisInt.toString(),simpleDateFormat)

    fun getTimeMM_SS(): String {
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