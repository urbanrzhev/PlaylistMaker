package com.example.playlistmaker.media_library.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media_library.ui.fragments.FragmentFavoritesTracks
import com.example.playlistmaker.media_library.ui.fragments.FragmentPlaylists

class MediaLibraryAdapter(hostFragment: Fragment)
     : FragmentStateAdapter(hostFragment) {

        // Тут указываем то количество элементов, которое хотим увидеть в нашем ViewPager
        override fun getItemCount(): Int = 2

        /**
         * Тут можно возвращать фрагменты одного класса, а можно абсолютно разные. Надо смотреть на
         * конкретную задачу. Главное подумать, какой фрагмент вы хотите видеть под определённой
         * позицией
         *
         * Если мы используем один фрагмент для всех страниц, то можно передавать внутрь него position,
         * как argument. И в зависимости от неё как-то менять интерфейс фрагмента
         */
        override fun createFragment(position: Int): Fragment {
            return if (position == 0) FragmentFavoritesTracks.newInstance() else FragmentPlaylists.newInstance()
        }

}