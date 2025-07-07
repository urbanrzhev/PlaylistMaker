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
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {
    private val viewModel: MediaLibraryViewModel by viewModel()
        //private lateinit var mediator: TabLayoutMediator
    //private lateinit var adapter: MediaLibraryAdapter
    val mediator:TabLayoutMediator by lazy{
            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorites_tracks)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
        }
    val adapter:MediaLibraryAdapter by lazy{
        MediaLibraryAdapter(childFragmentManager, lifecycle)
    }
    private lateinit var pageChangeCallback:ViewPager2.OnPageChangeCallback

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
        /*adapter = MediaLibraryAdapter(childFragmentManager, lifecycle)*/
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setStartFragment(binding.pager.currentItem)
            }
        }
        binding.pager.adapter = adapter
        binding.pager.registerOnPageChangeCallback(pageChangeCallback)

        /*mediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites_tracks)
                    1 -> tab.text = getString(R.string.playlists)
            }
        }*/
        mediator.attach()
    }

    override fun onStop() {
        super.onStop()
        binding.pager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }
}