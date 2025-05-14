package com.example.playlistmaker.domain.api

interface StartActivityInteractor {
    fun getStartActivity(
        consumer: BooleanConsumer
    )
    fun setStartActivity(data: Boolean)
    fun interface BooleanConsumer {
        fun consume(response: Boolean)
    }
}