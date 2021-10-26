package com.example.emoji.fragments.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.emoji.MainActivity
import com.example.emoji.databinding.FragmentChannelsBinding
import com.example.emoji.fragments.channels.pager.PagerAdapter
import com.example.emoji.fragments.channels.pager.SubscribedFragment
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.google.android.material.tabs.TabLayoutMediator

interface OnTopicSelected {
    fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel)
}

class ChannelsFragment : Fragment(), OnTopicSelected {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(layoutInflater)
        pagerAdapter = PagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        val tabs = listOf("Subscribed", "All streams")

        binding.streamsPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                SubscribedFragment.getInstance(this, true),
                SubscribedFragment.getInstance(this, false)
            )
        )

        TabLayoutMediator(binding.tabs, binding.streamsPager) { tab, pos ->
            tab.text = tabs[pos]
        }.attach()

        activity?.let { activity ->
            (activity as MainActivity).hideToolbar()
        }

        return binding.root
    }

    override fun moveToTopicDiscussion(currentViewedModel: StreamModel, topicModel: TopicModel) {
        findNavController().navigate(
            ChannelsFragmentDirections.actionChannelsFragmentToMessageFragment(
                currentViewedModel,
                topicModel
            )
        )
    }

}