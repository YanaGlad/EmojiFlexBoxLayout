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
import com.example.emoji.stub.MessageFactory
import com.example.emoji.support.toDelegateStreamsItemList
import java.util.*
import kotlin.collections.ArrayList

class StreamFragment : Fragment() {
    private var subscribed = false

    var onSearchHolder = object : OnSearchHolder {
        override fun onSearch(text: String) {
            streamsStubs = MessageFactory().getStreamsStub()
                streamsStubs = streamsStubs.filter {
                    it.title.lowercase(Locale.getDefault()).startsWith("#$text")
                } as ArrayList<StreamModel>

            val delegateList = streamsStubs.toDelegateStreamsItemList(-1)
            mainAdapter.submitList(delegateList)
        }
    }

    interface OnSearchHolder {
        fun onSearch(text: String)
    }

    companion object {
        fun getInstance(onTopicSelected: OnTopicSelected, subscribed: Boolean) =
            StreamFragment().apply {
                this.onTopicSelected = onTopicSelected
                this.subscribed = subscribed
            }
    }

    private var onTopicSelected: OnTopicSelected? = null

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    var streamsStubs: ArrayList<StreamModel> = MessageFactory().getStreamsStub()

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
            streamsStubs = streamsStubs.filter {
                it.subscribed
            } as ArrayList<StreamModel>
        }
    }

    var currentViewedModel: StreamModel? = null
    var savedPos: Int = -1

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(StreamDelegate(object : StreamDelegate.OnStreamDelegateClickListener {
                override fun onStreamClick(item: StreamModel, position: Int): Boolean { // TODO click!!!
                    if (currentViewedModel != null && currentViewedModel!!.title == item.title) {
                        streamsStubs = MessageFactory().getStreamsStub()
                        streamsStubs.forEach {
                            it.clicked = false
                        }
                        filterSubscribed()
                        mainAdapter.submitList(streamsStubs.toDelegateStreamsItemList(-1))
                        currentViewedModel = null
                        savedPos = -1
                        return false
                    } else {
                        val pos =
                            if (savedPos < position) position - (currentViewedModel?.topics?.size ?: 0) else position

                        streamsStubs = MessageFactory().getStreamsStub()
                        if (subscribed) {
                            streamsStubs = streamsStubs.filter {
                                it.subscribed
                            } as ArrayList<StreamModel>
                        }

                        val delegateList = streamsStubs.toDelegateStreamsItemList(pos)
                        mainAdapter.submitList(delegateList)

                        savedPos = position
                        currentViewedModel = item

                        return true
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
        mainAdapter.submitList(streamsStubs.toDelegateStreamsItemList(-1))
    }
}

