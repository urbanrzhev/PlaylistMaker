package com.example.playlistmaker.common.ui.adapter_holder

import android.util.Log
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
            Log.v("my", "Callback  ${tracks[position]}")
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


    fun changeTrack(pairList: List<Pair<Int,Boolean>>){
        //Log.v("my", "adapter BEFORE  changeTrack \n ${tracks.toString()}")
       // Log.v("my", "adapter. tracks. size ${tracks.size}")
  /*      val tempList = mutableListOf<Pair<Int,Boolean>>()
        if(pairList.isNotEmpty()) {
            tracks.forEachIndexed { i, t ->
                pairList.forEach {
                    if(it.first == t.trackId) {
                        Log.v("my", "adapter  it.first == t.trackId \n isFavorite = ${it.second} \n name = ${t.trackName}")
                        tempList.add(Pair(i, it.second))
                    }
                }
            }
            tempList.forEach {
                tracks.set(it.first,tracks[it.first].copy(isFavorite = it.second))

                   notifyItemChanged(it.first)
                Log.v("my", "adapter NotifyItemChanged  ")
            }
            Log.v("my", "adapter AFTER  changeTrack \n ${tracks.toString()}")
        }*/
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

    /*fun setTrackList(list: List<Track>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }*/

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }
}