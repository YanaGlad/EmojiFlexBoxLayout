package com.example.emoji.viewState.elm.users

import android.os.Parcelable
import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import kotlinx.parcelize.Parcelize

/**
 * @author y.gladkikh
 */
@Parcelize
data class UserState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val model: User? = null,
    val list: List<User> = listOf(),
    val presence: Presence? = null,
    val userId: Int = 0,
) : Parcelable

sealed class UserEvent {
    sealed class UI : UserEvent() {
        object Stub : UI()
    }

    sealed class Internal : UserEvent() {
        object AllUsersLoading : UserEvent.Internal()
        class AllUsersLoaded(val list: List<User>) : UserEvent.Internal()
        object MyUserLoading : UserEvent.Internal()
        class MyUserLoaded(val user: User) : UserEvent.Internal()
        data class ErrorLoading(val error: Throwable?) : UserEvent.Internal()
        object SuccessOperation : UserEvent.Internal()
        class PresenceLoaded(val presence: Presence?) : UserEvent.Internal()
    }
}

sealed class Effect {
    data class ShowErrorSnackBar(val erorr: Throwable) : Effect()
}

sealed class UserCommand {
    object LoadAllUsers : UserCommand()
    object LoadMyUser : UserCommand()
    class LoadPresence(val userId: Int) : UserCommand()
}

sealed class UserEffect {

}