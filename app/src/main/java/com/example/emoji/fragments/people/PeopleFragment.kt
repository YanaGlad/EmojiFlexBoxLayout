package com.example.emoji.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.emoji.databinding.FragmentPeopleBinding
import com.example.emoji.model.UserModel
import com.example.emoji.viewState.PeopleViewState
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class PeopleFragment : Fragment() {
    private val viewModel: PeopleViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPeopleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        viewModel.viewState.observe(viewLifecycleOwner, {
            handleViewState(it)
        })
    }

    private fun handleViewState(viewState: PeopleViewState) =
        when (viewState) {
            is PeopleViewState.Loaded -> onLoaded(viewState)
            PeopleViewState.Loading -> onLoading()
            PeopleViewState.Error.NetworkError -> {
            }
            PeopleViewState.SuccessOperation -> {
            }
            PeopleViewState.Error.UnexpectedError -> {
            }
        }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onLoaded(viewState: PeopleViewState.Loaded) {
        val mappedList = viewState.list.map {
            UserModel(
                id = it.id,
                name = it.full_name,
                email = it.email,
                picture = it.avatar_url
            )
        }
        adapter.submitList(mappedList)
        binding.progressBar.visibility = View.GONE
    }

    private fun initRecycler() {
        adapter = UserAdapter(object : UserAdapter.OnUserClickListener {
            override fun onUserClick(userModel: UserModel, position: Int) {
                val action = PeopleFragmentDirections.actionPeopleFragmentToOtherPeopleProfile(userModel)
                findNavController().navigate(action)
            }
        })
        binding.usersRecycler.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.dispose()
    }
}
