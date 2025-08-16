package com.example.playlistmaker.common.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.common.data.repository.DataBaseRepositoryImpl
import com.example.playlistmaker.common.data.repository.SharedPreferencesManagerImpl
import com.example.playlistmaker.common.db.AppDatabase
import com.example.playlistmaker.common.db.converters.TrackDbConverter
import com.example.playlistmaker.common.domain.api.DataBaseInteractor
import com.example.playlistmaker.common.domain.api.DataBaseRepository
import com.example.playlistmaker.common.domain.api.GetThemeUseCase
import com.example.playlistmaker.common.domain.api.SharedPreferencesManager
import com.example.playlistmaker.common.domain.impl.DataBaseInteractorImpl
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
    factory <DataBaseRepository> {
        DataBaseRepositoryImpl(get(),get())
    }
    factory <TrackDbConverter> {
        TrackDbConverter()
    }
    factory <DataBaseInteractor> {
        DataBaseInteractorImpl(get())
    }
    single<AppDatabase>{
        Room.databaseBuilder(androidContext(),AppDatabase::class.java,"database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}