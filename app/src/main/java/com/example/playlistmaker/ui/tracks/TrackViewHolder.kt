package com.example.playlistmaker.ui.tracks

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.history.TracksHistoryManager
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.MediaLibraryActivity
import com.example.playlistmaker.ui.player.trackActive

private const val SEARCH_DEBOUNCE_DELAY = 2000L

private const val CLICK_DEBOUNCE_DELAY = 1000L
fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

class TrackViewHolder(
    private val parent: ViewGroup,
    private val history: Boolean = false,
    private val callbackRefreshHistory:()->Unit = {}
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_list_for_search, parent, false)
) {
    private val addTrackInHistory = TracksHistoryManager(parent.context)
    private var isClickAllowed = true
    private val trackName: TextView = itemView.findViewById(R.id.textView1)
    private val artistName: TextView = itemView.findViewById(R.id.textView2)
    private val trackTime: TextView = itemView.findViewById(R.id.textView3)
    private val imageCover: ImageView = itemView.findViewById(R.id.imageViewCover)

    fun bind(model: Track) {
        itemView.setOnClickListener {
            if (!history) {
                addTrackInHistory.addHistory(model)
                callbackRefreshHistory()
            }
            if (isClickAllowed) {
                goIntent()
                trackActive = model
                clickDebounce()
            }
        }
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeNormal
        try {
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

    private fun goIntent() {
        val intent = Intent(itemView.context, MediaLibraryActivity::class.java)
        itemView.context.startActivity(intent)
    }

    private fun clickDebounce() {
        isClickAllowed = false
        Handler(Looper.getMainLooper())
            .postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
    }
}