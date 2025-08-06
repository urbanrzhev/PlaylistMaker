package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.flow.Flow

interface NetworkClient {
    fun doRequest(dto: Any): Flow<Response>
}