package com.example.emoji.fragments.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emoji.R
import com.example.emoji.databinding.FragmentMessageBinding
import com.example.emoji.databinding.FragmentPeopleBinding
import com.example.emoji.stub.MessageFactory


class PeopleFragment : Fragment() {

    private lateinit var adapter : UserAdapter

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(layoutInflater)
        adapter = UserAdapter()
        binding.usersRecycler.adapter = adapter

        adapter.submitList(MessageFactory().getUsers())

        return binding.root
    }

}