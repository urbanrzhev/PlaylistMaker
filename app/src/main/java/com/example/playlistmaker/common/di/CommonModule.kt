package com.example.playlistmaker.common.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.common.data.repository.DataBasePlaylistRepositoryImpl
import com.example.playlistmaker.common.data.repository.DataBaseFavoritesTracksRepositoryImpl
import com.example.playlistmaker.common.data.repository.SharedPreferencesManagerImpl
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.data.db.converters.ListFavoriteTrackEntityDbConverter
import com.example.playlistmaker.common.data.db.converters.TrackEntityDbConverter
import com.example.playlistmaker.common.data.db.converters.TrackEntityForPlaylistDbConverter
import com.example.playlistmaker.common.data.db.dao.PlaylistDao
import com.example.playlistmaker.common.data.db.dao.TrackDao
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsInteractor
import com.example.playlistmaker.common.domain.api.DataBasePlaylistsRepository
import com.example.playlistmaker.common.domain.api.DataBaseFavoritesTracksInteractor
import com.example.playlistmaker.common.domain.api.DataBaseFavoritesTracksRepository
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.impl.DataBasePlaylistsInteractorImpl
import com.example.playlistmaker.common.domain.impl.DataBaseFavoritesTracksInteractorImpl
import com.example.playlistmaker.common.domain.impl.GetThemeUseCaseImpl
import com.example.playlistmaker.common.util.OrthographyCount
import com.example.playlistmaker.common.util.TimeFormat
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModule = module {
    single<Gson> {
        Gson()
    }
    single<OrthographyCount>{
        OrthographyCount(get())
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("my_all_preferences", MODE_PRIVATE)
    }
    factory<TimeFormat> {
        TimeFormat()
    }
    factory<SharedPreferencesManager> {
        SharedPreferencesManagerImpl(get(),get(),get())
    }
    factory<GetThemeUseCase>{
        GetThemeUseCaseImpl(get())
    }
    factory <DataBaseFavoritesTracksRepository> {
        DataBaseFavoritesTracksRepositoryImpl(get(),get())
    }
    factory <TrackEntityDbConverter> {
        TrackEntityDbConverter()
    }
    factory<TrackEntityForPlaylistDbConverter> {
        TrackEntityForPlaylistDbConverter()
    }
    factory <DataBaseFavoritesTracksInteractor> {
        DataBaseFavoritesTracksInteractorImpl(get())
    }
    factory<ListFavoriteTrackEntityDbConverter> {
        ListFavoriteTrackEntityDbConverter()
    }
    single<TrackDao>{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java,"database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
            .getTrackDao()
    }
    single<PlaylistDao>{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java,"database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
            .getPlaylistDao()
    }
    factory<DataBasePlaylistsInteractor>{
        DataBasePlaylistsInteractorImpl(get())
    }
    factory <DataBasePlaylistsRepository> {
        DataBasePlaylistRepositoryImpl(get(),get(),get())
    }
}