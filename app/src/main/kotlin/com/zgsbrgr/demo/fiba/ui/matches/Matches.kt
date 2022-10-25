package com.zgsbrgr.demo.fiba.ui.matches

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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.zgsbrgr.demo.fiba.MyActivityViewModel
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.MatchesBinding
import com.zgsbrgr.demo.fiba.ui.adapter.MatchAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.MatchItemClickListener
import com.zgsbrgr.demo.fiba.ui.adapter.decor.SpaceItemDecoration
import com.zgsbrgr.demo.fiba.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Matches : Fragment() {

    private lateinit var viewBinding: MatchesBinding
    private val args by navArgs<MatchesArgs>()

    private val viewModel by viewModels<MatchesViewModel>()
    private val activityViewModel by activityViewModels<MyActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchesBinding.inflate(inflater, container, false)
        viewBinding.matchesRv.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(R.dimen.space))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = MatchAdapter(
            MatchItemClickListener { match, imageView ->
                val intent = Intent(requireActivity(), DetailActivity::class.java)

                intent.putExtra("match", match)
                intent.putExtra("imageUri", match.thumb)
                intent.putExtra("label", args.label)
                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        requireActivity(),
                        imageView,
                        match.thumb!!
                    )
                startActivity(intent, options.toBundle())
            }
        )
        viewBinding.matchesRv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Log.d(TAG, it.data.toString())
                    adapter.submitList(it.data)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.isOffline.collect { notConnected ->
                    if (notConnected) {
                        Toast.makeText(
                            requireActivity(),
                            resources.getString(R.string.not_connected),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else
                        viewModel.uiState.collect {
                            Log.d(TAG, it.data.toString())
                            adapter.submitList(it.data)
                        }
                }
            }
        }
    }

    companion object {
        const val TAG = "MatchesF"
    }
}
