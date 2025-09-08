package com.example.playlistmaker.new_playlist.data.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.new_playlist.domain.api.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FileRepositoryImpl(
    private val context: Context
) : FileRepository {
    override fun setNewPathFile(uri: Uri): Flow<Uri> {
        return flow {
            val filePath = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MY_DIRECTORY_PICTURES
            )
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath, UUID.randomUUID().toString())
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            emit(file.toUri())
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val MY_DIRECTORY_PICTURES = "playlist_maker_pictures"
    }
}