package com.example.emoji.fragments.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emoji.api.Api
import com.example.emoji.repository.MessageRepositoryImpl
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.MessageViewState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class MessageViewModel @AssistedInject constructor(var repo : MessageRepositoryImpl, var userRepo : UserRepository, var api : Api): ViewModel() {
    @AssistedFactory
    interface MessageViewModelAssistedFactory {
        fun create() : MessageViewModel
    }

    class Factory(
        val factory : MessageViewModelAssistedFactory
    ) : ViewModelProvider.Factory{
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

        compositeDisposable.add(userRepo.getMyUser()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    myUserName.postValue(it.full_name)
                    myUserId.postValue(it.id)
                },
                {
                    it.convertToViewState()
                }
            )
        )
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
                        _viewState.postValue(MessageViewState.SuccessOperation)
                    },
                    {
                        Log.d(TAG, "It is ERROR ${it.message}")
                        it.convertToViewState()
                    }
                )
        )
    }


    fun removeReaction(messageId: Int, emoji: String, topicTitle: String) {
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
        loadMessages(topicTitle)
    }


    fun addMessage(text: String) {
        _viewState.value = MessageViewState.Loading

        compositeDisposable.add(repo.addMessage(
            streamName = "general",
            topicName = "test",
            text = text
        )
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
    }

    fun loadMessages(topicTitle: String) {
        _viewState.value = MessageViewState.Loading

        val generalMessagesTest = repo.getMessages(
            streamName = "general",
            topicName = topicTitle,
            lastMessageId = 0,
            count = 1000
        )

        compositeDisposable.add(
            generalMessagesTest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _viewState.postValue(MessageViewState.Loaded(it))
                        Log.d(TAG, "It is $it")
                    },
                    {
                        Log.d(TAG, "It is ERROR ${it.message}")
                        it.convertToViewState()
                    }
                )
        )
    }
}