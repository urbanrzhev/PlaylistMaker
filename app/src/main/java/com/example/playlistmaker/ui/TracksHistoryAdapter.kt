package com.example.playlistmaker.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

class TracksHistoryAdapter(
    private var track: List<Track>,
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount() = track.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(track[position], true)
        }
    }

    fun interface TrackClickListener {
        fun onClick(track: Track, history: Boolean)
    }
}