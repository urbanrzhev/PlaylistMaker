package com.example.playlistmaker.player.domain.api

interface MediaPlayerRepository {
    fun preparePlayer(url:String,setOnPreparedListener:()->Unit, setOnCompletionListener:()->Unit, endListener:()->Unit)
    fun pausePlayer()
    fun getCurrentPosition():Int
    fun playbackControl():Boolean
    fun releasePlayer()
}
