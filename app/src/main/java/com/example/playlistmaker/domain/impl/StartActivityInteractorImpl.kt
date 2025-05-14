package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.StartActivityInteractor
import com.example.playlistmaker.domain.util.MyConstants

class StartActivityInteractorImpl(private val sharedRepository: SharedPreferencesRepository):StartActivityInteractor {
    private val key = MyConstants.MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY
    override fun getStartActivity(
        consumer: StartActivityInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getBoolean(key))
    }

    override fun setStartActivity(data: Boolean) {
        sharedRepository.setBoolean(key, data)
    }
}
