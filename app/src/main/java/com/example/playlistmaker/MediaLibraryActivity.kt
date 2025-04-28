package com.example.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

var trackActive: Track? = null
const val MY_ON_OFF_MEDIA_LIBRARY_ACTIVITY_PREFERENCES =
    "my_on_off_media_library_activity_preferences"
const val MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY = "my_key_on_off_media_library_activity"
private const val MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY =
    "my_key_default_track_media_library_activity"
private const val DELAY = 500L

class MediaLibraryActivity : AppCompatActivity() {
    private var sharedOnOfAndDefaultMedia: SharedPreferences? = null
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var playStopRunnable: Runnable = Runnable{}
    private var mainThreadHandler: Handler? = null
    private var play: View? = null
    private var timeText: TextView? = null
    private val gson = Gson()

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

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
        play = findViewById<Button>(R.id.buttonPause)
        timeText = findViewById<TextView>(R.id.textTimeOutPause)
        mainThreadHandler = Handler(Looper.getMainLooper())
        val vectorBack = findViewById<View>(R.id.vectorBack)
        playStopRunnable = object : Runnable {
            override fun run() {
                timeText?.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
        vectorBack.setOnClickListener {
            onBackPressed()
        }
        sharedOnOfAndDefaultMedia = getSharedPreferences(
            MY_ON_OFF_MEDIA_LIBRARY_ACTIVITY_PREFERENCES,
            MODE_PRIVATE
        )
        sharedOnOfAndDefaultMedia?.edit()
            ?.putString(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY, "true")
            ?.apply()

        getSetTrackDefault()
        preparePlayer()
        play?.setOnClickListener {
            playbackControl()
        }
    }

    private fun timeProgress(value: Boolean) {
        if (value) {
            mainThreadHandler?.post(
                playStopRunnable
            )
        } else {
            mainThreadHandler?.removeCallbacks(playStopRunnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            pausePlayer()
            timeText?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0L)
        }
        play?.setBackgroundResource(R.drawable.pause_play)
        playerState = STATE_PLAYING
        timeProgress(true)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play?.setBackgroundResource(R.drawable.play_play)
        playerState = STATE_PAUSED
        timeProgress(false)
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

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        sharedOnOfAndDefaultMedia?.edit()
            ?.putString(MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY, "false")
            ?.apply()
    }

    private fun preparePlayer() {
        if(trackActive != null) {
            mediaPlayer.setDataSource(trackActive?.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                play?.isEnabled = true
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        }
    }

    private fun loadTrack(model: Track) {
        val imageCover = findViewById<ImageView>(R.id.imageView3)
        val lengthTrack = findViewById<TextView>(R.id.textLengthTrackValue1)
        val textTimeOut = findViewById<TextView>(R.id.textTimeOutPause)
        val collectionName = findViewById<TextView>(R.id.textAlbumTrackValue2)
        visibleView(
            findViewById<View>(R.id.textAlbumTrackDescription2),
            model.collectionName,
            collectionName
        )
        val primaryGenreName = findViewById<TextView>(R.id.textAlbumTrackValue4)
        visibleView(
            findViewById<View>(R.id.textAlbumTrackDescription4),
            model.primaryGenreName,
            primaryGenreName
        )
        val country = findViewById<TextView>(R.id.textAlbumTrackValue5)
        visibleView(findViewById<View>(R.id.textAlbumTrackDescription5), model.country, country)
        val year = findViewById<TextView>(R.id.textLengthTrackValue3)
        visibleView(
            findViewById<View>(R.id.textLengthTrackDescription3),
            getYear(model.releaseDate),
            year
        )
        findViewById<TextView>(R.id.textBig).text = model.trackName
        findViewById<TextView>(R.id.textArtistName).text = model.artistName

        try {
            lengthTrack?.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
            textTimeOut?.text = lengthTrack?.text
            Glide.with(this)
                .load(getCoverArtwork(model.artworkUrl100))
                .transform(RoundedCorners(dpToPx(2f, this)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search)
                .into(imageCover)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                this.getString(R.string.crash_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getSetTrackDefault() {
        if (trackActive != null) {
            loadTrack(trackActive!!)
            sharedOnOfAndDefaultMedia?.edit()
                ?.putString(MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY, gson.toJson(trackActive))
                ?.apply()
        } else {
            trackActive = gson.fromJson(
                sharedOnOfAndDefaultMedia?.getString(
                    MY_KEY_DEFAULT_TRACK_MEDIA_LIBRARY_ACTIVITY,
                    null
                ), Track::class.java
            )
            if (trackActive != null)
                loadTrack(trackActive!!)
        }
    }

    private fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun getYear(date: String): String? {
        try {
            return date.split("-")[0]
        } catch (e: Exception) {
            return null
        }
    }

    private fun visibleView(view: View, modelString: String?, view2: TextView) {
        if (modelString == null) {
            view.isVisible = false
            view2.isVisible = false
        } else {
            view2.text = modelString
        }
    }
}