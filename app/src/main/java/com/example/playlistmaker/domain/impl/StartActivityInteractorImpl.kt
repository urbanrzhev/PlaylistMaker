package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.StartActivityInteractor

class StartActivityInteractorImpl(private val sharedRepository: SharedPreferencesRepository):StartActivityInteractor {
    override fun getStartActivity(
        consumer: StartActivityInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getMediaPlayerLoadStartActivity())
    }

    override fun setStartActivity(keyData: Boolean) {
        sharedRepository.setMediaPlayerLoadStartActivity(keyData)
    }
}