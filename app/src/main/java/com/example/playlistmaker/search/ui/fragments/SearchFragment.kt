package com.example.playlistmaker.search.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.ui.adapters_holders.TracksAdapter
import com.example.playlistmaker.search.ui.adapters_holders.TracksHistoryAdapter
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchBinding.inflate(layoutInflater, container, false)
        binding.recyclerSearch.adapter = adapterSearch
        binding.recyclerSearchHistory.adapter = adapterHistory
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeHistory().observe(viewLifecycleOwner) {
            adapterHistory.setTrackList(it)
        }
        viewModel.observerProgressBarLiveData().observe(viewLifecycleOwner) {
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
                context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.clearSearchButton.windowToken,
                0
            )
        }

        binding.toolbarSearch.setNavigationOnClickListener {
            //onBackPressedDispatcher.onBackPressed()
        }
        adapterHistory.setTrackList(viewModel.getHistory())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStartActivity()
    }

    override fun onDestroy() {
        textWatcher.let { binding.editSearchText.removeTextChangedListener(it) }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.secret_code), temporaryEditText)
    }

    /*override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textEdit = savedInstanceState.getString(getString(R.string.secret_code))
        binding.editSearchText.setText(textEdit)
    }*/

    private fun render(searchState: SearchState<List<Track>>) {
        when (searchState) {
            is SearchState.Loaded -> {
                showProgressBar(true)
            }

            is SearchState.Empty -> {
                emptyLoadTracks()
            }

            is SearchState.Success -> {
                successLoadTracks(searchState.data)
            }

            is SearchState.Error -> {
                errorLoadTracks(searchState.message)
            }
        }
    }

    private fun errorLoadTracks(message: Int) {
        showProgressBar(false)
        binding.viewGroupHistory.isVisible = false
        binding.notCall.isVisible = true
        binding.textNotCall.text = getString(message)
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
            /*val intent = Intent(this, MediaPlayerActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            startActivity(intent)*/
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