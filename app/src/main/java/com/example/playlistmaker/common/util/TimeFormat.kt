package com.example.playlistmaker.common.util

import java.text.SimpleDateFormat
import java.util.IllegalFormatException
import java.util.Locale

class TimeFormat {
    private val simpleDateFormatMM_SS: SimpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val simpleDateFormatMM: SimpleDateFormat = SimpleDateFormat("m" , Locale.getDefault())
    fun getTimeMM_SS(timeMillis: Long) = getTimeMM_SS(timeMillis.toString())
    fun getTimeMM_SS(timeMillis: Int) = getTimeMM_SS(timeMillis.toString())

    fun getTimeMM_SS(timeMillis: String): String {
        return try {
            simpleDateFormatMM_SS.format(timeMillis.toInt())
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> ERROR_01
                is IllegalFormatException -> ERROR_02
                else -> ERROR_00
            }
        }
    }

    fun getTimeMM(timeMillis:Int):String{
        return try{
            simpleDateFormatMM.format(timeMillis)
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> ERROR_01
                is IllegalFormatException -> ERROR_02
                else -> ERROR_00
            }
        }
    }
    companion object{
        private const val ERROR_01 = "error01"
        private const val ERROR_02 = "error02"
        private const val ERROR_00 = "error00"
    }
}