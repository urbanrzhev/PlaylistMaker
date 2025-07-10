package com.example.playlistmaker.media_library.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.media_library.ui.view_model.FavoritesTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : BindingFragment<FragmentFavoritesTracksBinding>() {
    private val viewModel: FavoritesTracksViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesTracksBinding {
        return FragmentFavoritesTracksBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}