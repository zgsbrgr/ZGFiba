package com.zgsbrgr.demo.fiba.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zgsbrgr.demo.fiba.MyActivityViewModel
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.data.util.NetworkMonitor
import com.zgsbrgr.demo.fiba.databinding.HomeBinding
import com.zgsbrgr.demo.fiba.domain.Match
import com.zgsbrgr.demo.fiba.ui.adapter.SectionAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.SectionClickListener
import com.zgsbrgr.demo.fiba.ui.adapter.decor.SpaceItemDecoration
import com.zgsbrgr.demo.fiba.ui.detail.DetailActivity
import com.zgsbrgr.demo.fiba.ui.matches.MatchesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Home : Fragment() {

    private var _viewBinding: HomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel by viewModels<HomeViewModel>()
    private val activityViewModel by activityViewModels<MyActivityViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _viewBinding = HomeBinding.inflate(inflater, container, false)
        viewBinding.homeRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(R.dimen.space, beforeFirst = false, afterLast = true))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = SectionAdapter(
            SectionClickListener { item, title, imageView ->
                if (item is Match) {
                    imageView?.let {
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
                } else if (item is Int) {
                    val intent = Intent(requireActivity(), MatchesActivity::class.java)
                    intent.putExtra("label", title)
                    intent.putExtra("position", item)
                    startActivity(intent)
                }
            }
        )
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        viewBinding.homeRecycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    activityViewModel.isOffline.collect { notConnected->
                        Log.d("connected", notConnected.toString())
                        if (notConnected) {
                            Snackbar
                                .make(
                                    viewBinding.root,
                                    resources.getString(R.string.not_connected),
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }
                launch {
                    viewModel.uiState.collect {
                        adapter.submitList(it.data)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}
