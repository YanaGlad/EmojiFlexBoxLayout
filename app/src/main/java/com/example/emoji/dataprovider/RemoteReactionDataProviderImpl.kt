package com.example.emoji.dataprovider

import com.example.emoji.api.Api
import com.example.emoji.api.model.Reaction
import com.example.emoji.api.model.mapToReactions
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RemoteReactionDataProviderImpl @Inject constructor(
    private val api: Api,
) : RemoteReactionDataProvider {

    override fun addMessageReaction(id: Int, reactionName: String): Single<Reaction> {
        return api.addMessageReaction(id, reactionName)
    }

    override fun removeMessageReaction(id: Int, reactionName: String): Completable {
        return api.removeMessageReaction(id, reactionName)
    }

    override fun getReactionsFromServer(): Single<List<Reaction>> {
        return api.getReactions()
            .map { response -> response.mapToReactions() }
    }
}