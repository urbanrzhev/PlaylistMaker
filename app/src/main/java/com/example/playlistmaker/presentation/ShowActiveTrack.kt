package com.example.playlistmaker.presentation

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.MyDisplayMetrics

class ShowActiveTrack(private val context:Context, private val view: View, private val model:Track) {
    fun show(){
        val imageCover = view.findViewById<ImageView>(R.id.imageView3)
        val lengthTrack = view.findViewById<TextView>(R.id.textLengthTrackValue1)
        val textTimeOut = view.findViewById<TextView>(R.id.textTimeOutPause)
        val collectionName = view.findViewById<TextView>(R.id.textAlbumTrackValue2)
        visibleView(
            view.findViewById<View>(R.id.textAlbumTrackDescription2),
            model.collectionName,
            collectionName
        )
        val primaryGenreName = view.findViewById<TextView>(R.id.textAlbumTrackValue4)
        visibleView(
            view.findViewById<View>(R.id.textAlbumTrackDescription4),
            model.primaryGenreName,
            primaryGenreName
        )
        val country = view.findViewById<TextView>(R.id.textAlbumTrackValue5)
        visibleView(view.findViewById<View>(R.id.textAlbumTrackDescription5), model.country, country)
        val year = view.findViewById<TextView>(R.id.textLengthTrackValue3)
        visibleView(
            view.findViewById<View>(R.id.textLengthTrackDescription3),
            getYear(model.releaseDate),
            year
        )
        view.findViewById<TextView>(R.id.textBig).text = model.trackName
        view.findViewById<TextView>(R.id.textArtistName).text = model.artistName
        lengthTrack?.text = model.trackTimeNormal
        try {
            textTimeOut?.text = lengthTrack?.text
            Glide.with(context)
                .load(getCoverArtwork(model.artworkUrl100))
                .transform(RoundedCorners(MyDisplayMetrics().dpToPx(2f, context)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search)
                .into(imageCover)
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
        try {
            return date.split("-")[0]
        } catch (e: Exception) {
            return null
        }
    }
}