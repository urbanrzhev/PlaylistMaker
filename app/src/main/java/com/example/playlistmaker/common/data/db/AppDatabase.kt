package com.example.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.dao.TrackDao
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackFavoriteEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist

@Database(
    version = 2,
    entities = [
        TrackFavoriteEntity::class,
        PlaylistEntity::class,
        CrossTrackAndPlaylistEntity::class,
        TrackEntityForPlaylist::class
    ]
)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getTrackDao(): TrackDao
    abstract fun getPlaylistDao(): PlaylistDao
}