package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists():List<PlaylistEntity>
    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist:PlaylistEntity)
    @Query("SELECT * FROM playlist_table WHERE playlist_id =:playlistId")
    suspend fun getPlaylist(playlistId:Long):PlaylistEntity
    @Query("SELECT * FROM track_table_for_playlist WHERE track_id IN(:listId)")
    suspend fun getTracksFromPlaylistById(listId: List<Int>):List<TrackEntityForPlaylist>
    @Query("SELECT track FROM cross_table WHERE playlist_id_cross =:playlistId")
    suspend fun getIdsTrackFromPlaylist(playlistId: Long):List<Int>
    @Insert(TrackEntityForPlaylist::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Insert(CrossTrackAndPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCross(value:CrossTrackAndPlaylistEntity)
    @Transaction
    suspend fun addTrackInPlaylistTransaction(track:TrackEntityForPlaylist, crossEntity:CrossTrackAndPlaylistEntity){
        addTrackEntityForPlaylist(track)
        addCross(crossEntity)
    }
    @Query("DELETE FROM cross_table WHERE track =:trackId AND playlist_id_cross =:playlistId")
    suspend fun deleteTrackFromCross(trackId:Int, playlistId:Long)
    @Delete
    suspend fun deleteTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Query("SELECT EXISTS (SELECT 1 FROM cross_table WHERE track =:trackId)")
    suspend fun checkIdTrackInCrossTable(trackId: Int):Boolean
    @Transaction
    suspend fun deleteTrackFromPlaylistTransaction(track:TrackEntityForPlaylist, playlistId:Long){
        deleteTrackFromCross(trackId = track.trackId, playlistId)
        val deleteTrack = checkIdTrackInCrossTable(track.trackId)
        if (!deleteTrack)
            deleteTrackEntityForPlaylist(track)
    }
    @Transaction
    suspend fun getTracksFromPlaylist(playlistId: Long):List<TrackEntityForPlaylist>{
        val idsList = getIdsTrackFromPlaylist(playlistId = playlistId)
        val tracks = getTracksFromPlaylistById(idsList)
        return tracks
    }
    @Delete
    suspend fun deletePlaylist(playlist:PlaylistEntity)
    @Query("DELETE FROM track_table_for_playlist WHERE track_id IN(:idsTrack)")
    suspend fun deleteTracksByIdsListInTrackTableForPlaylist(idsTrack:List<Int>)
    @Transaction
    suspend fun deletePlaylistFirstStageTransaction(playlist: PlaylistEntity):List<Int>{
            val idsList = getIdsTrackFromPlaylist(playlistId = playlist.playlistId)
            deletePlaylist(playlist)
        return idsList
    }
    @Transaction
    suspend fun deletePlaylistSecondStageTransaction(deleteIdsList: List<Int>){
        val deleteList = mutableListOf<Int>()
        deleteIdsList.forEach {
            if (!checkIdTrackInCrossTable(it)) {
                deleteList.add(it)
            }
        }
        deleteTracksByIdsListInTrackTableForPlaylist(deleteList)
    }
}