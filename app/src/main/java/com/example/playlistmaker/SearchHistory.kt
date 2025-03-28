package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPrefs: SharedPreferences
) {
    private val gson = Gson()
    private val musicHistoryList: MutableList<Track> = mutableListOf()
    private val musicHistoryTrackAdapter = MusicHistoryTrackAdapter(musicHistoryList)

    companion object {
        const val MY_HISTORY_PREFERENCES = "my_history_preferences"
        const val KEY_HISTORY_TRACK = "key_history_track_"
    }

    init {
        getHistory(musicHistoryList)
    }

    fun addHistory(track: Track) {
        val trackList = getActualList().filter {
            it.trackId != track.trackId
        } as ArrayList<Track>
        trackList.add(0, track)
        if (trackList.size > 10) trackList.removeAt(10)
        sharedPrefs.edit()
            .putString(KEY_HISTORY_TRACK, gson.toJson(trackList))
            .apply()
        getHistory(musicHistoryList)
    }

    fun getAdapter(): RecyclerView.Adapter<MusicTrackViewHolder> {
        return musicHistoryTrackAdapter
    }

    fun getCount(): Boolean {
        return musicHistoryList.size > 0
    }

    private fun getHistory(mL: MutableList<Track>) {
        mL.clear()
        getActualList().forEachIndexed { i, v ->
            mL.add(i, v)
        }
        musicHistoryTrackAdapter.notifyDataSetChanged()
    }

    private fun getActualList(): List<Track> {
        val gc = sharedPrefs.getString(KEY_HISTORY_TRACK, null) ?: return ArrayList()
        val itemType = object : TypeToken<List<Track>>() {}.type
        val trackListResult = gson.fromJson<List<Track>>(gc, itemType)
        return trackListResult
    }

    fun clearHistory() {
        sharedPrefs.edit().clear().apply()
        sharedPrefs.edit().remove(KEY_HISTORY_TRACK).apply()
    }
}