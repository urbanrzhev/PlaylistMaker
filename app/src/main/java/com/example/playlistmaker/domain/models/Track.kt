package com.example.playlistmaker.domain.models

data class Track(
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeNormal: String = "",
    val artworkUrl100: String = "",
    val previewUrl: String = "",
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String = "",
    val country: String = "",
    val trackId: Int = 0
)