package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.adapters_holder.TrackAdapter
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.media_library.ui.models.RecyclerState
import com.example.playlistmaker.media_library.ui.view_model.FavoritesTracksViewModel
import com.example.playlistmaker.player.ui.fragments.MediaPlayerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : BindingFragment<FragmentFavoritesTracksBinding>() {
    private val viewModel: FavoritesTracksViewModel by viewModel()
    private var isClickAllowed = true
    private val adapter = TrackAdapter {
            goAudioPlayer(it)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesTracksBinding {
        return FragmentFavoritesTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isClickAllowed = true
        binding.recycler.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is RecyclerState.Success -> {
                    renderUi(false)
                    adapter.updateList(it.data)
                }
                is RecyclerState.Idle -> {
                    renderUi(true)
                }
            }
        }
        viewModel.updateFavoritesTracks()
    }

    private fun goAudioPlayer(track: Track) {
        if (clickDebounce()) {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track)
            )
        }
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

    private fun renderUi(value: Boolean) {
        binding.group.isVisible = value
        binding.recycler.isVisible = !value
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoritesTracksFragment()
    }
}