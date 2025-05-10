package com.example.playlistmaker.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.tracks.NotCallViewHolder
import com.example.playlistmaker.ui.tracks.NotTrackViewHolder
import com.example.playlistmaker.ui.tracks.TrackViewHolder

class TracksAdapter(
    private val track: List<Track> = listOf(),
    private val callbackForHistory:()->Unit,
    private val sign: Int = 0,
    private val text: String = ""
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val SEARCH_COMPLETED: Int = 0
        const val SEARCH_NOT_TRACK: Int = 1
        const val SEARCH_NOT_CALL: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (sign) {
            SEARCH_COMPLETED -> TrackViewHolder(
                parent,
                history = false,
                callbackRefreshHistory = callbackForHistory
            )

            SEARCH_NOT_TRACK -> NotTrackViewHolder(parent)
            SEARCH_NOT_CALL -> NotCallViewHolder(parent, text)
            else -> throw IllegalStateException(parent.context.getString(R.string.illegal_state_exception))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                holder.bind(track[position])
            }
        }
    }

    override fun getItemCount(): Int {
        if (sign != SEARCH_COMPLETED) return 1
        return track.size
    }
}