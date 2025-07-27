package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.R

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val playButtonBackground: Int,
    val progress: String
) {
    class Default : PlayerState(false, R.drawable.play_button, "00:00")
    class Prepared : PlayerState(true, R.drawable.play_button, "00:00")
    class Playing(progress: String) : PlayerState(true, R.drawable.pause_button, progress)
    class Paused(progress: String) : PlayerState(true, R.drawable.play_button, progress)
}