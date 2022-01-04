package com.example.emoji.viewState.elm.users

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

/**
 * @author y.gladkikh
 */
class UserReducer : ScreenDslReducer<UserEvent, UserEvent.UI, UserEvent.Internal, UserState,
        UserEffect, UserCommand>(UserEvent.UI::class, UserEvent.Internal::class) {

    override fun Result.internal(event: UserEvent.Internal): Any {
        return when (event) {
            is UserEvent.Internal.ErrorLoading -> {
                state { copy(error = event.error, isLoading = false) }

            }
            is UserEvent.Internal.AllUsersLoaded -> {
                state { copy(isLoading = false, list = event.list) }

            }
            is UserEvent.Internal.AllUsersLoading -> {
                state { copy(isLoading = true) }
                commands {
                    +UserCommand.LoadAllUsers
                }
            }
            is UserEvent.Internal.SuccessOperation -> {
                state { copy(isLoading = false) }
            }
            is UserEvent.Internal.MyUserLoaded -> {
                state { copy(isLoading = false, model = event.user, userId = event.user.id) }
                commands {
                    +UserCommand.LoadPresence(state.userId)
                }
            }
            is UserEvent.Internal.MyUserLoading -> {
                state { copy(isLoading = true) }
                commands {
                    +UserCommand.LoadMyUser
                }
            }
            is UserEvent.Internal.PresenceLoaded -> {
                state { copy(isLoading = false, presence = event.presence) }
            }
        }
    }

    override fun Result.ui(event: UserEvent.UI): Any? {
        TODO("Not yet implemented")
    }
}