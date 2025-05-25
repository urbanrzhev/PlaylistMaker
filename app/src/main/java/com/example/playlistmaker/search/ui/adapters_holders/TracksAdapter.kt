package com.example.playlistmaker.search.ui.adapters_holders

import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track

class TracksAdapter(
    var state: Int = SEARCH_COMPLETED,
    private val callback: TrackClickListener = TrackClickListener {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val SEARCH_COMPLETED: Int = 0
        const val SEARCH_NOT_TRACK: Int = 1
        const val SEARCH_NOT_CALL: Int = 2
    }

    var tracks: MutableList<Track> = mutableListOf()
    var text: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (state) {
            SEARCH_COMPLETED -> TrackViewHolder(parent)
            SEARCH_NOT_TRACK -> NotTrackViewHolder(parent)
            SEARCH_NOT_CALL -> NotCallViewHolder(parent, text)
            else -> throw IllegalStateException(parent.context.getString(R.string.illegal_state_exception))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                holder.bind(tracks[position])
                holder.itemView.setOnClickListener {
                    callback.onClick(tracks[position])
                }
            }

            is NotCallViewHolder -> {
                holder.itemView.findViewById<Button>(R.id.updateSearch).setOnClickListener {
                    callback.onClick(Track())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (state != SEARCH_COMPLETED)
            return 1
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }
}