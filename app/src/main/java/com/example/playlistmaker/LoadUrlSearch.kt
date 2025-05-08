package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val previewUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackId: Int
)

data class MusicList(
    val resultCount: Int,
    val results: List<Track>
)

interface InterfaceITunes {
    @GET("/search?entity=song")
    fun getMusicList(@Query("term") text: String): Call<MusicList>
}

object ITunesService {
    private lateinit var onProgressBarVisibleTrueDefault: () -> Unit
    private lateinit var temporaryRequestString: String
    private lateinit var callbackSevice: (List<Track>) -> Unit
    private lateinit var errorCallbackSevice: (Int,Boolean) -> Unit
    private lateinit var errorConnectionCallbackSevice: () -> Unit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApi: InterfaceITunes = retrofit.create(InterfaceITunes::class.java)
    var searchHist: SearchHistory? = null
    fun load(
        request: String,
        searchHistory: SearchHistory?,
        onProgressBarVisibleTrueCallback: () -> Unit = onProgressBarVisibleTrueDefault,
        callback: (List<Track>) -> Unit,
        errorCallback: (Int,Boolean)->Unit,
        errorConnectionCallback: () -> Unit
    ) {
        callbackSevice = callback
        errorCallbackSevice = errorCallback
        errorConnectionCallbackSevice = errorConnectionCallback
        onProgressBarVisibleTrueCallback()
        searchHist = searchHistory
        onProgressBarVisibleTrueDefault = onProgressBarVisibleTrueCallback
        if (request.isNotEmpty()) {
            temporaryRequestString = request
            iTunesApi.getMusicList(request).enqueue(object : Callback<MusicList> {
                override fun onResponse(
                    call: Call<MusicList>,
                    response: retrofit2.Response<MusicList>
                ) {
                    if ((response.body()?.resultCount ?: 0) > 0 && response.code() == 200) {
                        callback(response.body()!!.results)
                    } else if (response.code() != 200)
                        errorCallback(MusicTrackAdapter.SEARCH_NOT_CALL,true)
                    else
                        errorCallback(MusicTrackAdapter.SEARCH_NOT_TRACK,false)
                }

                override fun onFailure(call: Call<MusicList>, t: Throwable) {
                    errorConnectionCallback()
                }
            })
        }
    }

    fun load() {
        load(temporaryRequestString, searchHistory = searchHist, callback = callbackSevice, errorCallback = errorCallbackSevice, errorConnectionCallback = errorConnectionCallbackSevice)
    }
}