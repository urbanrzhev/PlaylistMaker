package com.example.playlistmaker.common.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.koin.core.time.TimeInMillis

@Entity(tableName = "track_table_favorite")
data class TrackFavoriteEntity (
    val trackName: String,
    val artistName: String,
    val trackTimeNormal: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: String,
    @PrimaryKey @ColumnInfo(name = "track_id" )
    val trackId: Int,
    val timeOfAddition:Long = System.currentTimeMillis()
)