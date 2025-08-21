package com.example.playlistmaker.search.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.common.ui.adapter_holder.TracksAdapter
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: FragmentSearchBinding
    private var temporaryEditText = ""
    private var textWatcher: TextWatcher? = null
    private var isClickAllowed = true
    private val searchAdapter = TracksAdapter {
        addTrackHistory(it)
        goAudioPlayer(it)
    }
    private val historyAdapter = TracksAdapter {
        viewModel.visibleHistory()
        goAudioPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isClickAllowed = true
        binding.recyclerSearch.adapter = searchAdapter
        binding.recyclerSearchHistory.adapter = historyAdapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeHistory().observe(viewLifecycleOwner) {
            historyAdapter.updateList(it)
        }
        viewModel.observeFocusEditTextLiveData().observe(viewLifecycleOwner) {
            if (it) {
                binding.editSearchText.requestFocus()
            }
        }
        viewModel.observeTemporaryEditTextLiveData().observe(viewLifecycleOwner) {
            temporaryEditText = it
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearTrackListHistory()
            historyAdapter.updateList(mutableListOf())
            binding.viewGroupHistory.isVisible = false
        }
        binding.editSearchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editSearchText.text.isEmpty() && historyAdapter.itemCount > 0) {
                viewModel.visibleHistory()
                viewModel.setFocusEditText(hasFocus)
            }
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchButton.isVisible = !s.isNullOrEmpty()
                viewModel.setTemporaryEditText(s.toString())
                if (binding.editSearchText.hasFocus() && binding.editSearchText.text.trim()
                        .isEmpty() && historyAdapter.itemCount > 0
                ) {
                    viewModel.visibleHistory()
                }
                if (s.toString().trim().isNotEmpty()) {
                    viewModel.searchDebounce(s.toString())
                } else {
                    viewModel.clearTemporaryTextRequest()
                    viewModel.clearSearchDebounce()
                    binding.recyclerSearch.adapter = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher.let { binding.editSearchText.addTextChangedListener(it) }

        binding.clearSearchButton.setOnClickListener {
            viewModel.visibleHistory()
            binding.editSearchText.text = null
            val inputMethodManager =
                requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.clearSearchButton.windowToken,
                0
            )
        }
        viewModel.updateListsRecycler()
    }

    override fun onResume() {
        super.onResume()
        binding.editSearchText.setText(temporaryEditText)
        binding.editSearchText.setSelection(binding.editSearchText.text.length)
    }

    override fun onDestroy() {
        textWatcher?.let { binding.editSearchText.removeTextChangedListener(it) }
        super.onDestroy()
    }

    private fun render(searchState: SearchState<List<Track>>) {
        when (searchState) {
            is SearchState.History -> {
                showProgressBar(false)
                showHistory()
            }

            is SearchState.Loaded -> {
                showProgressBar(true)
            }

            is SearchState.Idle -> {
                showProgressBar(false)
                emptyLoadTracks()
            }

            is SearchState.Success -> {
                showProgressBar(false)
                successLoadTracks(searchState.data)
            }

            is SearchState.Error -> {
                showProgressBar(false)
                errorLoadTracks(searchState.message)
            }
        }
    }

    private fun errorLoadTracks(message: Int) {
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
        binding.notCall.isVisible = false
        binding.notTrack.isVisible = false
        binding.viewGroupHistory.isVisible = false
        searchAdapter.updateList(list)
        binding.recyclerSearch.adapter = searchAdapter
    }

    private fun goAudioPlayer(track: Track) {
        if (clickDebounce()) {
            findNavController().navigate(R.id.action_searchFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track))
        }
    }

    private fun addTrackHistory(track: Track) {
        viewModel.addTrackHistory(track)
    }

    private fun showProgressBar(value: Boolean) {
        binding.progressCircular.isVisible = value
    }

    private fun showHistory() {
        binding.notCall.isVisible = false
        binding.notTrack.isVisible = false
        if (historyAdapter.itemCount > 0)
            binding.viewGroupHistory.isVisible = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        isClickAllowed = false
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}