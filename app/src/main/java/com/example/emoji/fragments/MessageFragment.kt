package com.example.emoji.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.emoji.R
import com.example.emoji.ToolbarHolder
import com.example.emoji.customview.EmojiFactory
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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MessageFragment : Fragment() {
    private val args: MessageFragmentArgs by navArgs()

    private val usersStub = MessageFactory().getMessages()

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private var stream: StreamModel? = null
    private var topic: TopicModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stream = args.stream
        topic = args.topic

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater)

        initAdapter()
        setupSendPanel()
        setupSendingMessage()
        configureToolbar()

        return binding.root
    }

    private fun configureToolbar() {
        activity?.let { activity ->
            val holder = activity as ToolbarHolder
            stream?.let { holder.setToolbarTitle(it.title) }
            binding.textToolbar.text = topic?.title ?: ""

            holder.setToolbarNavigationButtonIcon(R.drawable.ic_back_arrow)
            holder.showToolbar()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
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
            usersStub.add(
                MessageModel(
                    232,
                    "Me",
                    binding.sendPanel.enterMessageEt.text.toString(),
                    R.drawable.doge,
                    "25",
                    "feb",
                    true,
                    mutableSetOf()
                )
            )
            binding.sendPanel.enterMessageEt.setText("")

            mainAdapter.submitList(usersStub.toDelegateItemListWithDate())
        }
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

             Single.just(oldElement.listReactions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { set ->
                    Single.just(set.toMutableSet())
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
                ).dispose()
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
        mainAdapter.submitList(usersStub.toDelegateItemListWithDate())
    }

    init {
        val reactions = EmojiFactory().getEmoji()
        for (reaction in reactions) {
            suggestReactions.add(Reaction(1, reaction))
        }
    }
}