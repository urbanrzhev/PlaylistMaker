package com.example.playlistmaker

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.TracksAdapter

class OptionAdaptersAndProgressBar(private val recyclerView: RecyclerView, private val progressBar:View) {
    private fun progressBarVisibleFalse(){
        progressBar.isVisible = false
    }
    fun progressBarVisibleTrue(){
        progressBar.isVisible = true
    }
    fun setAdapterSuccefull(responseBody: List<Track>) {
        progressBarVisibleFalse()
        recyclerView.adapter = TracksAdapter(
            responseBody,
            sign = TracksAdapter.SEARCH_COMPLETED,
            callbackForHistory = {}
            //searchHistory = searchHist
            //clickListener = {}
        )
    }

    fun setAdaptersErrors(data: Int, text: Boolean = false) {
        progressBarVisibleFalse()
        if (text)
            recyclerView.adapter = TracksAdapter(
                sign = data,
                text = recyclerView.context.getString(R.string.load_error_two_for_search),
                callbackForHistory = {}
              //  clickListener = {}
            )
        else
            recyclerView.adapter = TracksAdapter(
                sign = data,
                callbackForHistory = {}
                //clickListener = {}
            )//
//
    }
    fun setAdaptersErrorConnect(){
        progressBarVisibleFalse()
        recyclerView.adapter = TracksAdapter(
            sign = TracksAdapter.SEARCH_NOT_CALL,
            text = recyclerView.context.getString(R.string.load_error_one_for_search),
            callbackForHistory = {}
            //clickListener = {}
        )
    }
}