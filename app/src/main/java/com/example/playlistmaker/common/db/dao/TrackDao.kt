package com.example.playlistmaker.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.common.db.entity.TrackEntity

@Dao
interface TrackDao{
    @Query("SELECT * FROM track_table_favorite WHERE track_id = :trackId")
    suspend fun getFavoriteTrack(trackId:Int):TrackEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavoriteTrack(track:TrackEntity)
    @Query("DELETE FROM track_table_favorite WHERE track_id = :trackId")
    suspend fun deleteFavoriteTrack(trackId:Int)
    @Query("SELECT * FROM track_table_favorite")
    suspend fun getAllFavoritesTracks():List<TrackEntity>
    @Query("SELECT track_id FROM track_table_favorite")
    suspend fun getFavoritesTrackIds():List<Int>
}

