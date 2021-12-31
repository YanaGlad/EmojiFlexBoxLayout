package com.example.emoji.repository

import com.example.emoji.api.model.Reaction
import io.reactivex.Completable
import io.reactivex.Single

interface ReactionRepository {
    fun addReaction(messageId: Int, name: String): Single<Reaction>
    fun removeReaction(id: Int, reactionName: String): Completable
    fun getReactionsFromServer(): Single<List<Reaction>>

}
