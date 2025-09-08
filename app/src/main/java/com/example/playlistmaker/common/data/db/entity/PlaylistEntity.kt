package com.example.playlistmaker.common.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlist_id")
    val playlistId:Long = 0L,
    val photo:String,
    @ColumnInfo(name = "playlist_name")
    val name:String,
    val description:String
)