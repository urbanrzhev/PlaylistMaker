package com.example.playlistmaker.common.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonDataModule = module{
    factory<Gson> {
        Gson()
    }
    single<SharedPreferences>{
        androidContext().getSharedPreferences("my_all_preferences",MODE_PRIVATE)
    }
}