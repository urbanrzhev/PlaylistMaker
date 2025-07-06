package com.example.playlistmaker.main.ui.view_model

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.api.GetStartActivityUseCase
import com.example.playlistmaker.common.domain.api.SetStartActivityUseCase
import com.example.playlistmaker.media_library.ui.activity.MediaLibraryActivity
import com.example.playlistmaker.player.ui.activity.MediaPlayerActivity
import kotlin.math.sin

class MainViewModel(
    private val appContext: Context,
    private val getStartActivityUseCase: GetStartActivityUseCase,
    private val setStartActivityUseCase: SetStartActivityUseCase
) : ViewModel() {
    fun setStartActivity() {
        setStartActivityUseCase.execute(MAIN_ACTIVITY)
    }

    fun initActivity() {
        if(singleEvent) {
            singleEvent = false
            val intent: Intent? = when (getStartActivityUseCase.execute()) {
                PLAYER_ACTIVITY -> Intent(appContext, MediaPlayerActivity::class.java)
                MEDIA_LIBRARY_ACTIVITY -> Intent(appContext, MediaLibraryActivity::class.java)
                else -> null
            }?.apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent?.let {
                startActivity(it)
            }
        }
    }

    private fun startActivity(intent: Intent) {
        appContext.startActivity(intent)
    }

    companion object {
        private var singleEvent = true
        private const val MAIN_ACTIVITY = "main"
        private const val PLAYER_ACTIVITY = "player"
        private const val MEDIA_LIBRARY_ACTIVITY = "media_library"
    }
}
