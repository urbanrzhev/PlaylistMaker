package com.example.playlistmaker.common.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    val photo:String,
    @PrimaryKey @ColumnInfo(name = "playlist_name")
    val name:String,
    val description:String
)