package com.example.emoji.fragments.channels.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.emoji.R
import com.example.emoji.databinding.FragmentSubscribedBinding
import com.example.emoji.fragments.MessageFragment
import com.example.emoji.fragments.channels.*
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.StreamDelegate
import com.example.emoji.fragments.delegateItem.TopicDelegate
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.support.toDelegateStreamsItemList


class SubscribedFragment : Fragment() {
    companion object {
        fun getInstance(onTopicSelected: OnTopicSelected) =
            SubscribedFragment().apply {
                this.onTopicSelected = onTopicSelected
            }
    }

    private var onTopicSelected: OnTopicSelected? = null

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!


    val streamsStub = listOf(
        StreamModel("#general", listOf(TopicModel("#testing", 1345), TopicModel("#bruh", 234))),
        StreamModel("#Development", listOf(TopicModel("#Android", 4605), TopicModel("#iOS", 4522))),
        StreamModel(
            "#Design",
            listOf(
                TopicModel("#figma", 123),
                TopicModel("#html", 12234),
                TopicModel("#xml", 2342),
                TopicModel("#css", 555)
            )
        ),
        StreamModel("#PR", listOf())
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribedBinding.inflate(layoutInflater)
        initAdapter()
        return binding.root
    }

    var currentViewedModel: StreamModel? = null
    var savedPos: Int = -1

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(StreamDelegate(object : StreamDelegate.OnStreamDelegateClickListener {
                override fun onStreamClick(item: StreamModel, position: Int) {
                    if (currentViewedModel != null && currentViewedModel!!.title == item.title) {
                        mainAdapter.submitList(streamsStub.toDelegateStreamsItemList(-1))
                        currentViewedModel = null
                        savedPos = -1
                    } else {
                        mainAdapter.submitList(
                            streamsStub.toDelegateStreamsItemList(
                                if (savedPos < position) position - (currentViewedModel?.topics?.size
                                    ?: 0) else position
                            )
                        )
                        savedPos = position
                        currentViewedModel = item
                    }
                }
            }))

            addDelegate(TopicDelegate(object : TopicDelegate.OnTopicDelegateClickListener {
                override fun onTopicClick(item: TopicModel, position: Int) {
                    //TODO interface
                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()

                    if(onTopicSelected!=null){
                        onTopicSelected!!.moveToTopicDiscussion(item)
                    }
                }
            }))
        }

        binding.recycleStreams.adapter = mainAdapter
        mainAdapter.submitList(streamsStub.toDelegateStreamsItemList(-1))
    }
}

