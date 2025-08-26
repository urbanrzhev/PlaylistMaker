package com.example.playlistmaker.common.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.common.data.repository.DataBasePlaylistRepositoryImpl
import com.example.playlistmaker.common.data.repository.DataBaseTracksRepositoryImpl
import com.example.playlistmaker.common.data.repository.SharedPreferencesManagerImpl
import com.example.playlistmaker.common.db.AppDatabase
import com.example.playlistmaker.common.db.converters.TrackDbConverter
import com.example.playlistmaker.common.db.dao.PlaylistDao
import com.example.playlistmaker.common.db.dao.TrackDao
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.api.DataBaseTracksInteractor
import com.example.playlistmaker.common.domain.api.DataBaseTracksRepository
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.impl.DataBasePlaylistsInteractorImpl
import com.example.playlistmaker.common.domain.impl.DataBaseTracksInteractorImpl
import com.example.playlistmaker.common.domain.impl.GetThemeUseCaseImpl
import com.example.playlistmaker.common.util.TimeFormat
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Locale

val commonModule = module {
    single<Gson> {
        Gson()
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("my_all_preferences", MODE_PRIVATE)
    }
    factory<TimeFormat> {
        TimeFormat(get())
    }
    factory<SimpleDateFormat> {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
    factory<SharedPreferencesManager> {
        SharedPreferencesManagerImpl(get(),get(),get())
    }
    factory<GetThemeUseCase>{
        GetThemeUseCaseImpl(get())
    }
    factory <DataBaseTracksRepository> {
        DataBaseTracksRepositoryImpl(get(),get())
    }
    factory <TrackDbConverter> {
        TrackDbConverter()
    }
    factory <DataBaseTracksInteractor> {
        DataBaseTracksInteractorImpl(get())
    }
    single<TrackDao>{
        Room.databaseBuilder(androidContext(),AppDatabase::class.java,"database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
            .getTrackDao()
    }
    single<PlaylistDao>{
        Room.databaseBuilder(androidContext(),AppDatabase::class.java,"database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
            .getPlaylistDao()
    }
    factory<DataBasePlaylistsInteractor>{
        DataBasePlaylistsInteractorImpl(get())
    }
    factory <DataBasePlaylistsRepository> {
        DataBasePlaylistRepositoryImpl(get(),get())
    }
}