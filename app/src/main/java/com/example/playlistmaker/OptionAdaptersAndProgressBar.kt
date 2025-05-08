package com.example.playlistmaker

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ITunesService.searchHist

class OptionAdaptersAndProgressBar(private val recyclerView: RecyclerView, private val progressBar:View) {
    private fun progressBarVisibleFalse(){
        progressBar.isVisible = false
    }
    fun progressBarVisibleTrue(){
        progressBar.isVisible = true
    }
    fun setAdapterSuccefull(responseBody: List<Track>) {
        progressBarVisibleFalse()
        recyclerView.adapter = MusicTrackAdapter(
            responseBody,
            sign = MusicTrackAdapter.SEARCH_COMPLETED,
            searchHistory = searchHist
        )
    }

    fun setAdaptersErrors(data: Int, text: Boolean = false) {
        progressBarVisibleFalse()
        if (text)
            recyclerView.adapter = MusicTrackAdapter(
                sign = data,
                text = recyclerView.context.getString(R.string.load_error_two_for_search)
            )
        else
            recyclerView.adapter = MusicTrackAdapter(
                sign = data
            )

    }
    fun setAdaptersErrorConnect(){
        progressBarVisibleFalse()
        recyclerView.adapter = MusicTrackAdapter(
            sign = MusicTrackAdapter.SEARCH_NOT_CALL,
            text = recyclerView.context.getString(R.string.load_error_one_for_search)
        )
    }
}