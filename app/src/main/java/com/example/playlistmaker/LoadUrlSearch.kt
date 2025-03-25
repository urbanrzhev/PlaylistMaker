package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.recyclerview.widget.RecyclerView

object EmptyList {
    val track: Track = Track(
        "",
        "",
        "",
        "",
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
    fun load(request: String, recycler: RecyclerView) {
        if(request.isNotEmpty()) {
            temporaryRequestString = request
            catApi.getMusicList(request).enqueue(object : Callback<MusicList> {
                override fun onResponse(
                    call: Call<MusicList>,
                    response: retrofit2.Response<MusicList>
                ) {
                    if ((response.body()?.resultCount ?: 0) > 0 && response.code() == 200)
                        recycler.adapter = MusicTrackAdapter(response.body()!!.results)
                    else if (response.code() != 200)
                        recycler.adapter = MusicTrackAdapter(
                            listOf(EmptyList.track),
                            sign = 2,
                            text = recycler.context.getString(R.string.load_error_two_for_search)
                        )
                    else
                        recycler.adapter = MusicTrackAdapter(listOf(EmptyList.track), sign = 1)
                }

                override fun onFailure(call: Call<MusicList>, t: Throwable) {
                    recycler.adapter = MusicTrackAdapter(
                        listOf(EmptyList.track),
                        sign = 2,
                        text = recycler.context.getString(R.string.load_error_one_for_search)
                    )
                }

            })
        }
    }

    fun load(recycler: RecyclerView) {
        if (temporaryRequestString.isNotEmpty())
            load(temporaryRequestString, recycler)
    }
}
