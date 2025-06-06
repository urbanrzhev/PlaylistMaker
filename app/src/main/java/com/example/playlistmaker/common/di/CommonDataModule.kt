package com.example.playlistmaker.common.di

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.common.util.TimeFormat
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SimpleDateFormat")
val commonDataModule = module {
    single<Gson> {
        Gson()
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("my_all_preferences", MODE_PRIVATE)
    }
    factory<TimeFormat> { (param: Any) ->
        when (param) {
            is Long -> TimeFormat(timeMillisLong = param, get())
            is Int -> TimeFormat(timeMillisInt = param, get())
            is String -> TimeFormat(timeMillis = param, get())
            else -> throw IllegalArgumentException("Unsupported parameter type")
        }
    }
    factory<SimpleDateFormat> {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
}