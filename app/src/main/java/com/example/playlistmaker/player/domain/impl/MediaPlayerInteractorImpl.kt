package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val repositoryImpl: MediaPlayerRepository):
    MediaPlayerInteractor {
    override fun prepare(url: String, consumerPrepared: MediaPlayerInteractor.Consumer, consumerCompleted: MediaPlayerInteractor.Consumer, consumerEnd: MediaPlayerInteractor.Consumer) {
        repositoryImpl.preparePlayer(url, setOnPreparedListener = {
            consumerPrepared.consume()
        }, setOnCompletionListener = {
            consumerCompleted.consume()
        }, endListener = {
            consumerEnd.consume()
        })
    }

    override fun release() {
        repositoryImpl.releasePlayer()
    }

    override fun pause() {
        repositoryImpl.pausePlayer()
    }

    override fun currentPosition():Int {
        return repositoryImpl.getCurrentPosition()
    }

    override fun control(start: MediaPlayerInteractor.Consumer, pause: MediaPlayerInteractor.Consumer) {
        if(repositoryImpl.playbackControl()){
            start.consume()
        }
        else{
            pause.consume()
        }
    }
}