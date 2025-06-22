package com.example.playlistmaker.media_library.di

import com.example.playlistmaker.media_library.ui.view_model.MediaLibraryViewModel
import org.koin.dsl.module

val mediaLibraryModule = module{
    factory<MediaLibraryViewModel>{
        MediaLibraryViewModel()
    }
}