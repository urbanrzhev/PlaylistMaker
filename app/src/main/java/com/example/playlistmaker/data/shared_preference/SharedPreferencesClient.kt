package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.data.dto.SharedPreferencesBooleanResponse
import com.example.playlistmaker.data.dto.SharedPreferencesStringRequest
import com.example.playlistmaker.data.dto.SharedPreferencesStringResponse

interface SharedPreferencesClient {
    fun getBooleanRequest(dto:SharedPreferencesBooleanRequest):SharedPreferencesBooleanResponse
    fun getStringRequest(dto:SharedPreferencesStringRequest):SharedPreferencesStringResponse
    fun setBoolean(dto:SharedPreferencesBooleanRequest)
    fun setString(dto:SharedPreferencesStringRequest)
}