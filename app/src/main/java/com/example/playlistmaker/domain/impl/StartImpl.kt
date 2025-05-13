package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.domain.api.SharedPreferencesRepository2
import com.example.playlistmaker.domain.api.Start
import com.example.playlistmaker.domain.util.MyConstants

class StartImpl(val repository:SharedPreferencesRepository2):Start {
    override fun getStartActivity(consumer: Start.BooleanConsumer) {
        consumer.consume(
            repository.getMediaPlayerLoadStartActivity(SharedPreferencesBooleanRequest(MyConstants.MY_KEY_ON_OFF_MEDIA_LIBRARY_ACTIVITY,null))
        )
    }
}