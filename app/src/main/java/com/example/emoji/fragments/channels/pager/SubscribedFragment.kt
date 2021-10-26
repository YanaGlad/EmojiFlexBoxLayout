package com.example.emoji.fragments.channels.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emoji.databinding.FragmentSubscribedBinding
import com.example.emoji.fragments.channels.OnTopicSelected
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.StreamDelegate
import com.example.emoji.fragments.delegateItem.TopicDelegate
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.support.toDelegateStreamsItemList


interface HideIconListener {
    fun setupClicked(clicked: Boolean)
}

class SubscribedFragment : Fragment() {
    private var subscribed = false

    companion object {
        fun getInstance(onTopicSelected: OnTopicSelected, subscribed: Boolean) =
            SubscribedFragment().apply {
                this.onTopicSelected = onTopicSelected
                this.subscribed = subscribed
            }
    }

    var clickedListener: HideIconListener = object : HideIconListener {
        override fun setupClicked(clicked: Boolean) {

        }
    }

    private var onTopicSelected: OnTopicSelected? = null

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!


    var streamsStub = listOf(
        StreamModel(1,
            "#general",
            listOf(TopicModel("#testing", 1345), TopicModel("#bruh", 234)), true, false
        ),
        StreamModel(2,
            "#Development", listOf(
                TopicModel("#Android", 4605),
                TopicModel("#iOS", 4522)
            ), true, false
        ),
        StreamModel(3,
            "#Design",
            listOf(
                TopicModel("#figma", 123),
                TopicModel("#html", 12234),
                TopicModel("#xml", 2342),
                TopicModel("#css", 555)
            ),
            true,
            false
        ),
        StreamModel(4,"#PR", listOf(), false, false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribedBinding.inflate(layoutInflater)
        filterSubscribed()
        initAdapter()
        return binding.root
    }

    private fun filterSubscribed() {
        if (subscribed) {
            streamsStub = streamsStub.filter {
                it.subscribed
            }
        }
    }

    var currentViewedModel: StreamModel? = null
    var savedPos: Int = -1

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(StreamDelegate(object : StreamDelegate.OnStreamDelegateClickListener {
                override fun onStreamClick(item: StreamModel, position: Int) { // TODO click!!!
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
                    if (onTopicSelected != null) {
                        onTopicSelected!!.moveToTopicDiscussion(currentViewedModel!!, item)
                    }
                }
            }))
        }

        binding.recycleStreams.adapter = mainAdapter
        mainAdapter.submitList(streamsStub.toDelegateStreamsItemList(-1))
    }
}

