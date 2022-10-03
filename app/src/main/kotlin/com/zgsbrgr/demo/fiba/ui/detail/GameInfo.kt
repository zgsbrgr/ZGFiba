package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zgsbrgr.demo.fiba.data.GameInfoRepository
import com.zgsbrgr.demo.fiba.databinding.GameInfoBinding
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import com.zgsbrgr.demo.fiba.ui.adapter.MatchEventAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ARG_OBJECT = "arg_object"
const val ARG_MATCH_ID = "arg_match_id"

@AndroidEntryPoint
class GameInfo : Fragment() {

    private lateinit var viewBinding: GameInfoBinding

    // private val viewModel by viewModels<GameInfoViewModel>()

    @Inject
    lateinit var gameInfoRepository: GameInfoRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = GameInfoBinding.inflate(inflater, container, false)
        viewBinding.eventRv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = GameInfoViewModel.GameInfoViewModelFactory(
            gameInfoRepository,
            arguments?.getString(ARG_MATCH_ID)
        )
        val viewModel = ViewModelProvider(this, viewModelFactory)[GameInfoViewModel::class.java]

        val eventList = mutableListOf<MatchEvent>()

        val adapter = MatchEventAdapter()

//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            viewBinding.label.text = resources.getStringArray(R.array.game_tabs)[getInt(ARG_OBJECT)]
//        }
        viewBinding.eventRv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    it.data?.let { event ->
                        eventList.add(event)
                        if (adapter.currentList.isEmpty())
                            adapter.submitList(eventList)
                        adapter.notifyItemInserted(eventList.size)
                        viewBinding.eventRv.scrollToPosition(eventList.size - 1)
                    }

                    if (it.error != null)
                        Toast.makeText(requireActivity(), it.error.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

class GamePagerAdapter(fragment: Fragment, private val matchId: String) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val fragment = GameInfo()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
            putString(ARG_MATCH_ID, matchId)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}
