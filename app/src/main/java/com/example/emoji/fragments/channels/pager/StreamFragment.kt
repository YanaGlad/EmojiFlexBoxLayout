package com.example.emoji.fragments.channels.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emoji.App
import com.example.emoji.databinding.FragmentSubscribedBinding
import com.example.emoji.fragments.channels.OnTopicSelected
import com.example.emoji.fragments.delegateItem.MainAdapter
import com.example.emoji.fragments.delegateItem.StreamDelegate
import com.example.emoji.fragments.delegateItem.TopicDelegate
import com.example.emoji.fragments.message.MessageViewModel
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.support.toDelegateStreamsItemList
import com.example.emoji.viewState.StreamViewState
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*

@ExperimentalSerializationApi
class StreamFragment : Fragment() {

    private val viewModel: StreamsViewModel by viewModels {
        StreamsViewModel.Factory(
            (activity?.application as App).appComponent.streamsViewModelFactory()
        )
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

    private var onTopicSelected: OnTopicSelected? = null

    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    private var subscribed = false
    var streamsReal: List<StreamModel> = listOf()
    var streamsEvent: List<StreamModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadStreams()
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
        viewModel.viewState.observe(viewLifecycleOwner, {
            handleViewState(it)
        })
        filterSubscribed()
        initAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.dispose()
    }

    private fun handleViewState(viewState: StreamViewState) =
        when (viewState) {
            is StreamViewState.Loaded -> onLoaded(viewState)

            StreamViewState.Loading -> onLoading()
            StreamViewState.Error.NetworkError -> {
            }
            StreamViewState.SuccessOperation -> {
                binding.progressBar.visibility = View.GONE
            }
            StreamViewState.Error.UnexpectedError -> {
            }
        }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onLoaded(viewState: StreamViewState.Loaded) {
        binding.progressBar.visibility = View.GONE
        streamsReal = viewState.list
        streamsEvent = viewState.list
        initAdapter()
    }

    private fun filterSubscribed() {
        if (subscribed) {
            streamsReal = streamsReal.filter {
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
                        streamsReal.forEach {
                            it.clicked = false
                        }
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

                        val delegateList = streamsReal.toDelegateStreamsItemList(pos)
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
        mainAdapter.submitList(streamsReal.toDelegateStreamsItemList(-1))
    }

    companion object {
        private const val TAG = "STREAMS_TAG"
        fun getInstance(onTopicSelected: OnTopicSelected, subscribed: Boolean) =
            StreamFragment().apply {
                this.onTopicSelected = onTopicSelected
                this.subscribed = subscribed
            }
    }

    interface OnSearchHolder {
        fun onSearch(text: String)
    }
}

