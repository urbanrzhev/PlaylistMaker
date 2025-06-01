package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.main.ui.activity.MainActivity
import com.example.playlistmaker.search.ui.adapters_holders.TracksAdapter
import com.example.playlistmaker.search.ui.adapters_holders.TracksHistoryAdapter
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.State
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private var temporaryEditText = ""
    private lateinit var textWatcher: TextWatcher
    private var isClickAllowed = true
    private val adapterSearch = TracksAdapter {
        addTrackHistory(it)
        goAudioPlayer(it)
    }
    private val adapterHistory = TracksHistoryAdapter {
        goAudioPlayer(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        viewModel =
            ViewModelProvider(
                this,
                SearchViewModel.getViewModelFactory()
            )[SearchViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = if (ime.bottom > 0) ime.bottom else systemBars.bottom
            v.setPadding(systemBars.left, systemBars.top + ime.top, systemBars.right, bottomPadding)
            insets
        }
        binding.recyclerSearch.adapter = adapterSearch
        binding.recyclerSearchHistory.adapter = adapterHistory
        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observeHistory().observe(this) {
            adapterHistory.setTrackList(it)
        }
        viewModel.observerProgressBarLiveData().observe(this) {
            showProgressBar(it)
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearTrackListHistory()
            binding.viewGroupHistory.isVisible = false
        }

        binding.editSearchText.setOnFocusChangeListener { _, hasFocus ->
            binding.viewGroupHistory.isVisible =
                hasFocus && binding.editSearchText.text.isEmpty() && adapterHistory.itemCount > 0
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temporaryEditText = s.toString()
                binding.clearSearchButton.isVisible = !s.isNullOrEmpty()
                if (binding.editSearchText.hasFocus() && binding.editSearchText.text.isEmpty() && adapterHistory.itemCount > 0)
                    showHistory()
                if (s.toString().isNotEmpty()) {
                    viewModel.searchDebounce(s.toString())
                } else {
                    viewModel.clearSearchDebounce()
                    binding.recyclerSearch.adapter = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher.let { binding.editSearchText.addTextChangedListener(it) }

        binding.clearSearchButton.setOnClickListener {
            binding.editSearchText.text = null
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.clearSearchButton.windowToken,
                0
            )
        }

        binding.toolbarSearch.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        adapterHistory.setTrackList(viewModel.getHistory())
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher.let { binding.editSearchText.removeTextChangedListener(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.secret_code), temporaryEditText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textEdit = savedInstanceState.getString(getString(R.string.secret_code))
        binding.editSearchText.setText(textEdit)
    }

    private fun render(state: State<List<Track>>) {
        when (state) {
            is State.Else -> {
            }

            is State.Empty -> {
                emptyLoadTracks()
            }

            is State.Success -> {
                successLoadTracks(state.data)
            }

            is State.Error -> {
                errorLoadTracks(state.message)
            }
        }
    }

    private fun errorLoadTracks(value: String) {
        showProgressBar(false)
        binding.viewGroupHistory.isVisible = false
        binding.notCall.isVisible = true
        binding.textNotCall.text = value
        binding.notTrack.isVisible = false
        binding.notCallButton.setOnClickListener {
            viewModel.searchDebounce()
        }
    }

    private fun emptyLoadTracks() {
        binding.notCall.isVisible = false
        binding.notTrack.isVisible = true
    }

    private fun successLoadTracks(list: List<Track>) {
        showProgressBar(false)
        binding.notCall.isVisible = false
        binding.notTrack.isVisible = false
        binding.viewGroupHistory.isVisible = false
        adapterSearch.setTrackList(list)
        binding.recyclerSearch.adapter = adapterSearch
    }

    private fun goAudioPlayer(track: Track) {
        if (!clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            startActivity(intent)
            setActiveTrack(track)
        }
    }

    private fun addTrackHistory(track: Track) {
        viewModel.addTrackHistory(track)
    }

    private fun showProgressBar(value: Boolean) {
        binding.progressCircular.isVisible = value
    }

    private fun showHistory() {
        showProgressBar(false)
        binding.notCall.isVisible = false
        binding.notTrack.isVisible = false
        binding.viewGroupHistory.isVisible = true
    }

    private fun setActiveTrack(track: Track) {
        viewModel.setActiveTrack(track)
    }

    private fun clickDebounce(): Boolean {
        isClickAllowed = false
        handler.postDelayed({
            isClickAllowed = true
        }, CLICK_DEBOUNCE_DELAY)
        return isClickAllowed
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}