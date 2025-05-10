package com.example.playlistmaker.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryTrackListInteraсtor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.domain.util.MyConstants
import com.example.playlistmaker.domain.util.MyConstants.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.presentation.TracksAdapter
import com.example.playlistmaker.presentation.TracksHistoryAdapter
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var temporaryEditText = ""
    private var temporaryTextRequest = ""
    private var isClickAllowed = true
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var progressBar: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private val clickItemAdapter: (Track, Boolean) -> Unit = { track, history ->
        if (!history) {
            addHistoryTrackList(track)
        }
        if (isClickAllowed) {
            goIntent()
            setActiveTrackForMediaPlayer(track)
            clickDebounce()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = if (ime.bottom > 0) ime.bottom else systemBars.bottom
            v.setPadding(systemBars.left, systemBars.top + ime.top, systemBars.right, bottomPadding)
            insets
        }
        val buttonSettingsBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val buttonClearSearch = findViewById<ImageView>(R.id.clearSearchButton)
        val editSearch = findViewById<EditText>(R.id.editSearchText)
        val viewGroupHistory = findViewById<LinearLayout>(R.id.viewGroupHistory)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerSearch)
        recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerSearchHistory)
        progressBar = findViewById<FrameLayout>(R.id.progress_circular)
        runnable = Runnable { searchRequest(editSearch.text.toString()) }
        initHistoryAdapter()

        clearHistoryButton.setOnClickListener {
            clearHistoryTrackList()
            viewGroupHistory.isVisible = false
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.isVisible =
                hasFocus && editSearch.text.isEmpty() && countHistoryTrackList() > 0
        }

        clearHistoryButton.setOnClickListener {
            clearHistoryTrackList()
            viewGroupHistory.isVisible = false
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.isVisible =
                hasFocus && editSearch.text.isEmpty() && countHistoryTrackList() > 0
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temporaryEditText = s.toString()
                buttonClearSearch.isVisible = !s.isNullOrEmpty()
                viewGroupHistory.isVisible =
                    editSearch.hasFocus() && editSearch.text.isEmpty() && countHistoryTrackList() > 0
                if (s.toString().isNotEmpty())
                    searchDebounce(runnable)
                else recyclerView.adapter = null
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editSearch.addTextChangedListener(simpleTextWatcher)
        buttonClearSearch.setOnClickListener {
            editSearch.text = null
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                buttonClearSearch.windowToken,
                0
            )
        }

        buttonSettingsBack.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.secret_code), temporaryEditText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textEdit = savedInstanceState.getString(getString(R.string.secret_code))
        findViewById<EditText>(R.id.editSearchText).setText(textEdit)
    }

    private fun searchDebounce(runnable: Runnable) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, MyConstants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(
        requestText: String
    ) {
        if (requestText.isNotEmpty()) {
            temporaryTextRequest = requestText
            progressBar.isVisible = true
            searchTracks(requestText)
        }
    }

    private fun setAdapter(data: TrackData) {
        handler.post(Runnable {
            progressBar.isVisible = false
            val adapter: TracksAdapter =
                when (data.resultCodeResponse) {
                    200 -> {
                        TracksAdapter(
                            data.results,
                            sign = if (data.resultCount > 0) TracksAdapter.SEARCH_COMPLETED else TracksAdapter.SEARCH_NOT_TRACK,
                            clickListener = clickItemAdapter
                        )
                    }

                    in 500..509, 404 -> {
                        TracksAdapter(
                            data.results,
                            sign = TracksAdapter.SEARCH_NOT_CALL,
                            text = getString(R.string.load_error_two_for_search),
                            callbackReloadRequest = {
                                searchDebounce {
                                    searchRequest(temporaryTextRequest)
                                }
                            }
                        )
                    }

                    else -> {
                        TracksAdapter(
                            data.results,
                            sign = TracksAdapter.SEARCH_NOT_CALL,
                            text = getString(R.string.load_error_one_for_search),
                            callbackReloadRequest = {
                                searchDebounce {
                                    searchRequest(temporaryTextRequest)
                                }
                            }
                        )
                    }
                }
            recyclerView.adapter = adapter
        })
    }

    private fun clickDebounce() {
        isClickAllowed = false
        Handler(Looper.getMainLooper())
            .postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
    }

    private fun goIntent() {
        val intent = Intent(this, MediaLibraryActivity::class.java)
        this.startActivity(intent)
    }

    private fun countHistoryTrackList(): Int {
        return Creator.provideHistoryTrackListInteractor(this).count()
    }

    private fun addHistoryTrackList(track: Track?) {
        Creator.provideHistoryTrackListInteractor(this)
            .addTrack(track, HistoryTrackListInteraсtor.Consumer {
                recyclerViewHistory.adapter = TracksHistoryAdapter(it, clickItemAdapter)
            })
    }

    private fun initHistoryAdapter() {
        addHistoryTrackList(null)
    }

    private fun clearHistoryTrackList() {
        Creator.provideHistoryTrackListInteractor(this).clear()
    }

    private fun searchTracks(requestText: String) {
        Creator.provideTracksInteractor()
            .searchTracks(requestText, TracksInteractor.TracksConsumer {
                setAdapter(it)
            })
    }

    private fun setActiveTrackForMediaPlayer(track: Track) {
        Creator.provideSharedPreferencesInteractor(this).setActiveTrackForMediaPlayer(
            track
        )
    }
}