package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists():List<PlaylistEntity>
    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    @Query("SELECT idsTracks FROM playlist_table")
    suspend fun getItemsId():String
}