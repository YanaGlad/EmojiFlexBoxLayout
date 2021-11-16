package com.example.emoji.repository

import android.util.Log
import com.example.emoji.api.model.Message
import com.example.emoji.api.model.MessagesNarrowRequest
import com.example.emoji.api.model.Reaction
import com.example.emoji.dataprovider.LocalMessageDataProvider
import com.example.emoji.dataprovider.RemoteMessageDataProvider
import com.example.emoji.model.MessageModel
import com.example.emoji.viewState.MessageViewState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@ExperimentalSerializationApi
class MessageRepositoryImpl @Inject constructor(
    private val remote: RemoteMessageDataProvider,
    private val local: LocalMessageDataProvider,
) : MessageRepository {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun getMessagesQuery(streamName: String, topicName: String, lastMessageId: Int, numBefore: Int): Map<String, String> {
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


    private fun Throwable.convertToViewState(): MessageViewState =
        when (this) {
            is IOException -> MessageViewState.Error.NetworkError
            else -> MessageViewState.Error.UnexpectedError
        }




    override fun getMessages(streamName: String, topicName: String, lastMessageId: Int, count: Int): Single<MessageViewState> {
        val query = getMessagesQuery(streamName, topicName, lastMessageId, count)

//        var viewState: MessageViewState = MessageViewState.Loading
//
//        compositeDisposable.add(
//            local.getAllMessages()
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(
//                    {
//                        Log.d(TAG, "Good $it")
//
//                        viewState = MessageViewState.Loaded(it)
//
//                    },
//                    {
//                        Log.e(TAG, "Error local $it")
//                        viewState = it.convertToViewState()
//                    }
//                )
//        )


        return Single.create { emitter ->
            compositeDisposable.add(remote.getMessages(query)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        local.insertAllMessages(it)
                        Log.d(TAG, "YEEEE $it")
                        emitter.onSuccess(MessageViewState.Loaded(it))
                        dispose()
                    },
                    {
                        Log.e(TAG, "Error!!!!!!!!!!!! $it")
                        emitter.onSuccess(it.convertToViewState())
                        dispose()
                    }
                )
            )
        }
    }

    fun dispose(){
        compositeDisposable.dispose()
    }

    override fun getLocalMessages(): Single<MessageViewState> {
        return Single.create { emitter ->
            compositeDisposable.add(local.getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        local.insertAllMessages(it)
                        Log.d(TAG, "LOCAL YEAH $it")
                        emitter.onSuccess(MessageViewState.Loaded(it))
                    },
                    {
                        Log.e(TAG, "Error LOCAL!!!!!!!!!!!! $it")
                        emitter.onSuccess(it.convertToViewState())
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