package com.example.playlistmaker.ui

import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TracksAdapter(
    private val track: List<Track> = listOf(),
    private val callbackReloadRequest: ReloadListener = ReloadListener {},
    private val clickListener: TrackClickListener = TrackClickListener { _, _ -> },
    private val sign: Int = 0,
    private val text: String = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val SEARCH_COMPLETED: Int = 0
        const val SEARCH_NOT_TRACK: Int = 1
        const val SEARCH_NOT_CALL: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (sign) {
            SEARCH_COMPLETED -> TrackViewHolder(parent)
            SEARCH_NOT_TRACK -> NotTrackViewHolder(parent)
            SEARCH_NOT_CALL -> NotCallViewHolder(parent, text)
            else -> throw IllegalStateException(parent.context.getString(R.string.illegal_state_exception))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                holder.bind(track[position])
                holder.itemView.setOnClickListener {
                    clickListener.onClick(track[position], false)
                }
            }

            is NotCallViewHolder -> {
                holder.itemView.findViewById<Button>(R.id.updateSearch).setOnClickListener {
                    callbackReloadRequest.reload()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (sign != SEARCH_COMPLETED) return 1
        return track.size
    }

    fun interface TrackClickListener {
        fun onClick(track: Track, history: Boolean)
    }

    fun interface ReloadListener {
        fun reload()
    }
}