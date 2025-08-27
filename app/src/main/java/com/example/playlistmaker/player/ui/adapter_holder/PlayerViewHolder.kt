package com.example.playlistmaker.player.ui.adapter_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.MyDisplayMetrics

class PlayerViewHolder(
    private val parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.view_list_for_player_bottom_behavior, parent, false)
) {
    val name = itemView.findViewById<TextView>(R.id.textName)
    val count = itemView.findViewById<TextView>(R.id.textCount)
    val photo = itemView.findViewById<ImageView>(R.id.imageCover)

    fun bind(model: Playlist) {
        name.text = model.name
        count.text = model.count
        try {
            Glide.with(itemView.context)
                .load(model.photo.toUri())
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(2f, itemView.context)))
                .placeholder(R.drawable.placeholder)
                .into(photo)
        } catch (e: Exception) {
            Toast.makeText(
                itemView.context,
                itemView.context.getString(R.string.crash_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}