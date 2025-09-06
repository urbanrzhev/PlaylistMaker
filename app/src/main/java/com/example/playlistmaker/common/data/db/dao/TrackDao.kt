package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.common.data.db.entity.TrackFavoriteEntity

@Dao
interface TrackDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavoriteTrack(track: TrackFavoriteEntity)
    @Query("SELECT EXISTS (SELECT 1 FROM track_table_favorite WHERE track_id =:trackId)")
    suspend fun checkTrackInFavorites(trackId: Int):Boolean
    @Query("DELETE FROM track_table_favorite WHERE track_id = :trackId")
    suspend fun deleteFavoriteTrack(trackId:Int)
    @Query("SELECT * FROM track_table_favorite")
    suspend fun getAllFavoritesTracks():List<TrackFavoriteEntity>
    @Query("SELECT track_id FROM track_table_favorite")
    suspend fun getFavoritesTrackIds():List<Int>
}

