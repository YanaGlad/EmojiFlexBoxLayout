package com.example.emoji.fragments.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emoji.api.Api
import com.example.emoji.api.Instance
import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.MessageViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class MessageViewModel : ViewModel() {
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
        val api = Instance.getInstance().create(Api::class.java)
        val repo = UserRepository(api)

        compositeDisposable.add(repo.getMyUser()
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

        _viewState.value = MessageViewState.Loading

        val api = Instance.getInstance().create(Api::class.java)
        val repo = MessageRepository(api)

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


    fun addMessage(text: String) {
        _viewState.value = MessageViewState.Loading

        val api = Instance.getInstance().create(Api::class.java)
        val repo = MessageRepository(api)

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

        val response = Instance.getInstance().create(Api::class.java)
        val repo = MessageRepository(response)

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