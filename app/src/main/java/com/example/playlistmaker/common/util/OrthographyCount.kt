package com.example.playlistmaker.common.util

import android.content.Context
import com.example.playlistmaker.R

class OrthographyCount(val context:Context){
    fun orthographyCountTracks(count:Int):String{
        return when{
            count == 0 -> context.getString(R.string.not_tracks_recycler)
            count == 1 -> "$count ${context.getString(R.string.track_recycler)}"
            count % 10 == 2 || count % 10 == 3 || count % 10 == 4 -> "$count ${context.getString(R.string.tracka_recycler)}"
            else -> "$count ${context.getString(R.string.tracks_recycler)}"
        }
    }
    fun orthographyCountMinutes(count:Int):String{
        return when{
            count == 1 -> "$count ${context.getString(R.string.minuta)}"
            count == 11 || count == 2 || count == 3 || count == 4 -> "$count ${context.getString(R.string.minuts)}"
            count == 12 -> "$count ${context.getString(R.string.minut)}"
            count % 10 == 2 || count % 10 == 3 || count % 10 == 4 -> "$count ${context.getString(R.string.minuts)}"
            count % 10 == 5 || count % 10 == 6 || count % 10 == 7 || count % 10 == 8 || count % 10 == 9 -> "$count ${context.getString(R.string.minut)}"
            count % 10 == 1 -> "$count ${context.getString(R.string.minuta)}"
            else -> "$count ${context.getString(R.string.minut)}"
        }
    }
}