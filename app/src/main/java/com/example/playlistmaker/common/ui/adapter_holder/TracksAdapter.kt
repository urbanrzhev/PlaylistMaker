package com.example.playlistmaker.common.ui.adapter_holder

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TracksAdapter(
    private val callback: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks: List<Track> = listOf()

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

    fun updateList(newList:List<Track>){
        val oldList = tracks
        val difResult = DiffUtil.calculateDiff(object :DiffUtil.Callback(){
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId &&
                        oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        tracks = newList.toList()
        difResult.dispatchUpdatesTo(this)
    }

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }
}