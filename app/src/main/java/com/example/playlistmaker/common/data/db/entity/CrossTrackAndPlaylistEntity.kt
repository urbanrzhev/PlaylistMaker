package com.example.playlistmaker.common.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cross_table" , foreignKeys = [
    ForeignKey(
        entity = TrackEntityForPlaylist::class,
        parentColumns = ["track_id"],
        childColumns = ["track"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["playlist_id"],
        childColumns = ["playlist_id_cross"],
        onDelete = ForeignKey.CASCADE
    )
]
)
data class CrossTrackAndPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val crossReferencesId:Long = 0,
    @ColumnInfo(name = "track")
    val trackId:Int,
    @ColumnInfo(name = "playlist_id_cross")
    val playlistId:Long
)