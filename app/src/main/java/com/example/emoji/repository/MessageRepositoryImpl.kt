package com.example.emoji.repository

import android.util.Log
import com.example.emoji.api.model.Message
import com.example.emoji.api.model.MessagesNarrowRequest
import com.example.emoji.api.model.Reaction
import com.example.emoji.dataprovider.LocalMessageDataProvider
import com.example.emoji.dataprovider.RemoteMessageDataProvider
import com.example.emoji.viewState.MessageViewState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class MessageRepositoryImpl @Inject constructor(
    private val remote: RemoteMessageDataProvider,
    private val local: LocalMessageDataProvider,
) : MessageRepository {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun getMessagesQuery(
        streamName: String,
        topicName: String,
        lastMessageId: Int,
        numBefore: Int,
    ): Map<String, String> {

        val narrow = mutableListOf(
            MessagesNarrowRequest("stream", streamName)
        )
        if (topicName.isNotEmpty()) narrow.add(MessagesNarrowRequest("topic", topicName))

        val anchor = if (lastMessageId > 0) lastMessageId.toString() else "newest"

        return mapOf(
            "anchor" to anchor,
            "num_before" to numBefore.toString(),
            "num_after" to "0",
            "narrow" to Json.encodeToString(narrow),
            "apply_markdown" to "false"
        )
    }

    private fun addMessageQuery(streamName: String, topicName: String, content: String): Map<String, String> {
        return mapOf(
            "type" to "stream",
            "to" to streamName,
            "content" to content,
            "topic" to topicName
        )
    }


    private fun Throwable.convertToViewState(local: Boolean = false): MessageViewState {
        if (local) return MessageViewState.Error.UnexpectedError
        return when (this) {
            is IOException -> MessageViewState.Error.NetworkError
            else -> MessageViewState.Error.UnexpectedError
        }
    }


    override fun getMessages(
        streamName: String,
        topicName: String,
        lastMessageId: Int,
        count: Int,
    ): Single<List<Message>> {

        val query = getMessagesQuery(streamName, topicName, lastMessageId, count)
        return Single.create { emitter ->
            compositeDisposable.add(remote.getMessages(query)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        local.insertAllMessages(it)
                        emitter.onSuccess(it)
                    },
                    {
                        emitter.onError(it)
                    }
                )
            )
        }
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    override fun getLocalMessages(): Single<List<Message>> {
        return Single.create { emitter ->
            compositeDisposable.add(local.getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d(TAG, "Local messages loaded ")
                        emitter.onSuccess(it)
                    },
                    {
                        Log.e(TAG, "Error in local $it")
                      ///  emitter.onError(it)
                    }
                )
            )
        }
    }

    override fun addMessage(streamName: String, topicName: String, text: String): Completable {
        val query = addMessageQuery(streamName, topicName, text)
        return remote.sendMessage(query)
    }

    override fun addReaction(messageId: Int, name: String): Single<Reaction> {
        return remote.addMessageReaction(messageId, name)
    }

    override fun removeReaction(id: Int, reactionName: String): Completable {
        return remote.removeMessageReaction(id, reactionName)
    }

    companion object {
        private const val TAG = "MESSAGE_REPOSITORY_TAG"
    }
}
