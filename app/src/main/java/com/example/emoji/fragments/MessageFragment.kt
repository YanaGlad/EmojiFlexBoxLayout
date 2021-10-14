package com.example.emoji.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emoji.R
import com.example.emoji.databinding.FragmentMessageBinding
import com.example.emoji.fragments.delegateItem.*
import com.example.emoji.model.DateModel
import com.example.emoji.model.UserModel


fun List<UserModel>.toDelegateItemListWithDate(): List<DelegateItem> {
    val delegateItemList: MutableList<DelegateItem> = mutableListOf()
    var lastDate = ""
    this
        .sortedBy { it.date.toInt() }
        .forEach { userModel ->
            if (userModel.date != lastDate) {
                delegateItemList.add(
                    DateDelegateItem(
                        DateModel(userModel.date, userModel.month)
                    )
                )
                delegateItemList.add(
                    UserDelegateItem(
                        userModel
                    )
                )
                lastDate = userModel.date
            } else {
                delegateItemList.add(
                    UserDelegateItem(
                        userModel
                    )
                )
            }
        }
    return delegateItemList
}

class MessageFragment : Fragment() {
    private var mainAdapter: MainAdapter? = null

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater)
        initAdapter()

        return binding.root
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()

        val testUser = UserModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar"
        )

        val testUser1 = UserModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar"
        )

        val testUser3 = UserModel(
            name = "Yana Glad",
            message = "Hello! How re you? Как дела?",
            picture = R.drawable.spaceman,
            "18",
            "feb"
        )

        val testUser4 = UserModel(
            name = "Yana Glad",
            message = "Hello! How re you? Как дела?",
            picture = R.drawable.spaceman,
            "11",
            "feb"
        )

        val users = listOf(testUser, testUser1, testUser3, testUser4)

        mainAdapter?.addDelegate(UserDelegate())
        mainAdapter?.addDelegate(DateDelegate())

        binding.recycleMessage.adapter = mainAdapter
        mainAdapter?.submitList(users.toDelegateItemListWithDate())
    }

}