package com.example.playlistmaker.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.util.TimeFormat
import com.example.playlistmaker.ui.ShowActiveTrack

private const val DELAY = 500L
private const val DELAY_NULL = 0L

class MediaLibraryActivity : AppCompatActivity() {
    private val mediaPlayer: MediaPlayerInteractor = Creator.provedeMediaPlayerInteractor()
    private lateinit var timeText: TextView
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private val trackGet = Creator.provideGetActiveTrackUseCase()
    private val startActivity = Creator.provideSetStartActivityUseCase()
    private lateinit var playButton:View
    private lateinit var playStopRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top + ime.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        playStopRunnable = Runnable{
            timeText.text = TimeFormat(mediaPlayer.currentPosition()).getTimeMM_SS()
            mainThreadHandler.postDelayed(playStopRunnable, DELAY)
        }
        timeText = findViewById<TextView>(R.id.textTimeOutPause)
        playButton = findViewById<Button>(R.id.buttonPause)
        playButton.setOnClickListener {
            mediaPlayer.control(start = {
                playButton.setBackgroundResource(R.drawable.pause_play)
                timeProgress(true)
            }, pause = {
                playButton.setBackgroundResource(R.drawable.play_play)
                timeProgress(false)
            })
        }
        val vectorBack = findViewById<View>(R.id.vectorBack)
        vectorBack.setOnClickListener {
            onBackPressed()
        }
        setStartActivity(true)
        getTrackDefault()
    }

    override fun onPause() {
        super.onPause()
        playButton.setBackgroundResource(R.drawable.play_play)
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeProgress(false)
        mediaPlayer.release()
        setStartActivity(false)
    }

    private fun loadTrack(model: Track) {
        ShowActiveTrack(this, findViewById(R.id.main), model)
            .show()
    }

    private fun setStartActivity(key:Boolean) {
        startActivity.setActivity(key)
    }

    private fun getTrackDefault() {
        trackGet.getTrack{
            val track: Track = it
            if (track.previewUrl.isNotEmpty()) {
                loadTrack(track)
                mediaPlayer.prepare(track.previewUrl, consumerPrepared = {
                    playButton.isEnabled = true
                }, consumerCompleted = {
                    playButton.isEnabled = true
                }, consumerEnd = {
                    playButton.setBackgroundResource(R.drawable.play_play)
                    timeProgress(false)
                    timeText.text = TimeFormat(DELAY_NULL).getTimeMM_SS()
                })
            }
        }
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