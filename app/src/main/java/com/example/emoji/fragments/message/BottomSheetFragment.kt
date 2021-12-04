package com.example.emoji.fragments.message

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

/**
 * @author y.gladkikh
 */
class BottomSheetFragment(
    reactMap: Map<String, String>,
    var onEmogiClick: (reaction: Reaction, position: Int) -> Unit
) : BottomSheetDialogFragment() {

    companion object{
        private const val COLLAPSE = 230
    }

    private lateinit var adapter : EmojiAdapter

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    val reactListFromMap : ArrayList<Reaction> = arrayListOf()

    init{
        for ((value, id) in reactMap) reactListFromMap.add(Reaction(-1, value, id, false))
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }


    private fun initRecycler() {
        adapter = EmojiAdapter(object : EmojiAdapter.OnEmojiClickListener {
            override fun onEmojiClick(reaction: Reaction, position: Int) {
                onEmogiClick(reaction, position)
                //reactList.removeAt(position)
                dismiss()
            }
        })

        adapter.submitList(reactListFromMap)
        binding.grid.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onStart() {
        super.onStart()
        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.apply {
                peekHeight = (COLLAPSE * density).toInt()
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(reactListFromMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
