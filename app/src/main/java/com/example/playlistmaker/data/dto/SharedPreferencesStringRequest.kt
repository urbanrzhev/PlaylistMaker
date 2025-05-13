package com.example.playlistmaker.data.dto

data class SharedPreferencesStringRequest(
    val key:String,
    val data:String? = null
)