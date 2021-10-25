package com.example.emoji.fragments.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.emoji.databinding.FragmentChannelsBinding
import com.example.emoji.fragments.MessageFragment
import com.example.emoji.fragments.channels.pager.AllStreamsFragment
import com.example.emoji.fragments.channels.pager.PagerAdapter
import com.example.emoji.fragments.channels.pager.SubscribedFragment
import com.example.emoji.model.TopicModel
import com.google.android.material.tabs.TabLayoutMediator

interface OnTopicSelected{
    fun moveToTopicDiscussion(topic : TopicModel)
}

class ChannelsFragment : Fragment(), OnTopicSelected {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!
    lateinit var pagerAdapter : PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(layoutInflater)
        pagerAdapter = PagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        val tabs = listOf("Subscribed", "All streams")

        binding.streamsPager.adapter = pagerAdapter
        pagerAdapter.update(listOf(SubscribedFragment.getInstance(this), AllStreamsFragment()))

        TabLayoutMediator(binding.tabs, binding.streamsPager){ tab, pos->
            tab.text = tabs[pos]
        }.attach()

        return binding.root
    }

    override fun moveToTopicDiscussion(topic: TopicModel) {
        findNavController().navigate(ChannelsFragmentDirections.actionChannelsFragmentToMessageFragment())
    }

}