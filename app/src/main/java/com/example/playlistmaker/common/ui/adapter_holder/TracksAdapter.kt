package com.example.playlistmaker.common.ui.adapter_holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TracksAdapter(
    private val callback: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            callback.onClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTrackList(list: List<Track>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }
}