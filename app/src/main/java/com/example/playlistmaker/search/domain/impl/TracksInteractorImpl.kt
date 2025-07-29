package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.ui.models.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {
    override suspend fun searchTracksSuspend(expression: String): Flow<SearchState<List<Track>>> {
        return repository.searchTracksSuspend(expression).map {
            when (it) {
                is Resource.Success -> {
                    if (it.data.isNotEmpty())
                        SearchState.Success(it.data)
                    else
                        SearchState.Empty()
                }

                is Resource.Error -> {
                    SearchState.Error(
                        it.message
                    )
                }
            }
        }
    }
}