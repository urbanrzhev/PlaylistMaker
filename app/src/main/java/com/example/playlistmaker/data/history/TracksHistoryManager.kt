package com.example.playlistmaker.data.history

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.api.AdapterRefresh
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TracksHistoryManager(
    context: Context/*,
    val callback: TracksHistoryManager.Change?*/
) {
    private val gson = Gson()
    private var trackHistoryList: MutableList<Track> = mutableListOf()
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(MY_HISTORY_PREFERENCES, MODE_PRIVATE)

    companion object {
        const val MY_HISTORY_PREFERENCES = "my_history_preferences"
        const val KEY_HISTORY_TRACK = "key_history_track_"
    }

    init {
        trackHistoryList = getActualList()
    }

    fun addHistory(track: Track) {
        val trackList = getActualList().filter {
            it.trackId != track.trackId
        } as MutableList<Track>
        trackList.add(0, track)
        if (trackList.size > 10) trackList.removeAt(10)
        sharedPrefs.edit()
            .putString(KEY_HISTORY_TRACK, gson.toJson(trackList))
            .apply()
        refresh(trackHistoryList)
        //callback?.onChange()
    }

    fun getCount(): Boolean {
        return trackHistoryList.size > 0
    }

    private fun refresh(mL: MutableList<Track>) {
        mL.clear()
        getActualList().forEachIndexed { i, v ->
            mL.add(i, v)
        }
    }

    fun getActualList(): MutableList<Track> {
        val gc = sharedPrefs.getString(KEY_HISTORY_TRACK, null) ?: return mutableListOf()
        val itemType = object : TypeToken<List<Track>>() {}.type
        val trackList = gson.fromJson<List<Track>>(gc, itemType)
        trackHistoryList = trackList.toMutableList()
        return trackHistoryList
    }

    fun clearHistory() {
        sharedPrefs.edit().clear().apply()
        sharedPrefs.edit().remove(KEY_HISTORY_TRACK).apply()
    }


/*    fun interface Change {
        fun onChange()
    }*/
}