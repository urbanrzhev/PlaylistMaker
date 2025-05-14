package com.example.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.TimeFormat
private const val DELAY = 500L
class ControlerPlayer(val view: View, val track: Track) : MediaPlayer() {
    private var playerState = STATE_DEFAULT
    private val myContext = this
    private val timeText: TextView = view.findViewById<TextView>(R.id.textTimeOutPause)
    private val play: View = view.findViewById<Button>(R.id.buttonPause)
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private val playStopRunnable: Runnable = object : Runnable {
        override fun run() {
            timeText.text = TimeFormat(myContext.currentPosition).getTimeMM_SS()
            mainThreadHandler.postDelayed(this, DELAY)
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    init {
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer() {
        this.setDataSource(track.previewUrl)
        this.prepareAsync()
        this.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        this.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        this.start()
        this.setOnCompletionListener {
            pausePlayer()
            timeText.text = TimeFormat(0L).getTimeMM_SS()
        }
        play.setBackgroundResource(R.drawable.pause_play)
        playerState = STATE_PLAYING
        timeProgress(true)
    }

    fun pausePlayer() {
        this.pause()
        play.setBackgroundResource(R.drawable.play_play)
        playerState = STATE_PAUSED
        timeProgress(false)
    }

    private fun timeProgress(value: Boolean) {
        if (value) {
            mainThreadHandler.post(
                playStopRunnable
            )
        } else {
            mainThreadHandler.removeCallbacks(playStopRunnable)
        }
    }
}