package com.example.playlistmaker.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.presentation.ControlerPlayer
import com.example.playlistmaker.presentation.ShowActiveTrack
import com.example.playlistmaker.domain.models.Track

class MediaLibraryActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayerUseCase? = null// ControlerPlayer? = null

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
        val vectorBack = findViewById<View>(R.id.vectorBack)
        vectorBack.setOnClickListener {
            onBackPressed()
        }
        setStartActivity(true)
        initActivity()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        setStartActivity(false)
    }

    private fun loadTrack(model: Track) {
        ShowActiveTrack(this, findViewById(R.id.main), model)
            .show()
    }

    private fun setStartActivity(key:Boolean) {
        Creator.provideSharedPreferencesInteractor(this)
            .setStartActivity(key)
    }

    private fun initActivity() {
        Creator.provideSharedPreferencesInteractor(this).getActiveTrackForMediaPlayer(
                SharedPreferencesInteractor.TrackConsumer {
                    val track: Track = it
                    if (track.previewUrl.isNotEmpty()) {
                        loadTrack(track)
                        mediaPlayer = MediaPlayerUseCase(findById<View>(R.id.main), track)
                        //ControlerPlayer(findViewById(R.id.main), track)
                    }
                })
    }
}
