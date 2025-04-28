package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

private const val CLICK_DEBOUNCE_DELAY = 1000L
fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

class MusicHistoryTrackAdapter(
    private val track: MutableList<Track>
) : RecyclerView.Adapter<MusicTrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicTrackViewHolder {
        return MusicTrackViewHolder(parent, history = true)
    }

    override fun getItemCount(): Int {
        return track.size
    }

    override fun onBindViewHolder(holder: MusicTrackViewHolder, position: Int) {
        holder.bind(track[position])
    }
}

class MusicTrackAdapter(
    private val track: List<Track>,
    private val sign: Int = 0,
    private val text: String = "",
    private val searchHistory: SearchHistory? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val SEARCH_NOT_TRACK: Int = 1
        const val SEARCH_NOT_CALL: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (sign) {
            0 -> MusicTrackViewHolder(parent, history = false, searchHistory = searchHistory)
            1 -> MusicNotTrackViewHolder(parent)
            2 -> MusicNotCallViewHolder(parent, text)
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

class MusicNotTrackViewHolder(parent: ViewGroup, empty: Boolean = false) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.nothing_for_search, parent, false)
) {
    init {
        if (empty) {
            val main = itemView.findViewById<LinearLayout>(R.id.main)
            main.isVisible = false
        }
    }
}

class MusicNotCallViewHolder(parent: ViewGroup, text: String) : ViewHolder(
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

class MusicTrackViewHolder(
    parent: ViewGroup,
    val history: Boolean = false,
    val searchHistory: SearchHistory? = null
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_list_for_search, parent, false)
) {
    private var isClickAllowed = true
    private val trackName: TextView = itemView.findViewById(R.id.textView1)
    private val artistName: TextView = itemView.findViewById(R.id.textView2)
    private val trackTime: TextView = itemView.findViewById(R.id.textView3)
    private val imageCover: ImageView = itemView.findViewById(R.id.imageViewCover)

    fun bind(model: Track) {
        itemView.setOnClickListener {
            if (!history)
                searchHistory?.addHistory(model)
        }
        itemView.setOnClickListener {
            if (!history)
                searchHistory?.addHistory(model)
            if (isClickAllowed) {
                goIntent()
                trackActive = model
                clickDebounce()
            }
        }
        trackName.text = model.trackName
        artistName.text = model.artistName
        try {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
            Glide.with(itemView.context)
                .load(model.artworkUrl100)
                .transform(RoundedCorners(dpToPx(2f, itemView.context)))
                .fitCenter()
                .placeholder(R.drawable.placeholder_search)
                .into(imageCover)
        } catch (e: Exception) {
            Toast.makeText(
                itemView.context,
                itemView.context.getString(R.string.crash_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun goIntent(){
        val intent = Intent(itemView.context, MediaLibraryActivity::class.java)
        itemView.context.startActivity(intent)
    }
    private fun clickDebounce(){
        isClickAllowed = false
        Handler(Looper.getMainLooper())
            .postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
    }
}


