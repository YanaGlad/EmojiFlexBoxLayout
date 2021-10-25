package com.example.emoji.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.emoji.R
import com.example.emoji.customview.EmojiFactory
import com.example.emoji.databinding.FragmentMessageBinding
import com.example.emoji.fragments.delegateItem.DateDelegate
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.UserDelegate
import com.example.emoji.model.MessageModel
import com.example.emoji.model.Reaction
import com.example.emoji.stub.UserFactory
import com.example.emoji.support.toDelegateItemListWithDate


class MessageFragment : Fragment() {
    private val usersStub = UserFactory().getUsers()

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater)

        initAdapter()
        setupSendPanel()
        setupSendingMessage()

        return binding.root
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

    val bottomSheet = BottomSheetFragment(suggestReactions) { reaction, _ ->
        Toast.makeText(context, reaction.emoji, Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(UserDelegate(onUserClick = object : UserDelegate.OnUserDelegateClickListener {
                override fun onUserClick(item: MessageModel, position: Int) {
                    bottomSheet.onEmogiClick = { reaction, _ ->
                        item.listReactions.add(reaction)
                        notifyItemChanged(position)
                    }
                    bottomSheet.show(childFragmentManager, "bottom_tag")
                }
            }))
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