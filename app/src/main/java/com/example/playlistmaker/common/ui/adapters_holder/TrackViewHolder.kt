package com.example.playlistmaker.common.ui.adapters_holder

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.MyDisplayMetrics
import com.example.playlistmaker.databinding.ViewListForSearchBinding

class TrackViewHolder(
    binding: ViewListForSearchBinding
) : RecyclerView.ViewHolder(
    binding.root
) {
    private val trackName = binding.textView1
    private val artistName = binding.textView2
    private val trackTime = binding.textView3
    private val imageCover = binding.imageViewCover

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeNormal
        try {
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(2f, itemView.context)))
                .placeholder(R.drawable.placeholder)
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