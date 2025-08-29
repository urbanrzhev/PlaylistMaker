package com.example.playlistmaker.common.util

import android.content.Context
import com.example.playlistmaker.R

class OrthographyCount(val context:Context){
    fun orthographyCount(count:Int):String{
        return when{
            count == 0 -> context.getString(R.string.not_tracks_recycler)
            count == 1 -> "$count ${context.getString(R.string.track_recycler)}"
            count % 10 == 2 || count % 10 == 3 || count % 10 == 4 -> "$count ${context.getString(R.string.tracka_recycler)}"
            else -> "$count ${context.getString(R.string.tracks_recycler)}"
        }
    }
}