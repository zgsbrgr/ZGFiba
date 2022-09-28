package com.zgsbrgr.demo.fiba.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.HomeBinding
import com.zgsbrgr.demo.fiba.domain.Match
import com.zgsbrgr.demo.fiba.ui.adapter.SectionAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.SectionClickListener
import com.zgsbrgr.demo.fiba.ui.adapter.decor.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home: Fragment() {

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

        val adapter = SectionAdapter(SectionClickListener { item, imageView ->
            imageView?.let {
                if(item is Match) {

                    val extras =  FragmentNavigatorExtras(
                        it to (item as Match).thumb!!
                    )
                    val action = HomeDirections.actionHomeToMatchDetail(imageUri = (item as Match).thumb!!)
                    findNavController().navigate(action, extras)
                }
            }
        })
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