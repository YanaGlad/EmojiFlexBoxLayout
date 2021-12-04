package com.example.emoji.fragments.channels.pager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emoji.App
import com.example.emoji.databinding.FragmentSubscribedBinding
import com.example.emoji.fragments.channels.OnTopicSelected
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.StreamDelegate
import com.example.emoji.fragments.delegateItem.TopicDelegate
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.support.toDelegateStreamsItemList
import com.example.emoji.viewState.elm.stream.StreamEffect
import com.example.emoji.viewState.elm.stream.StreamEvent
import com.example.emoji.viewState.elm.stream.StreamGlobalDI
import com.example.emoji.viewState.elm.stream.StreamState
import kotlinx.serialization.ExperimentalSerializationApi
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.util.*
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class StreamFragment : ElmFragment<StreamEvent, StreamEffect, StreamState>() {

    interface OnSearchHolder {
        fun onSearch(text: String)
    }

    var currentViewedModel: StreamModel? = null
    var savedPos: Int = -1

    @Inject
    lateinit var streamGlobalDI: StreamGlobalDI

    override val initEvent: StreamEvent
        get() = StreamEvent.Internal.PageLoading()


    override fun render(state: StreamState) {
        if (state.isLoading) showSkeleton() else stopSkeleton()

        if (state.model != null) {
            streamsReal.add(state.model)
            streamsEvent.add(state.model)
        }
        initAdapter()

    }

    override fun createStore(): Store<StreamEvent, StreamEffect, StreamState> {
        return streamGlobalDI.elmStoreFactory.provide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubscribedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterSubscribed()
        initAdapter()
    }

    var onSearchHolder = object : OnSearchHolder {
        override fun onSearch(text: String) {
            streamsReal = streamsEvent.filter {
                Log.d(TAG, " Got text : $text")
                it.title.lowercase(Locale.getDefault()).startsWith(text)
            } as ArrayList<StreamModel>

            val delegateList = streamsReal.toDelegateStreamsItemList(-1)
            mainAdapter.submitList(delegateList)
        }
    }

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    private var onTopicSelected: OnTopicSelected? = null

    private lateinit var mainAdapter: MainAdapter

    private var subscribed = false
    var streamsReal: ArrayList<StreamModel> = arrayListOf()
    var streamsEvent: ArrayList<StreamModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    private fun showSkeleton() {
        binding.skeleton.root.visibility = View.VISIBLE
        binding.real.root.visibility = View.GONE
    }


    private fun stopSkeleton() {
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            binding.skeleton.root.visibility = View.GONE
            binding.real.root.visibility = View.VISIBLE
        }, DELAY_SKELETON)
    }

    private fun filterSubscribed() {
        if (subscribed) {
            streamsReal = streamsReal.filter {
                it.subscribed
            } as ArrayList<StreamModel>
        }
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        mainAdapter.apply {
            addDelegate(StreamDelegate(object : StreamDelegate.OnStreamDelegateClickListener {
                override fun onStreamClick(item: StreamModel, position: Int): Boolean { // TODO click!!!
                    if (currentViewedModel != null && currentViewedModel!!.title == item.title) {
                        streamsReal.forEach {
                            it.clicked = false
                        }
                        notifyDataSetChanged() //TODO что не так с диффом?
                        filterSubscribed()
                        mainAdapter.submitList(streamsReal.toDelegateStreamsItemList(-1))
                        currentViewedModel = null
                        savedPos = -1
                        return false
                    } else {
                        val pos =
                            if (savedPos < position) position - (currentViewedModel?.topics?.size ?: 0) else position

                        if (subscribed) {
                            streamsReal = streamsReal.filter {
                                it.subscribed
                            } as ArrayList<StreamModel>
                        }

                        mainAdapter.submitList(streamsReal.toDelegateStreamsItemList(pos))
                        notifyDataSetChanged()

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

        binding.real.recycleStreams.adapter = mainAdapter
        mainAdapter.submitList(streamsReal.toDelegateStreamsItemList(-1))
    }

    companion object {
        private const val DELAY_SKELETON = 2500L
        private const val TAG = "STREAMS_TAG"

        fun getInstance(onTopicSelected: OnTopicSelected, subscribed: Boolean) =
            StreamFragment().apply {
                this.onTopicSelected = onTopicSelected
                this.subscribed = subscribed
            }
    }
}
