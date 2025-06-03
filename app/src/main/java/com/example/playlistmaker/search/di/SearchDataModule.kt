package com.example.playlistmaker.search.di

import android.net.ConnectivityManager
import com.example.playlistmaker.search.data.network.CheckInternetManager
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    single<NetworkClient>{
        RetrofitNetworkClient(get(),get())
    }
    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
    single<ConnectivityManager> {
        CheckInternetManager(androidContext())
            .getSystemService()
    }
}