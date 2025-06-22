package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.databinding.FragmentMediaLibraryHostBinding
import com.example.playlistmaker.media_library.ui.adapters.MediaLibraryAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentContainer:BindingFragment<FragmentMediaLibraryHostBinding>() {
    private val mediator:TabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout,binding.pager){tab,position->
            when(position){
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaLibraryHostBinding {
        return FragmentMediaLibraryHostBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = MediaLibraryAdapter(this)
        mediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediator.detach()
    }
    companion object{
        fun getInstance() = FragmentContainer()
    }
}