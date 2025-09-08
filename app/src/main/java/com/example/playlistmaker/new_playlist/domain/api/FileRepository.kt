package com.example.playlistmaker.new_playlist.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    fun setNewPathFile(uri: Uri): Flow<Uri>
}