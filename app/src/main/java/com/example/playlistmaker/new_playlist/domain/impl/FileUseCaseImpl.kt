package com.example.playlistmaker.new_playlist.domain.impl

import android.net.Uri
import com.example.playlistmaker.new_playlist.domain.api.FileRepository
import com.example.playlistmaker.new_playlist.domain.api.FileUseCase
import kotlinx.coroutines.flow.Flow

class FileUseCaseImpl(
    private val repository:FileRepository
): FileUseCase {
    override fun execute(uri: Uri): Flow<Uri> {
      return repository.setNewPathFile(uri)
    }
}