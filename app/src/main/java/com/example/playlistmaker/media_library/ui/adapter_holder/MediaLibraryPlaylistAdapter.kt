package com.example.playlistmaker.media_library.ui.adapter_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.databinding.ViewListForPlaylistBinding

class MediaLibraryPlaylistAdapter(
    private val callback: PlaylistListener
) : RecyclerView.Adapter<MediaLibraryPlaylistViewHolder>() {
    private var playlists: List<Playlist> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaLibraryPlaylistViewHolder {
        val binding =
            ViewListForPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaLibraryPlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: MediaLibraryPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            try {
                callback.click(playlists[position])
            } catch (e: Exception) {
                callback.click(playlists[0])
            }
        }
    }

    fun updateList(newList: List<Playlist>) {
        val oldList = playlists
        val difResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].name == newList[newItemPosition].name
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        playlists = newList.toList()
        difResult.dispatchUpdatesTo(this)
    }

    fun interface PlaylistListener {
        fun click(playlist: Playlist)
    }
}