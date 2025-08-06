package com.example.playlistmaker.player.domain.api

interface MediaPlayerRepository {
    fun preparePlayer(url:String,setOnPreparedListener:()->Unit, setOnCompletionListener:()->Unit)
    fun pausePlayer()
    fun isPlaying():Boolean
    fun getCurrentPosition():Int
    fun playbackControl():Boolean
    fun releasePlayer()
}
