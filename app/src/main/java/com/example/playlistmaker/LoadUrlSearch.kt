package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName

data class Track(
   @SerializedName("trackName") val trackName: String,
   @SerializedName("artistName") val artistName: String,
   @SerializedName("trackTimeMillis") val trackTimeMillis: String,
   @SerializedName("artworkUrl100") val artworkUrl100: String,
   val collectionName:String,
   val releaseDate:String,
   val primaryGenreName:String,
   val country:String,
   val trackId: Int
)

object EmptyList {
    val track: Track = Track(
        "wertwret",
        "wreterw",
        "97000",
        "",
        "",
        "",
        "",
        "",
        0
    )
}

data class MusicList(
    val resultCount: Int,
    val results: List<Track>
)

interface InterfaceITunes {
    @GET("/search?entity=song")
    fun getMusicList(@Query("term") text: String): Call<MusicList>
}

object ITunesService {
    var temporaryRequestString: String = ""
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val catApi = retrofit.create(InterfaceITunes::class.java)
    var searchHist:SearchHistory? = null
    fun load(request: String, recycler: RecyclerView, searchHistory: SearchHistory?) {
        searchHist = searchHistory
        if(request.isNotEmpty()) {
            temporaryRequestString = request
            catApi.getMusicList(request).enqueue(object : Callback<MusicList> {
                override fun onResponse(
                    call: Call<MusicList>,
                    response: retrofit2.Response<MusicList>
                ) {
                    if ((response.body()?.resultCount ?: 0) > 0 && response.code() == 200) {
                        recycler.adapter = MusicTrackAdapter(response.body()!!.results, searchHistory = searchHist)
                    }else if (response.code() != 200)
                        recycler.adapter = MusicTrackAdapter(
                            listOf(EmptyList.track),
                            sign = MusicTrackAdapter.SEARCH_NOT_CALL,
                            text = recycler.context.getString(R.string.load_error_two_for_search)
                        )
                    else
                        recycler.adapter = MusicTrackAdapter(listOf(EmptyList.track), sign = MusicTrackAdapter.SEARCH_NOT_TRACK)
                }

                override fun onFailure(call: Call<MusicList>, t: Throwable) {
                    recycler.adapter = MusicTrackAdapter(
                        listOf(EmptyList.track),
                        sign = MusicTrackAdapter.SEARCH_NOT_CALL,
                        text = recycler.context.getString(R.string.load_error_one_for_search)
                    )
                }

            })
        }
    }

    fun load(recycler: RecyclerView) {
        if (temporaryRequestString.isNotEmpty())
            load(temporaryRequestString, recycler, searchHist)
    }

}
