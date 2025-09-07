package com.example.playlistmaker.info_playlist.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.common.data.sharing.ExternalNavigator
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.info_playlist.domain.api.InfoSharingUseCase

class InfoSharingUseCaseImpl(
    private val extNavigator: ExternalNavigator,
    private val appContext: Context
) : InfoSharingUseCase {

    override fun execute(playlist: Playlist, tracks: List<Track>) {
        val message = createString(playlist, tracks)
        extNavigator.shareLink(message)
    }

    private fun createString(playlist: Playlist, tracks: List<Track>): String {
        var string = "${appContext.getString(R.string.playlist)} \n" +
                "${playlist.name} \n" +
                if (playlist.description.isNotEmpty()) {
                    "${playlist.description} \n"
                } else ""
        string += appContext.resources.getQuantityString(
            R.plurals.numberForTracks,
            tracks.size,
            tracks.size
        )
        tracks.forEachIndexed { i, v ->
            string += "\n${i + 1}. ${v.artistName} - ${v.trackName} ${v.trackTimeNormal}"
        }
        return string
    }
}