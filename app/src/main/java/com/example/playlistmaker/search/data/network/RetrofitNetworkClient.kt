package com.example.playlistmaker.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {

    override fun doRequest(dto: Any): Flow<Response> {
        return flow {
            if (!isConnected()) {
                emit(Response().apply { resultCode = -1 })
            }
            if (dto is TracksSearchRequest) {
                try {
                    val resp = iTunesApiService.searchTracksSuspend(dto.expression)
                    emit(resp.apply { resultCode = 200 })
                } catch (e: Exception) {
                    emit(Response().apply { resultCode = 500 })
                }
            } else {
                emit(Response().apply { resultCode = 400 })
            }
        }.flowOn(Dispatchers.IO)
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