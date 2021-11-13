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
import androidx.navigation.fragment.navArgs
import com.example.emoji.R
import com.example.emoji.databinding.FragmentProfileBinding
import com.example.emoji.model.UserModel
import com.example.emoji.support.loadImage
import com.example.emoji.viewState.PresenceViewState
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class OtherPeopleProfile : Fragment() {
    private val viewModel: OtherPeopleProfileViewModel by viewModels()
    private val args: OtherPeopleProfileArgs by navArgs()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val model: UserModel by lazy { args.model }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runViewModel()
        initView()
    }

    private fun handleViewState(viewState: PresenceViewState) =
        when (viewState) {
            is PresenceViewState.Loaded -> onLoaded(viewState)

            is PresenceViewState.Loading -> {
                binding.skeleton.root.visibility = View.VISIBLE
                binding.real.root.visibility = View.GONE
            }
            is PresenceViewState.Error.NetworkError -> {

            }
            is PresenceViewState.SuccessOperation -> {
            }
            is PresenceViewState.Error.UnexpectedError -> {
            }
        }

    private fun onLoaded(viewState: PresenceViewState.Loaded) {
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

        handler.postDelayed( {
            binding.skeleton.root.visibility = View.GONE
            binding.real.root.visibility = View.VISIBLE
        }, 1000)

    }

    private fun initView() {
        binding.real.logout.visibility = View.GONE
        binding.real.name.text = model.name
        loadImage(requireContext(), model.picture, binding.real.avatar)
    }

    private fun runViewModel() {
        viewModel.getPresence(model.id)
        viewModel.viewStatePresence.observe(viewLifecycleOwner, {
            handleViewState(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}