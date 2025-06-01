package com.example.playlistmaker.search.ui.adapters_holders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TracksHistoryAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    private var tracks: MutableList<Track> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount() = tracks.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(tracks[position])
        }
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