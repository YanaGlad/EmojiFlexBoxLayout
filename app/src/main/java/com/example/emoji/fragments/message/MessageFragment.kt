package com.example.emoji.fragments.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.emoji.R
import com.example.emoji.databinding.FragmentMessageBinding
import com.example.emoji.fragments.delegateItem.DateDelegate
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.UserDelegate
import com.example.emoji.model.MessageModel
import com.example.emoji.model.Reaction
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.stub.MessageFactory
import com.example.emoji.support.MyCoolSnackbar
import com.example.emoji.support.toDelegateItemListWithDate
import com.example.emoji.viewState.MessageViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalSerializationApi
class MessageFragment : Fragment() {
    private val viewModel: MessageViewModel by viewModels()
    private val args: MessageFragmentArgs by navArgs()

    private val usersStub = MessageFactory().getMessages()

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val stream: StreamModel by lazy { args.stream }
    private val topic: TopicModel by lazy { args.topic }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMessages(topic.title)
    }

    private fun handleViewState(viewState: MessageViewState) =
        when (viewState) {
            is MessageViewState.Loaded -> onLoaded(viewState)

            MessageViewState.Loading -> onLoading()
            MessageViewState.Error.NetworkError -> {
            }
            MessageViewState.SuccessOperation -> {
            }
            MessageViewState.Error.UnexpectedError -> {
            }
        }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater)

        viewModel.viewState.observe(viewLifecycleOwner) {
            handleViewState(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initAdapter()
        setupSendPanel()
        setupSendingMessage()
    }

    private fun setupToolbar() {
        with(binding) {
            titleToolbar.text = stream.title
            textToolbar.text = topic.title

            backArrowToolbar.setOnClickListener {
                findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToChannelsFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.dispose()
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

            viewModel.addMessage(binding.sendPanel.enterMessageEt.text.toString())
            viewModel.loadMessages(topic.title)
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

    private fun onLoaded(viewState: MessageViewState.Loaded) {
        val mappedList = viewState.list.map { it ->
            MessageModel(
                id = it.id,
                name = it.authorName,
                picture = it.avatar_url,
                message = it.content,
                date = convertDateFromUnixDay(it.time),
                month = convertDateFromUnix(it.time).substring(0, 3),
                isMe = it.is_me_message,
                listReactions = it.reactions.map { Reaction(it.userId, it.code) }
            )
        }

        mainAdapter.submitList(mappedList.toDelegateItemListWithDate())
        binding.progressBar.visibility = View.GONE
        binding.recycleMessage.visibility = View.VISIBLE


        for (i in mappedList.indices)
            for (j in mappedList[i].listReactions.indices)
                suggestReactions.add(Reaction(mappedList[i].listReactions[j].userId, mappedList[i].listReactions[j].emoji))
    }


    private fun hideKeyboard() {
        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(
            binding.sendPanel.enterMessageEt.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private val suggestReactions: ArrayList<Reaction> = arrayListOf()

    private fun showBottomSheetFragment(messageId: Int) {
        BottomSheetFragment(suggestReactions) { reaction, _ ->
            updateElementWithReaction(messageId, reaction)
            mainAdapter.submitList(usersStub.toDelegateItemListWithDate())
        }.show(childFragmentManager, "bottom_tag")
    }

    private fun updateElementWithReaction(messageId: Int, reaction: Reaction) {
        usersStub.indexOfFirst { it.id == messageId }.let { position ->
            val oldElement = usersStub[position]

            //TODO перенести в view model
            val disposable = Single.just(oldElement.listReactions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { set ->
                    val rnd = Random().nextInt(10)
                    if (rnd < 5)
                        throw RuntimeException("test")
                    Single.just(set.toMutableList())
                }
                .subscribe(
                    {
                        it.add(reaction)
                        usersStub.removeAt(position)
                        val newElement = MessageModel(
                            oldElement.id,
                            oldElement.name,
                            oldElement.message,
                            oldElement.picture,
                            oldElement.date,
                            oldElement.month,
                            oldElement.isMe,
                            it
                        )
                        usersStub.add(position, newElement)
                        mainAdapter.submitList(usersStub.toDelegateItemListWithDate())
                    },
                    {
                        MyCoolSnackbar(
                            layoutInflater,
                            binding.root,
                            "Ошибка!"
                        )
                            .makeSnackBar()
                            .show()
                    }
                )
        }
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(UserDelegate { item, _ ->
                showBottomSheetFragment(item.id)
            })
            addDelegate(DateDelegate())
        }

        binding.recycleMessage.adapter = mainAdapter
    }

}
