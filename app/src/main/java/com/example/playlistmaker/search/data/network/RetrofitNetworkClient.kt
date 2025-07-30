package com.example.playlistmaker.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {

    override suspend fun doRequestSuspend(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            if (!isConnected()) {
                Response().apply { resultCode = -1 }
            }
            if (dto is TracksSearchRequest) {
                try {
                    val resp = iTunesApiService.searchTracksSuspend(dto.expression)
                    resp.apply { resultCode = 200 }
                } catch (e: Exception) {
                    Response().apply { resultCode = 500 }
                }
            } else {
                Response().apply { resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}