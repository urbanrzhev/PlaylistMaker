package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

class MusicTrackAdapter(
    private val track: List<Track>,
    private val sign: Int = 0,
    private val text:String = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (sign) {
            0 -> MusicTrackViewHolder(parent)
            1 -> MusicNotTrackViewHolder(parent)
            2 -> MusicNotCallViewHolder(parent,text)
            3 -> MusicNotTrackViewHolder(parent, empty = true)
            else -> throw IllegalStateException(parent.context.getString(R.string.illegal_state_exception))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MusicTrackViewHolder -> holder.bind(track[position])
        }
    }

    override fun getItemCount(): Int {
        return track.size
    }
}

class MusicNotTrackViewHolder(parent: ViewGroup,empty:Boolean = false) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.nothing_for_search, parent, false)
){
    init {
        if(empty){
            val main = itemView.findViewById<LinearLayout>(R.id.main)
            main.visibility = View.INVISIBLE
        }
    }
}

class MusicNotCallViewHolder(parent: ViewGroup,text:String) : ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.load_error_for_search, parent, false)
) {
    init {
        val updateSearch = itemView.findViewById<Button>(R.id.updateSearch)
        val textNotFound = itemView.findViewById<TextView>(R.id.textNotFound)
        textNotFound.text = text
        updateSearch.setOnClickListener {
            ITunesService.load(parent as RecyclerView)
        }
    }

}

class MusicTrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_list_for_search, parent, false)
) {

    private val trackName: TextView = itemView.findViewById(R.id.textView1)
    private val artistName: TextView = itemView.findViewById(R.id.textView2)
    private val trackTime: TextView = itemView.findViewById(R.id.textView3)
    private val imageCover: ImageView = itemView.findViewById(R.id.imageViewCover)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        try {
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .transform(RoundedCorners(dpToPx(2f, itemView.context)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search_light)
                .into(imageCover)
        }catch (e:Exception){
            Toast.makeText(itemView.context, itemView.context.getString(R.string.crash_error), Toast.LENGTH_SHORT).show()
        }
    }
}

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
)
