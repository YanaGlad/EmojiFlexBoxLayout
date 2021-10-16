package com.example.emoji.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.emoji.R
import com.example.emoji.databinding.FragmentBottomSheetBinding
import com.example.emoji.fragments.delegateItem.gridEmojiAdapter.EmojiAdapter
import com.example.emoji.model.Reaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment(
    val reactList: ArrayList<Reaction>,
    val onEmogiClick: (reaction: Reaction, position: Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var adapter : EmojiAdapter

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val COLLAPSED_VALUE = 230

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(layoutInflater)

         adapter = EmojiAdapter(object : EmojiAdapter.OnEmojiClickListener {
            override fun onEmojiClick(reaction: Reaction, position: Int) {
                onEmogiClick(reaction, position)
                reactList.removeAt(position)
                dismiss()
            }
        })

        adapter.submitList(reactList)
        binding.grid.adapter = adapter
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = (COLLAPSED_VALUE * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(reactList)
    }
}