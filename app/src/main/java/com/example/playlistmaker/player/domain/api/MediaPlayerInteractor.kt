package com.example.playlistmaker.player.domain.api

interface MediaPlayerInteractor {
    fun prepare(url:String, consumerPrepared: Consumer, consumerCompleted: Consumer)
    fun release()
    fun pause()
    fun currentPosition():Int
    fun control(start: Consumer, pause: Consumer)
    fun interface Consumer{
        fun consume()
    }
}