package com.example.emoji.fragments.message

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.emoji.App
import com.example.emoji.R
import com.example.emoji.api.model.Message
import com.example.emoji.databinding.FragmentMessageBinding
import com.example.emoji.fragments.delegateItem.DateDelegate
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.MessageDelegate
import com.example.emoji.model.MessageModel
import com.example.emoji.model.Reaction
import com.example.emoji.model.reactionsMap
import com.example.emoji.support.MyCoolSnackbar
import com.example.emoji.support.toDelegateItemListWithDate
import com.example.emoji.viewState.elm.messanger.MessageEffect
import com.example.emoji.viewState.elm.messanger.MessageEvent
import com.example.emoji.viewState.elm.messanger.MessengerGlobalDI
import com.example.emoji.viewState.elm.messanger.MessengerState
import kotlinx.serialization.ExperimentalSerializationApi
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class MessageFragment : ElmFragment<MessageEvent, MessageEffect, MessengerState>() {

    //TODO
    private val streamName = "general"
    private val topicName = "test"

    @Inject
    lateinit var messengerGlobalDI: MessengerGlobalDI

    private var cachedMessages: ArrayList<MessageModel> = arrayListOf()

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override val initEvent: MessageEvent = MessageEvent.Internal.PageLoading(
        streamName = streamName, //stream.title,
        topicName = topicName, // topic.title,
        lastMessageId = 0,
        count = 1500
    )

    override fun createStore(): Store<MessageEvent, MessageEffect, MessengerState> {
        return messengerGlobalDI.elmStoreFactory.provide()
    }

    override fun render(state: MessengerState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.recycleMessage.visibility = if (state.isLoading) View.GONE else View.VISIBLE

        initAdapter() //TODO remove
        //mainAdapter.submitList(cachedMessages.toDelegateItemListWithDate())
        setupMessageList(state)
        mainAdapter.submitList(cachedMessages.toDelegateItemListWithDate())
    }

    private fun setupMessageList(state: MessengerState) {
        val mappedList = (state.items as List<Message>)
            .map { it ->
                MessageModel(
                    id = it.id,
                    userId = it.authorId,
                    name = it.authorName,
                    picture = it.avatarUrl,
                    message = it.content,
                    date = convertDateFromUnixDay(it.time),
                    month = convertDateFromUnix(it.time).substring(0, 3),
                    isMe = state.myUserName == it.authorName,
                    listReactions = it.reactions.map {
                        Reaction(
                            userId = it.userId,
                            emoji = it.code,
                            emojiName = it.name,
                            clicked = state.myUserId == it.userId
                        )
                    })
            }

        mappedList.forEach {
            it.countedReactions = countEmoji(it)

            it.listReactions.forEach { reaction ->
               reaction.clicked = reaction.userId == 455747 //TODO REAL
            }
        }
        cachedMessages += mappedList

        mainAdapter.submitList(cachedMessages.toDelegateItemListWithDate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater)

        cachedMessages.clear()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //      viewModel.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initAdapter()
        setupSendPanel()
        setupSendingMessage()
    }

    private fun showErrorSnackbar(message: String) {
        store.accept(MessageEvent.Internal.PageLoading(streamName, topicName))

        MyCoolSnackbar(
            layoutInflater,
            binding.root,
            message
        )
            .makeSnackBar()
            .show()

        binding.progressBar.visibility = View.GONE
        binding.recycleMessage.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        cachedMessages.forEach {
            it.countedReactions = countEmoji(it)
        }

        mainAdapter.submitList(cachedMessages.toDelegateItemListWithDate())
        binding.progressBar.visibility = View.GONE
        binding.recycleMessage.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun setupToolbar() {
        with(binding) {
            titleToolbar.text = "#${streamName}" //TODO args
            textToolbar.text = "#${topicName}"

            backArrowToolbar.setOnClickListener {
                findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToChannelsFragment())
            }
        }
    }

    private fun setupSendPanel() {
        binding.sendPanel.apply {
            sendButton.also {
                it.isEnabled = false
                enterMessageEt.doOnTextChanged { _, _, _, _ ->
                    if (enterMessageEt.text.toString().isEmpty()) {
                        it.setImageDrawable(requireContext().getDrawable(R.drawable.delete))
                        it.isEnabled = false
                    } else {
                        it.setImageDrawable(requireContext().getDrawable(R.drawable.ic_delete))
                        it.isEnabled = true
                    }
                }
            }
        }
    }


    private fun setupSendingMessage() {
        binding.sendPanel.sendButton.setOnClickListener {
            hideKeyboard()

            store.accept(MessageEvent.Internal.MessageAdded(streamName, topicName, binding.sendPanel.enterMessageEt.text.toString(), IOException()))
            store.accept(MessageEvent.Internal.PageLoading(streamName, topicName))

            binding.sendPanel.enterMessageEt.setText("")
        }
    }

    private fun convertDateFromUnix(date: Long): String {
        val sdf = SimpleDateFormat("MMMM d. yyyy. hh:mm", Locale.US)
        val dat = Date(date * 1000L)

        val finalString = sdf.format(dat)

        val calendar = GregorianCalendar()
        calendar.time = dat

        return finalString
    }

    private fun convertDateFromUnixDay(date: Long): String {
        val sdf = SimpleDateFormat("d", Locale.US)
        val dat = Date(date * 1000L)

        val finalString = sdf.format(dat)

        val calendar = GregorianCalendar()
        calendar.time = dat

        return finalString
    }

    private fun hideKeyboard() {
        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(
            binding.sendPanel.enterMessageEt.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showBottomSheetFragment(messageId: Int) {
        BottomSheetFragment(reactionsMap) { reaction, _ ->
            updateElementWithReaction(messageId, reaction)
            mainAdapter.submitList(cachedMessages.toDelegateItemListWithDate())
        }.show(childFragmentManager, "bottom_tag")
    }

    private fun countEmoji(message: MessageModel): Map<String, Int> {
        val emojiCount = mutableMapOf<String, Int>()
        message.listReactions.forEach {
            val oldValue = emojiCount[it.emoji]
            if (oldValue == null) {
                emojiCount[it.emoji] = 1
            } else emojiCount[it.emoji] = oldValue + 1
        }
        return emojiCount
    }

    private fun updateElementWithReaction(messageId: Int, reaction: Reaction) {
        cachedMessages.indexOfFirst { it.id == messageId }.let {
            if (!reaction.clicked) {
                store.accept(MessageEvent.Internal.ReactionAdded(messageId, reaction.emojiName, IOException()))
            } else {
                store.accept(MessageEvent.Internal.ReactionRemoved(messageId, reaction.emojiName, topicName, streamName, IOException()))
            }

            store.accept(MessageEvent.Internal.PageLoading(
                streamName = "general", // stream.title, //TODO args
                topicName = "test", //topic.title,
                lastMessageId = 0,
                count = 1500
            ))
        }
    }


    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(MessageDelegate(
                { item, _ -> showBottomSheetFragment(item.id) },
                { emoji, id ->
                    {
                        //  viewModel.addReaction(id, emoji)
                    }
                },
                { pos ->
                    //   viewModel.loadMessages(topic.title, stream.title, pos)

                })
            )
            addDelegate(DateDelegate())
        }

        binding.recycleMessage.adapter = mainAdapter
    }
}
