package com.example.emoji.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.emoji.App
import com.example.emoji.databinding.FragmentPeopleBinding
import com.example.emoji.model.UserModel
import com.example.emoji.support.MyCoolSnackbar
import com.example.emoji.viewState.PeopleViewState
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class PeopleFragment : Fragment() {
    private val viewModel: PeopleViewModel by viewModels() {
        PeopleViewModel.Factory(
            (activity?.application as App).appComponent.usersViewModelFactory()
        )
    }

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
            PeopleViewState.Error.NetworkError -> showErrorSnackbar("Нет соединения с интернетом!")
            PeopleViewState.SuccessOperation -> {
            }
            PeopleViewState.Error.UnexpectedError -> showErrorSnackbar("Ошибка!")
        }

    private fun showErrorSnackbar(message: String) {
        viewModel.loadUsers()
        MyCoolSnackbar(
            layoutInflater,
            binding.root,
            message
        )
            .makeSnackBar()
            .show()
    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onLoaded(viewState: PeopleViewState.Loaded) {
        val mappedList = viewState.list.map {
            UserModel(
                id = it.id,
                name = it.fullName,
                email = it.email,
                picture = it.avatarUrl
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
