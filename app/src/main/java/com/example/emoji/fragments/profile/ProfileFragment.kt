package com.example.emoji.fragments.profile

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emoji.App
import com.example.emoji.R
import com.example.emoji.databinding.FragmentProfileBinding
import com.example.emoji.support.MyCoolSnackbar
import com.example.emoji.support.loadImage
import com.example.emoji.viewState.elm.users.UserEffect
import com.example.emoji.viewState.elm.users.UserEvent
import com.example.emoji.viewState.elm.users.UserGlobalDI
import com.example.emoji.viewState.elm.users.UserState
import kotlinx.serialization.ExperimentalSerializationApi
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class ProfileFragment : ElmFragment<UserEvent, UserEffect, UserState>() {

    @Inject
    lateinit var usersGlobalDI: UserGlobalDI

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override val initEvent: UserEvent
        get() = UserEvent.Internal.MyUserLoading

    override fun createStore(): Store<UserEvent, UserEffect, UserState> {
        return usersGlobalDI.elmStoreFactory.provide()
    }

    override fun render(state: UserState) {
        if (state.isLoading) showSkeleton() else hideSkeleton()
        setupUser(state)
        setupPresence(state)
    }

    private fun setupPresence(state: UserState) {
        with(binding.real.online) {
            text = state.presence?.status
            when (state.presence?.status) {
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

    private fun setupUser(state: UserState) {
        binding.real.name.text = state.model?.fullName
        loadImage(requireContext(), state.model?.avatarUrl, binding.real.avatar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun showSkeleton() {
        binding.skeleton.root.visibility = View.VISIBLE
        binding.real.root.visibility = View.GONE
    }

    private fun hideSkeleton() {
        binding.skeleton.root.visibility = View.GONE
        binding.real.root.visibility = View.VISIBLE
    }

    private fun showErrorSnackbar(message: String) {
        MyCoolSnackbar(
            layoutInflater,
            binding.root,
            message
        )
            .makeSnackBar()
            .show()
    }

    companion object {
        private const val DELAY_SKELETON = 1000L
    }
}
