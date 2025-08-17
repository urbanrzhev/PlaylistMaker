package com.example.playlistmaker.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.common.util.TimeFormat

@Entity(tableName = "track_table_favorite")
data class TrackEntity (
    val trackName: String,
    val artistName: String,
    val trackTimeNormal: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    @PrimaryKey @ColumnInfo(name = "track_id" )
    val trackId: Int,
    val timeOfAddition:Long = System.currentTimeMillis()
)