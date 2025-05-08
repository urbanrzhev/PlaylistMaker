package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(baseUrl:String) : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = iTunesApiService.searchTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}