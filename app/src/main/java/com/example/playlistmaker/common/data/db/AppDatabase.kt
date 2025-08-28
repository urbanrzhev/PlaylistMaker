package com.example.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.dao.TrackDao
import com.example.playlistmaker.common.data.db.dao.TrackInPlaylistsDao
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.common.data.db.entity.TrackInPlaylistEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TrackInPlaylistEntity::class
    ]
)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getTrackDao(): TrackDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getTrackInPlaylistDao(): TrackInPlaylistsDao
}