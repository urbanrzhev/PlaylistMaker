package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.R

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val playButtonBackground: Int
) {
    class Default : PlayerState(false, R.drawable.play_button)
    class Prepared : PlayerState(true, R.drawable.play_button)
    class Playing : PlayerState(true, R.drawable.pause_button)
    class Paused : PlayerState(true, R.drawable.play_button)
}