package com.example.emoji.fragments.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.MessageViewState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class MessageViewModel @AssistedInject constructor(
    private var repo: MessageRepository,
    private var userRepoImpl: UserRepository,
) : ViewModel() {

    @AssistedFactory
    interface MessageViewModelAssistedFactory {
        fun create(): MessageViewModel
    }

    class Factory(
        val factory: MessageViewModelAssistedFactory,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    companion object {
        private const val TAG = "message_view_model_tag"
    }

    private val compositeDisposable = CompositeDisposable()

    private val _viewState: MutableLiveData<MessageViewState> = MutableLiveData()
    val viewState: LiveData<MessageViewState>
        get() = _viewState

    val myUserName: MutableLiveData<String> = MutableLiveData()
    val myUserId: MutableLiveData<Int> = MutableLiveData()

    fun getMyUser() {

        compositeDisposable.add(userRepoImpl.getMyUser()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    myUserName.postValue(it.fullName)
                    myUserId.postValue(it.id)
                },
                {
                    it.convertToViewState()
                }
            ))
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    private fun Throwable.convertToViewState() =
        when (this) {
            is IOException -> MessageViewState.Error.NetworkError
            else -> MessageViewState.Error.UnexpectedError
        }


    fun addReaction(messageId: Int, emoji: String) {
        Log.d(TAG, "MsgId is $messageId emoji is $emoji")

        compositeDisposable.add(
            repo.addReaction(messageId, emoji)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d(TAG, "It is $it")
                        if ((it as MessageViewState) !is MessageViewState.Error.NetworkError)
                            _viewState.postValue(MessageViewState.SuccessOperation)
                    },
                    {
                        Log.d(TAG, "It is ERROR ${it.message}")
                        it.convertToViewState()
                    }
                )
        )
    }


    fun removeReaction(messageId: Int, emoji: String, topicTitle: String, streamTitle: String) {
        Log.d(TAG, "Remove MsgId is $messageId emoji is $emoji")

        _viewState.value = MessageViewState.Loading

        compositeDisposable.add(
            repo.removeReaction(messageId, emoji)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        _viewState.postValue(MessageViewState.SuccessOperation)
                    },
                    {
                        it.convertToViewState()
                    }
                )
        )
        loadMessages(topicTitle, streamTitle)
    }


    fun addMessage(text: String, topicTitle: String, streamTitle: String) {
        Log.d(TAG, "Loading messages")

        _viewState.value = MessageViewState.Loading

        compositeDisposable.add(repo.addMessage(
            streamName = streamTitle,
            topicName = topicTitle,
            text = text
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    _viewState.postValue(MessageViewState.SuccessOperation)
                },
                {
                    _viewState.postValue(it.convertToViewState())
                }
            )
        )
    }

    fun loadMessages(topicTitle: String, streamTitle: String, lastMessageId: Int = 0, messageCount: Int = 1240) {
        Log.d(TAG, "Loading more... ")

        _viewState.value = MessageViewState.Loading

        compositeDisposable.add(repo.getLocalMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    //_viewState.postValue(it)
                    Log.d(TAG, "It LOCAL is  ")
                },
                {
                    Log.d(TAG, "It LOCAL is ERROR ${it.message}")
                    _viewState.postValue(it.convertToViewState())
                }
            )
        )

        val generalMessagesTest = repo.getMessages(
            streamName = streamTitle,
            topicName = topicTitle,
            lastMessageId = lastMessageId,
            count = messageCount
        )

        compositeDisposable.add(
            generalMessagesTest.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
//                        if (it !is MessageViewState.Error.NetworkError)
//                            _viewState.postValue(it)

                        Log.d(TAG, "It REMOTE is $it ")
                    },
                    {
                        Log.d(TAG, "It REMOTE is ERROR ${it.message}")
                        _viewState.postValue(it.convertToViewState())
                    }
                )
        )
    }
}
