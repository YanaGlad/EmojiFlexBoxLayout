package com.example.emoji.viewState.elm.stream

import com.example.emoji.viewState.elm.laod.LoadStream
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

/**
 * @author y.gladkikh
 */
class StreamActor constructor(
    private val loadStreams: LoadStream,
) : ActorCompat<StreamCommand, StreamEvent.Internal> {

    override fun execute(command: StreamCommand): Observable<StreamEvent.Internal> {
        when (command) {
            is StreamCommand.LoadAllStreams -> {
                return loadStreams.getStreams()
                    .mapEvents(
                        { model -> StreamEvent.Internal.PageLoaded(model) },
                        { error -> StreamEvent.Internal.ErrorLoading(error) }
                    )
            }
        }
    }
}