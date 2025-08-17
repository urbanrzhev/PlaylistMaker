package com.example.playlistmaker.common.util

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.playlistmaker.common.domain.models.Track

object TrackBundleUtil {
    fun createBundle(key: String, track: Track): Bundle {
        return with(track) {
            bundleOf(
                key to
                        bundleOf(
                            "trackName" to trackName,
                            "artistName" to artistName,
                            "trackTimeNormal" to trackTimeNormal,
                            "artworkUrl100" to artworkUrl100,
                            "previewUrl" to previewUrl,
                            "collectionName" to collectionName,
                            "releaseDate" to releaseDate,
                            "primaryGenreName" to primaryGenreName,
                            "country" to country,
                            "trackId" to trackId,
                            "isFavorite" to isFavorite
                        )
            )
        }
    }

    fun rewriteBundle(trackBundle: Bundle?): Track? {
        if (trackBundle != null) {
            with(trackBundle) {
                return Track(
                    trackName = getString("trackName") ?: "",
                    artistName = getString("artistName") ?: "",
                    trackTimeNormal = getString("trackTimeNormal") ?: "",
                    artworkUrl100 = getString("artworkUrl100") ?: "",
                    previewUrl = getString("previewUrl") ?: "",
                    collectionName = getString("collectionName") ?: "",
                    releaseDate = getString("releaseDate") ?: "",
                    primaryGenreName = getString("primaryGenreName") ?: "",
                    country = getString("country") ?: "",
                    trackId = getInt("trackId"),
                    isFavorite = getBoolean("isFavorite")
                )
            }
        } else
            return null
    }
}
