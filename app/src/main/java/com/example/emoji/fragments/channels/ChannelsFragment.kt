package com.example.emoji.fragments.channels

import android.os.Bundle
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
import java.util.*
import java.util.concurrent.TimeUnit


interface OnTopicSelected {
    fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel)
}

@ExperimentalSerializationApi
class ChannelsFragment : Fragment(), OnTopicSelected {

    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(layoutInflater)

        setupPager()
        setupTabs()

        compositeDisposable.add(Observable.create(ObservableOnSubscribe<String> { subscriber ->
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
            .map { text -> text.lowercase(Locale.getDefault()).trim()}
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .subscribe { text ->
                val pagerPos = binding.streamsPager.currentItem
                val currentFragment =
                    activity?.supportFragmentManager?.findFragmentByTag("f${pagerAdapter.getItemId(pagerPos)}")
                (currentFragment as StreamFragment).onSearchHolder.onSearch(text)
            }
        )

        return binding.root
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

    override fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel) {
        findNavController().navigate(
            ChannelsFragmentDirections.actionChannelsFragmentToMessageFragment(
                currentViewedModel,
                topicModel
            )
        )
        setupPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.dispose()
    }
}