package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.MyDisplayMetrics

class TrackViewHolder(
    private val parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_list_for_search, parent, false)
) {
    private val trackName: TextView = itemView.findViewById(R.id.textView1)
    private val artistName: TextView = itemView.findViewById(R.id.textView2)
    private val trackTime: TextView = itemView.findViewById(R.id.textView3)
    private val imageCover: ImageView = itemView.findViewById(R.id.imageViewCover)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeNormal
        try {
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(2f, itemView.context)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search)
                .into(imageCover)
        } catch (e: Exception) {
            Toast.makeText(
                itemView.context,
                itemView.context.getString(R.string.crash_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}