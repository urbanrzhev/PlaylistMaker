package com.example.playlistmaker.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.common.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setTrack(track: TrackInPlaylistEntity)
}