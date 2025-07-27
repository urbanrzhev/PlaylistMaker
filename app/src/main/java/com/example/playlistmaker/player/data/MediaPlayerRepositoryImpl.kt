package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val player: MediaPlayer) : MediaPlayerRepository {
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(
        url: String,
        setOnPreparedListener: () -> Unit,
        setOnCompletionListener: () -> Unit
    ) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = STATE_PREPARED
            setOnPreparedListener.invoke()
        }
        player.setOnCompletionListener {
            playerState = STATE_PREPARED
            setOnCompletionListener.invoke()
        }
    }

    private fun startPlayer() {
        player.start()
        playerState = STATE_PLAYING
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition
    }

    override fun releasePlayer() {
        player.release()
    }

    override fun pausePlayer() {
        if (player.isPlaying)
            player.pause()
        playerState = STATE_PAUSED
    }

    override fun isPlaying() = player.isPlaying

    override fun playbackControl(): Boolean {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                return false
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                return true
            }
        }
        return false
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}
