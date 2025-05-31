package com.example.playlistmaker.search.ui.adapters_holders

import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track

class TracksAdapter(
    private val state: Int,
    private val callback: TrackClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    constructor(callback: TrackClickListener) : this(state = SEARCH_COMPLETED, callback = callback)
    constructor(state: Int) : this(state = state, callback = {})

    companion object {
        const val SEARCH_COMPLETED: Int = 0
        const val RESULT_NOT_TRACK: Int = 1
        const val RESULT_NOT_CALL: Int = 2
    }

    var tracks: MutableList<Track> = mutableListOf()
    var text: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (state) {
            SEARCH_COMPLETED -> TrackViewHolder(parent)
            RESULT_NOT_TRACK -> NotTrackViewHolder(parent)
            RESULT_NOT_CALL -> NotCallViewHolder(parent, text)
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