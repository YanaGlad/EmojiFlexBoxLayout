package com.example.emoji.viewState.elm.users

import com.example.emoji.viewState.elm.laod.LoadUsers
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

/**
 * @author y.gladkikh
 */
class UserActor constructor(
    private val loadUsers: LoadUsers,
) : ActorCompat<UserCommand, UserEvent.Internal> {

    override fun execute(command: UserCommand): Observable<UserEvent.Internal> {
        when (command) {
            is UserCommand.LoadAllUsers -> {
                return loadUsers.getAllUsers()
                    .mapEvents(
                        { list -> UserEvent.Internal.AllUsersLoaded(list) },
                        { error -> UserEvent.Internal.ErrorLoading(error) }
                    )
            }
            is UserCommand.LoadMyUser -> {
                return loadUsers.getMyUser()
                    .mapEvents(
                        { user -> UserEvent.Internal.MyUserLoaded(user) },
                        { error -> UserEvent.Internal.ErrorLoading(error) }
                    )
            }
            is UserCommand.LoadPresence -> {
                return loadUsers.getUserPresence(command.userId)
                    .mapEvents(
                        { presence -> UserEvent.Internal.PresenceLoaded(presence) },
                        { error -> UserEvent.Internal.ErrorLoading(error) }
                    )
            }
        }
    }
}
