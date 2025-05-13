package com.example.playlistmaker.domain.api

interface StartActivityInteractor {
    fun getStartActivity(
        consumer: BooleanConsumer
    )
    fun setStartActivity(keyData: Boolean)
    fun interface BooleanConsumer {
        fun consume(response: Boolean)
    }
}