package com.example.emoji.viewState.elm.stream

import android.os.Parcelable
import com.example.emoji.model.StreamModel
import kotlinx.parcelize.Parcelize

/**
 * @author y.gladkikh
 */
@Parcelize
data class StreamState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val model: StreamModel? = null,
) : Parcelable

sealed class StreamEvent {
    sealed class UI : StreamEvent() {
        object Stub : UI()
    }

    sealed class Internal : StreamEvent() {
        class PageLoading() : StreamEvent.Internal()
        class PageLoaded(val model: StreamModel) : StreamEvent.Internal()
        data class ErrorLoading(val error: Throwable?) : StreamEvent.Internal()
        class SuccessOperation() : StreamEvent.Internal()
    }
}

sealed class Effect {
    data class ShowErrorSnackBar(val erorr: Throwable) : Effect()
}

sealed class StreamCommand {
    class StreamsLoaded() : StreamCommand()
}

sealed class StreamEffect {

}