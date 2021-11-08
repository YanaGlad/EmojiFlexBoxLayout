package com.example.emoji.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emoji.databinding.FragmentProfileBinding
import com.example.emoji.support.loadImage
import com.example.emoji.viewState.UserViewState


class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        viewModel.getMyUser()

        viewModel.viewState.observe(viewLifecycleOwner, {
            handleViewState(it)
        })

        return binding.root
    }

    private fun handleViewState(viewState: UserViewState) =
        when (viewState) {
            is UserViewState.Loaded -> onLoaded(viewState)

            UserViewState.Loading -> {
            }
            UserViewState.Error.NetworkError -> {
            }
            UserViewState.SuccessOperation -> {
            }
            UserViewState.Error.UnexpectedError -> {
            }
        }

    private fun onLoaded(viewState: UserViewState.Loaded) {
        binding.name.text = viewState.user.full_name
        loadImage(requireContext(), viewState.user.avatar_url, binding.avatar)
        binding.online.text = if (viewState.user.is_active) "online" else "offline"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.dispose()
    }
}
