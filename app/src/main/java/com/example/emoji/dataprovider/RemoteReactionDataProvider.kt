package com.example.emoji.dataprovider

import com.example.emoji.api.model.Reaction
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteReactionDataProvider {
    fun addMessageReaction(id: Int, reactionName: String): Single<Reaction>
    fun removeMessageReaction(id: Int, reactionName: String): Completable
    fun getReactionsFromServer(): Single<List<Reaction>>
}