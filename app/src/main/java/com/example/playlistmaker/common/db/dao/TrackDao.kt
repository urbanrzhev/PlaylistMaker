package com.example.playlistmaker.common.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.playlistmaker.common.db.entity.TrackEntity

@Dao
interface TrackDao{
    @Query("SELECT * FROM track_table WHERE favorite = 1")
     fun getFavoritesTracks():TrackEntity
}

