package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.common.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setTrack(track: TrackInPlaylistEntity)
    @Query("SELECT track_id FROM track_in_playlist_table")
    suspend fun getTracks():List<TrackEntity>
}