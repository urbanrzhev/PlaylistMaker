package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager

class CheckInternetManager(private val context: Context) {
    fun getSystemService(): ConnectivityManager {
        return context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    }
}