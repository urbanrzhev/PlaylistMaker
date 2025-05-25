package com.example.playlistmaker.player.ui.activity

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.util.MyDisplayMetrics

class ShowActiveTrack(
    private val context: Context,
    private val binding: ActivityAudioPlayerBinding,
    private val model: Track
) {
    fun show() {
        visibleView(
            binding.textLengthTrackDescription1,
            model.trackTimeNormal,
            binding.lengthTrack
        )
        visibleView(
            binding.textAlbumTrackDescription2,
            model.collectionName,
            binding.nameCollection
        )
        visibleView(
            binding.textAlbumTrackDescription4,
            model.primaryGenreName,
            binding.primaryGenreName
        )
        visibleView(
            binding.textAlbumTrackDescription5,
            model.country,
            binding.country
        )
        visibleView(
            binding.textLengthTrackDescription3,
            getYear(model.releaseDate),
            binding.year
        )
        binding.textBig.text = model.trackName
        binding.textArtistName.text = model.artistName
        binding.lengthTrack.text = model.trackTimeNormal
        try {
            binding.textProgress.text = binding.lengthTrack.text
            Glide.with(context)
                .load(getCoverArtwork(model.artworkUrl100))
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(2f, context)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search)
                .into(binding.imageCover)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.crash_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun visibleView(view: View, modelString: String?, view2: TextView) {
        if (modelString.isNullOrEmpty()) {
            view.isVisible = false
            view2.isVisible = false
        } else {
            view2.text = modelString
        }
    }

    private fun getYear(date: String): String? {
        return try {
            date.split("-")[0]
        } catch (e: Exception) {
            null
        }
    }
}