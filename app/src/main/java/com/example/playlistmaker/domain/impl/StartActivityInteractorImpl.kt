package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.StartActivityInteractor

class StartActivityInteractorImpl(private val sharedRepository: SharedPreferencesRepository):StartActivityInteractor {
  val key = MyConstants. 
    override fun getStartActivity(
        consumer: StartActivityInteractor.BooleanConsumer
    ) {
        consumer.consume(sharedRepository.getBoolean(key) /*getMediaPlayerLoadStartActivity()*/)
    }

    override fun setStartActivity(data: Boolean) {
        sharedRepository.setBoolean(key, data) /*setMediaPlayerLoadStartActivity(keyData)*/
    }
}
