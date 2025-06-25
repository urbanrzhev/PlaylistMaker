package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.State
import java.util.concurrent.Executor

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: Executor
) :TracksInteractor {
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val data = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    if (data.data.isNotEmpty())
                        consumer.consume(State.Success(data.data))
                    else
                        consumer.consume(State.Empty())
                }

                is Resource.Error -> {
                    consumer.consume(
                        State.Error(
                            data.message
                        )
                    )
                }
            }
        }
    }
}