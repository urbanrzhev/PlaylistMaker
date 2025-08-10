package com.example.playlistmaker.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
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
    @ColumnInfo(name = "favorite")
    val favorite:Boolean,
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Int
)