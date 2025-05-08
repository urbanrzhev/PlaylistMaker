package com.example.playlistmaker

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.data.network.ITunesApiService
import com.example.playlistmaker.presentation.TracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*data class MusicList(
    val resultCount: Int,
    val results: List<Track>
)*/

/*interface InterfaceITunes {
    @GET("/search?entity=song")
    fun getMusicList(@Query("term") text: String): Call<MusicList>
}*/

object ITunesService {
    private lateinit var onProgressBarVisibleTrueDefault: () -> Unit
    private lateinit var temporaryRequestString: String
    private lateinit var callbackSevice: (List<TrackDto>) -> Unit
    private lateinit var errorCallbackSevice: (Int,Boolean) -> Unit
    private lateinit var errorConnectionCallbackSevice: () -> Unit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApi = retrofit.create(ITunesApiService::class.java)
    //var searchHist: SearchHistory? = null
    fun load(
        request: String,
      //  searchHistory: SearchHistory?,
        onProgressBarVisibleTrueCallback: () -> Unit = onProgressBarVisibleTrueDefault,
        callback: (List<TrackDto>) -> Unit,
        errorCallback: (Int,Boolean)->Unit,
        errorConnectionCallback: () -> Unit
    ) {
        callbackSevice = callback
        errorCallbackSevice = errorCallback
        errorConnectionCallbackSevice = errorConnectionCallback
        onProgressBarVisibleTrueCallback()
        //searchHist = searchHistory
        onProgressBarVisibleTrueDefault = onProgressBarVisibleTrueCallback
        if (request.isNotEmpty()) {
            temporaryRequestString = request
            iTunesApi.searchTracks(request).enqueue(object : Callback<TracksSearchResponse> {
                override fun onResponse(
                    call: Call<TracksSearchResponse>,
                    response: retrofit2.Response<TracksSearchResponse>
                ) {
                    if ((response.body()?.results?.size ?: 0) > 0 && response.code() == 200) {
                        callback(response.body()!!.results)
                    } else if (response.code() != 200)
                        errorCallback(TracksAdapter.SEARCH_NOT_CALL,true)
                    else
                        errorCallback(TracksAdapter.SEARCH_NOT_TRACK,false)
                }

                override fun onFailure(call: Call<TracksSearchResponse>, t: Throwable) {
                    errorConnectionCallback()
                }
            })
        }
    }

    fun load() {
   //     load(temporaryRequestString, searchHistory = searchHist, callback = callbackSevice, errorCallback = errorCallbackSevice, errorConnectionCallback = errorConnectionCallbackSevice)
    }
}