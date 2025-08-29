package com.example.playlistmaker.common.util

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.playlistmaker.common.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.getKoin

object PlaylistBundleUtil {
    private val gson:Gson = getKoin().get()
    private val gsonList = object : TypeToken<List<Int>>() {}.getType()
    fun createBundle(key: String, playlist: Playlist): Bundle {
        return with(playlist) {
            bundleOf(
                key to
                        bundleOf(
                            "name" to name,
                            "photo" to photo,
                            "description" to description,
                            "idsTrack" to gson.toJson(idsTrack),
                            "count" to count
                        )
            )
        }
    }

    fun rewriteBundle(playlistBundle: Bundle): Playlist {
        return with(playlistBundle){
            Playlist(
                photo = getString("photo")?:"",
                name = getString("name")?:"",
                description = getString("description")?:"",
                idsTrack = gson.fromJson(getString("idsTrack"), gsonList),
                count = getString("count")?:""
            )
        }
    }
}