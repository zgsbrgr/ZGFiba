package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.GameInfoBinding

const val ARG_OBJECT = "arg_object"

class GameInfo : Fragment() {

    private lateinit var viewBinding: GameInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = GameInfoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            viewBinding.label.text = resources.getStringArray(R.array.game_tabs)[getInt(ARG_OBJECT)]
        }
    }
}

class GamePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val fragment = GameInfo()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}
