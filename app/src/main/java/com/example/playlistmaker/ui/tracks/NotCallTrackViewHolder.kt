package com.example.playlistmaker.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.ITunesService
import com.example.playlistmaker.R

class NotCallViewHolder(parent: ViewGroup, text: String) : ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.load_error_for_search, parent, false)
) {
    init {
        val updateSearch = itemView.findViewById<Button>(R.id.updateSearch)
        val textNotFound = itemView.findViewById<TextView>(R.id.textNotFound)
        textNotFound.text = text
        updateSearch.setOnClickListener {
            //ITunesService.load()
        }
    }
}