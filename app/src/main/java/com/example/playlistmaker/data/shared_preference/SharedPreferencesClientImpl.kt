package com.example.playlistmaker.data.shared_preference

import android.content.Context
import com.example.playlistmaker.data.dto.SharedPreferencesBooleanRequest
import com.example.playlistmaker.data.dto.SharedPreferencesBooleanResponse
import com.example.playlistmaker.data.dto.SharedPreferencesStringRequest
import com.example.playlistmaker.data.dto.SharedPreferencesStringResponse
import com.example.playlistmaker.domain.util.MyConstants

class SharedPreferencesClientImpl(private val context: Context) : SharedPreferencesClient {
  private val gson = Gson() 
    private val sharedPreferences =
        context.getSharedPreferences(MyConstants.MY_ALL_PREFERENCES, MyConstants.MODE_PRIVATE)
    override fun getBooleanRequest(dto: SharedPreferencesBooleanRequest): SharedPreferencesBooleanResponse =
        SharedPreferencesBooleanResponse(sharedPreferences.getBoolean(dto.key, false))

    override fun getStringRequest(dto: SharedPreferencesStringRequest): SharedPreferencesStringResponse =
        SharedPreferencesStringResponse(sharedPreferences.getString(dto.key, null))

    override fun setBooleanData(dto:SharedPreferencesBooleanRequest) {
        sharedPreferences.edit()
            .putBoolean(dto.key,dto.dataBoolean?:false)
            .apply()
    }

    override fun setStringData(dto: SharedPreferencesStringRequest) {
        sharedPreferences.edit()
            .putString(dto.key,dto.dataString?:"")
            .apply()
    }
    override fun getTrackRequest(dto:SharedPreferencesTrackRequest) :TrackDto?{
         return gson.fromJson(
            sharedPreferences.getString(
                dto.key,
                null
            ), TrackDto::class.java
        )
       }
    override fun setTrackData(dto:SharedPreferencesTrackRequest) {
        
     } 

}








