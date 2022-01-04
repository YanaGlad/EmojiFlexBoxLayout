package com.example.emoji.viewState.elm.stream

import com.example.emoji.repository.StreamRepository
import com.example.emoji.viewState.elm.laod.LoadStream
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class StreamGlobalDI @Inject constructor(val repository: StreamRepository) {

    private val loadStreams by lazy { LoadStream(repository) }

    private val actor by lazy { StreamActor(loadStreams) }

    val elmStoreFactory by lazy { StreamStore(actor) }
}