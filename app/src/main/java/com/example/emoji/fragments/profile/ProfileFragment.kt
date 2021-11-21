package com.example.emoji.fragments.profile

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emoji.App
import com.example.emoji.R
import com.example.emoji.databinding.FragmentProfileBinding
import com.example.emoji.support.MyCoolSnackbar
import com.example.emoji.support.loadImage
import com.example.emoji.viewState.PresenceViewState
import com.example.emoji.viewState.UserViewState
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(
            (activity?.application as App).appComponent.profileViewModelFactory()
        )
    }

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

        viewModel.viewStatePresence.observe(viewLifecycleOwner, {
            handleViewStatePresence(it)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.dispose()
    }

    private fun handleViewStatePresence(viewState: PresenceViewState) =
        when (viewState) {
            is PresenceViewState.Loaded -> onLoadedPresence(viewState)
            is PresenceViewState.Loading -> showSkeleton()
            is PresenceViewState.Error.NetworkError ->
                showErrorSnackbar("Нет соединения с интернетом!") { viewModel.getPresence() }
            is PresenceViewState.SuccessOperation -> {
            }
            is PresenceViewState.Error.UnexpectedError ->
                showErrorSnackbar("Ошибка!") { viewModel.getPresence() }
        }

    private fun showSkeleton() {
        binding.skeleton.root.visibility = View.VISIBLE
        binding.real.root.visibility = View.GONE
    }

    private fun showErrorSnackbar(message: String, action: () -> Unit) {
        action()
        MyCoolSnackbar(
            layoutInflater,
            binding.root,
            message
        )
            .makeSnackBar()
            .show()
    }

    private fun onLoadedPresence(viewState: PresenceViewState.Loaded) {
        with(binding.real.online) {
            text = viewState.presence.status
            when (viewState.presence.status) {
                "offline" -> setTextColor(resources.getColor(R.color.color_offline))
                "idle" -> setTextColor(resources.getColor(R.color.color_idle))
                "active" -> setTextColor(Color.GREEN)
                else -> setTextColor(resources.getColor(R.color.color_offline))
            }
        }
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            binding.skeleton.root.visibility = View.GONE
            binding.real.root.visibility = View.VISIBLE
        }, DELAY_SKELETON)
    }

    private fun handleViewState(viewState: UserViewState) =
        when (viewState) {
            is UserViewState.Loaded -> onLoaded(viewState)

            UserViewState.Loading -> {
            }
            UserViewState.Error.NetworkError -> {
                showErrorSnackbar("Нет соединения с интернетом!") { viewModel.getMyUser() }
            }
            UserViewState.SuccessOperation -> {
            }
            UserViewState.Error.UnexpectedError -> showErrorSnackbar("Ошибка!") { viewModel.getMyUser() }
        }

    private fun onLoaded(viewState: UserViewState.Loaded) {
        viewModel.getPresence()
        binding.real.name.text = viewState.user.fullName
        loadImage(requireContext(), viewState.user.avatarUrl, binding.real.avatar)
    }

    companion object {
        private const val DELAY_SKELETON = 1000L
    }
}
