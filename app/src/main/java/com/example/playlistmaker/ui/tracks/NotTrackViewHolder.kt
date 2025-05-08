package com.example.playlistmaker.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class NotTrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.nothing_for_search, parent, false)
    ) {
}