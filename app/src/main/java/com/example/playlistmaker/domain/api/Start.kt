package com.example.playlistmaker.domain.api

interface Start {
    fun getStartActivity(
        consumer: BooleanConsumer
    )
    //fun setStartActivity(keyData: Boolean)
    fun interface BooleanConsumer {
        fun consume(response:Boolean)
    }
}