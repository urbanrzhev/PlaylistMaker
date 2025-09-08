package com.example.playlistmaker.common.domain.models

data class Playlist(
    var playlistId:Long = 0,
    var photo:String = "",
    var name:String = "",
    var description:String = "",
    var idsTrack:MutableList<Int> = mutableListOf(),
    var count:String = ""
)