package com.example.emoji.repository

import com.example.emoji.api.model.Reaction
import com.example.emoji.dataprovider.RemoteReactionDataProvider
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val remote: RemoteReactionDataProvider,
) : ReactionRepository {

    override fun addReaction(messageId: Int, name: String): Single<Reaction> {
        return remote.addMessageReaction(messageId, name)
    }

    override fun removeReaction(id: Int, reactionName: String): Completable {
        return remote.removeMessageReaction(id, reactionName)
    }

    override fun getReactionsFromServer(): Single<List<Reaction>> {
        return remote.getReactionsFromServer()
    }
}
