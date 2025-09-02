package com.example.playlistmaker.common.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.common.data.db.entity.CrossTrackAndPlaylistEntity
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.data.db.entity.TrackEntityForPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists():List<PlaylistEntity>
    @Query("SELECT * FROM playlist_table WHERE playlist_name =:playlistName")
    suspend fun getPlaylist(playlistName: String):PlaylistEntity
    @Query("SELECT * FROM track_table_for_playlist WHERE track_id IN(:listId)")
    suspend fun getTracksFromPlaylistById(listId: List<Int>):List<TrackEntityForPlaylist>
    @Query("SELECT track FROM cross_table WHERE playlist =:playlistName")
    suspend fun getIdsTrackFromPlaylist(playlistName: String):List<Int>
    @Insert(TrackEntityForPlaylist::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Insert(CrossTrackAndPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCross(value:CrossTrackAndPlaylistEntity)
    @Transaction
    suspend fun addTrackInPlaylistTransaction(track:TrackEntityForPlaylist, crossEntity:CrossTrackAndPlaylistEntity){
        addTrackEntityForPlaylist(track)
        addCross(crossEntity)
    }
    @Query("DELETE FROM cross_table WHERE track =:trackId AND playlist =:playlistName")
    suspend fun deleteTrackFromCross(trackId:Int, playlistName: String)
    @Delete
    suspend fun deleteTrackEntityForPlaylist(track:TrackEntityForPlaylist)
    @Query("SELECT EXISTS (SELECT 1 FROM cross_table WHERE track =:trackId)")
    suspend fun checkIdTrackInCrossTable(trackId: Int):Boolean
    @Transaction
    suspend fun deleteTrackFromPlaylistTransaction(track:TrackEntityForPlaylist, playlistName: String){
        deleteTrackFromCross(trackId = track.trackId, playlistName)
        val deleteTrack = checkIdTrackInCrossTable(track.trackId)
        if (!deleteTrack)
            deleteTrackEntityForPlaylist(track)
    }
    @Transaction
    suspend fun getTracksFromPlaylist(playlistName: String):List<TrackEntityForPlaylist>{
        val idsList = getIdsTrackFromPlaylist(playlistName = playlistName)
        val tracks = getTracksFromPlaylistById(idsList)
        return tracks
    }
    @Delete
    suspend fun deletePlaylist(playlist:PlaylistEntity)
    @Query("DELETE FROM track_table_for_playlist WHERE track_id IN(:idsTrack)")
    suspend fun deleteTracksByIdsListInTrackTableForPlaylist(idsTrack:List<Int>)
    @Transaction
    suspend fun deletePlaylistFirstStageTransaction(playlist: PlaylistEntity):List<Int>{
            val idsList = getIdsTrackFromPlaylist(playlistName = playlist.name)
            deletePlaylist(playlist)
        Log.v("my", "deletePlaylistFirstDAO")
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
        delay(5000L)

        Log.v("my", "deletePlaylistSecondDAO")
    }
}