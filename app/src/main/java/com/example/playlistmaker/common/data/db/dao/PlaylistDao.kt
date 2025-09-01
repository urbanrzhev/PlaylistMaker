package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists():List<PlaylistEntity>
    @Delete
    suspend fun deletePlaylist(playlist:PlaylistEntity)
    @Query("SELECT * FROM track_table_for_playlist WHERE track_id IN(:listId)")
    suspend fun getTracksFromPlaylistById(listId: List<Int>):List<TrackEntityForPlaylist>
    @Query("SELECT track FROM cross_table WHERE playlist =:playlistName")
    suspend fun getIdsTrackFromPlaylist(playlistName: String):List<Int>
    @Insert(TrackEntityForPlaylist::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Insert(CrossTrackAndPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCross(value:CrossTrackAndPlaylistEntity)
    @Transaction
    suspend fun addTrackInPlaylist(track:TrackEntityForPlaylist, crossEntity:CrossTrackAndPlaylistEntity){
        addTrackEntityForPlaylist(track)
        addCross(crossEntity)
    }
    @Query("DELETE FROM cross_table WHERE track =:trackId AND playlist =:playlistName")
    suspend fun deleteTrackFromCross(trackId:Int, playlistName: String)
    @Delete
    suspend fun deleteTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Query("SELECT * FROM cross_table WHERE track =:trackId")
    suspend fun checkIdTrackFromPlaylist(trackId: Int):List<CrossTrackAndPlaylistEntity>
    @Transaction
    suspend fun deleteTrackFromPlaylistTransaction(track:TrackEntityForPlaylist, playlistName: String){
        deleteTrackFromCross(trackId = track.trackId, playlistName)
        val deleteList = checkIdTrackFromPlaylist(track.trackId)
        if (deleteList.isEmpty())
            deleteTrackEntityForPlaylist(track)
    }
    @Transaction
    suspend fun getTracksFromPlaylist(playlistName: String):List<TrackEntityForPlaylist>{
        val idsList = getIdsTrackFromPlaylist(playlistName = playlistName)
        val tracks = getTracksFromPlaylistById(idsList)
        return tracks
    }
}