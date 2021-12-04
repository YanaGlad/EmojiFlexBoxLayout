package com.example.emoji.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.emoji.App
import com.example.emoji.databinding.FragmentPeopleBinding
import com.example.emoji.model.UserModel
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
class PeopleFragment : ElmFragment<UserEvent, UserEffect, UserState>() {

    @Inject
    lateinit var usersGlobalDI: UserGlobalDI

    private lateinit var adapter: UserAdapter

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    override val initEvent: UserEvent
        get() = UserEvent.Internal.AllUsersLoading

    override fun createStore(): Store<UserEvent, UserEffect, UserState> {
        return usersGlobalDI.elmStoreFactory.provide()
    }

    override fun render(state: UserState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        onUsersLoaded(state)
    }

    private fun onUsersLoaded(state: UserState) {
        val mappedList = state.list.map {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
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
    }
}
