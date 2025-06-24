package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.media_library.util.BindingFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}