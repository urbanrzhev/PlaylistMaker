package com.example.playlistmaker.media_library.ui.fragments

import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.example.playlistmaker.media_library.ui.adapters.MediaLibraryAdapter
import com.example.playlistmaker.media_library.ui.view_model.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {
    private val viewModel: MediaLibraryViewModel by viewModel()
    private lateinit var mediator: TabLayoutMediator
    private lateinit var adapter: MediaLibraryAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaLibraryBinding {
        return FragmentMediaLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeStartFragment().observe(viewLifecycleOwner) {
            binding.pager.setCurrentItem(it)
        }
        viewModel.startFragment()
        adapter = MediaLibraryAdapter(childFragmentManager, lifecycle)
        binding.pager.adapter = adapter

        mediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        mediator.attach()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setStartFragment(binding.pager.currentItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }
}