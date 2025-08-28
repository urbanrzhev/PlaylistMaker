package com.example.playlistmaker.media_library.ui.adapter_holder

import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.MyDisplayMetrics
import com.example.playlistmaker.databinding.ViewListForPlaylistBinding

class MediaLibraryPlaylistViewHolder(
    binding: ViewListForPlaylistBinding
) : RecyclerView.ViewHolder(
    binding.root
) {
    private val name = binding.name
    private val photo = binding.image
    private val count = binding.count

    fun bind(model: Playlist) {
        name.text = model.name
        count.text = model.count
        try {
            Glide.with(itemView.context)
                .load(model.photo.toUri())
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(8f, itemView.context)))
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