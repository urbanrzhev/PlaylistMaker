package com.example.playlistmaker.player.ui.adapter_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.databinding.ViewListForPlayerBottomBehaviorBinding

class PlayerAdapter(
    private val callback:BottomListener
): RecyclerView.Adapter<PlayerViewHolder>() {
    private var playlists:List<Playlist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ViewListForPlayerBottomBehaviorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            callback.click(playlists[position])
        }
    }

    fun updateList(newList:List<Playlist>){
        val oldList = playlists
        val difResult = DiffUtil.calculateDiff(object : DiffUtil.Callback(){
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].count == newList[newItemPosition].count
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })
        playlists = newList.toList()
        difResult.dispatchUpdatesTo(this)
    }

    fun interface BottomListener{
        fun click(playlist:Playlist)
    }
}