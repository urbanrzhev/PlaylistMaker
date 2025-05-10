package com.example.playlistmaker.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.api.AdapterRefresh
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.TrackViewHolder

class TracksHistoryAdapter(
    private val track: List<Track>/*,
    private val clickListener: TrackClickListener*/
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent, history = true)
    }

    override fun getItemCount(): Int {
        return track.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])
     //   holder.itemView.setOnClickListener { clickListener.onTrackClick() }
    }

    fun refresh(){
        this.notifyDataSetChanged()
    }
    /*fun interface TrackClickListener{
        fun onTrackClick()
    }*/
}