package com.example.playlistmaker.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.common.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists():List<PlaylistEntity>
    @Query("UPDATE playlist_table SET idsTracks = :idsTracks WHERE playlist_name = :playlistName")
    suspend fun updatePlaylist(idsTracks:String,playlistName:String)
    @Query("SELECT idsTracks FROM playlist_table")
    suspend fun getItemsId():String
}