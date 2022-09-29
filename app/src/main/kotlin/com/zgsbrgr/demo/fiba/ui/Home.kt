package com.zgsbrgr.demo.fiba.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.HomeBinding
import com.zgsbrgr.demo.fiba.domain.Match
import com.zgsbrgr.demo.fiba.ui.adapter.SectionAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.SectionClickListener
import com.zgsbrgr.demo.fiba.ui.adapter.decor.SpaceItemDecoration
import com.zgsbrgr.demo.fiba.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home : Fragment() {

    private lateinit var viewBinding: HomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = HomeBinding.inflate(inflater, container, false)
        viewBinding.homeRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(R.dimen.space, true))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = SectionAdapter(
            SectionClickListener { item, title, imageView ->
                imageView?.let {
                    if (item is Match) {
                        val intent = Intent(requireActivity(), DetailActivity::class.java)

                        intent.putExtra("match", item)
                        intent.putExtra("imageUri", item.thumb)
                        intent.putExtra("label", title)
                        val options = ActivityOptions
                            .makeSceneTransitionAnimation(
                                requireActivity(),
                                it,
                                item.thumb!!
                            )
                        startActivity(intent, options.toBundle())
                        // shared element transition with fragment
//                        val extras = FragmentNavigatorExtras(
//                            it to item.thumb!!
//                        )
//                        val action = HomeDirections.actionHomeToMatchDetail(imageUri = item.thumb, match = item)
//                        findNavController().navigate(action, extras)
                    }
                }
            }
        )
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        viewBinding.homeRecycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    println(it.data.toString())
                    adapter.submitList(it.data)
                }
            }
        }
    }
}
