package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.common.util.TimeFormat
import com.example.playlistmaker.player.domain.api.GetActiveTrackUseCase
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.SetStartActivityUseCase

class AudioPlayerViewModel(
    private val getActiveTrackUseCase: GetActiveTrackUseCase,
    private val mediaPlayer: MediaPlayerInteractor,
    private val setStartActivityUseCase: SetStartActivityUseCase
) : ViewModel() {
    private var enablePlayButton = MutableLiveData(false)
    private val activeTrack = getActiveTrackUseCase.execute()
    private var backgroundPlayButton = MutableLiveData(R.drawable.play_play)
    private lateinit var playStopRunnable: Runnable
    private var timeProgressLiveData = MutableLiveData(activeTrack.trackTimeNormal)
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    fun observeEnablePlayButton(): LiveData<Boolean> = enablePlayButton
    fun observeBackgroundPlayButton(): LiveData<Int> = backgroundPlayButton
    fun observeTimeProgressLiveData(): LiveData<String> = timeProgressLiveData

    init {
        val url = activeTrack.previewUrl
        if (url.isNotEmpty()) {
            mediaPlayer.prepare(url, consumerPrepared = {
                enablePlayButton.value = true
            }, consumerCompleted = {
                backgroundPlayButton.value = R.drawable.play_play
                timeProgressLiveData.value = TimeFormat(DELAY_NULL).getTimeMM_SS()
                timeProgress(false)
            })
        }
        playStopRunnable = Runnable {
            timeProgressLiveData.value = TimeFormat(mediaPlayer.currentPosition()).getTimeMM_SS()
            mainThreadHandler.postDelayed(playStopRunnable, DELAY)
        }
    }

    fun getActiveTrack(): Track {
        return activeTrack
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

    fun setThisStartActivity(value: Boolean) {
        setStartActivityUseCase.execute(value)
    }

    fun control() {
        mediaPlayer.control(start = {
            backgroundPlayButton.value = R.drawable.pause_play
            timeProgress(true)
        }, pause = {
            backgroundPlayButton.value = R.drawable.play_play
            timeProgress(false)
        })
    }

    fun pause() {
        mediaPlayer.pause()
        backgroundPlayButton.value = R.drawable.play_play
    }

    override fun onCleared() {
        mediaPlayer.release()
        timeProgress(false)
    }

    companion object {
        private const val DELAY = 400L
        private const val DELAY_NULL = 0L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    getActiveTrackUseCase = Creator.provideGetActiveTrackUseCase(),
                    mediaPlayer = Creator.provideMediaPlayerInteractor(),
                    setStartActivityUseCase = Creator.provideSetStartActivityUseCase()
                )
            }
        }
    }
}