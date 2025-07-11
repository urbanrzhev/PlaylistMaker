package com.example.playlistmaker.search.data.dto

data class TrackDto(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String = "" ,
    val trackTimeNormal: String = "",
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackId: Int
)
