package com.example.emoji.fragments.channels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.emoji.databinding.FragmentChannelsBinding
import com.example.emoji.fragments.channels.pager.PagerAdapter
import com.example.emoji.fragments.channels.pager.StreamFragment
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.ExperimentalSerializationApi

interface OnTopicSelected {
    fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel)
}

sealed class SearchViewEvent {
    data class OnQueryChanged(val text: String?) : SearchViewEvent()
    data class OnQueryTextSumbit(val query: String?) : SearchViewEvent()
}

@ExperimentalSerializationApi
class ChannelsFragment : Fragment(), OnTopicSelected {

    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChannelsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPager()
        setupTabs()
        setupSearch()
    }

    private fun setupSearch() {
        compositeDisposable.add(Observable.create(ObservableOnSubscribe<SearchViewEvent> { subscriber ->
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(SearchViewEvent.OnQueryChanged(newText))
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(SearchViewEvent.OnQueryTextSumbit(query))
                    return true
                }
            })
        })
            .switchMap { event ->
                when (event) {
                    is SearchViewEvent.OnQueryChanged -> Observable.just(event.text)
                    is SearchViewEvent.OnQueryTextSumbit -> Observable.just(event.query)
                }
            }.subscribe(
                {
                    val pagerPos = binding.streamsPager.currentItem
                    val currentFragment =
                        activity?.supportFragmentManager?.findFragmentByTag("f${pagerAdapter.getItemId(pagerPos)}")
                    it?.let { (currentFragment as StreamFragment).onSearchHolder.onSearch(it) }

                },
                {
                    Log.e(TAG, "Error while searching ${it.message}")
                }
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()
    }

    override fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel) {
        findNavController().navigate(
            ChannelsFragmentDirections.actionChannelsFragmentToMessageFragment(
                currentViewedModel,
                topicModel
            )
        )
        setupPager()
    }

    private fun setupTabs() {
        val tabs = listOf("Subscribed", "All oneStreams")

        TabLayoutMediator(binding.tabs, binding.streamsPager) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()
    }

    private fun setupPager() {
        pagerAdapter = PagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.streamsPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                StreamFragment.getInstance(this, true),
                StreamFragment.getInstance(this, false)
            )
        )
    }

    companion object{
        private const val TAG = "CHANNELS_TAG"
    }
}