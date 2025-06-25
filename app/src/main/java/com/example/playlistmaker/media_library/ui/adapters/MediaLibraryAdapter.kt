package com.example.playlistmaker.media_library.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media_library.ui.fragments.FavoritesTracksFragment
import com.example.playlistmaker.media_library.ui.fragments.PlaylistsFragment

class MediaLibraryAdapter(hostFragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(hostFragment, lifecycle) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesTracksFragment.newInstance() else PlaylistsFragment.newInstance()
    }

}