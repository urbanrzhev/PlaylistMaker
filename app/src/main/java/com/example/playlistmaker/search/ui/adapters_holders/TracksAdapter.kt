package com.example.playlistmaker.search.ui.adapters_holders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TracksAdapter(
    private val callback: TrackClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                holder.bind(tracks[position])
                holder.itemView.setOnClickListener {
                    callback.onClick(tracks[position])
                }
            }
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