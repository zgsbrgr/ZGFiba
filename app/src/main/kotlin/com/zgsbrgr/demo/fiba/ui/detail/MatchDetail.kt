@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.ui.detail

import android.graphics.Typeface
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.MatchDetailBinding
import com.zgsbrgr.demo.fiba.domain.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchDetail : Fragment() {

    private lateinit var viewBinding: MatchDetailBinding
    private lateinit var pagerAdapter: GamePagerAdapter

    private val args by navArgs<MatchDetailArgs>()

    private val viewModel by viewModels<MatchDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MatchDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        viewBinding.preview.apply {
            transitionName = args.imageUri!!
            Glide.with(this)
                .load(args.imageUri)
                .into(this)
        }

        pagerAdapter = GamePagerAdapter(this, args.match.id)
        viewBinding.matchPager.adapter = pagerAdapter

        TabLayoutMediator(
            viewBinding.tab,
            viewBinding.matchPager
        ) { tab, position ->
            tab.text = resources.getStringArray(R.array.game_tabs)[position]
        }.attach()

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    it.data?.let { match ->
                        viewBinding.itemMatch = match
                        setPointsForTeams(match)
                    }
                }
            }
        }
    }

    private fun setPointsForTeams(match: Match) {
        viewBinding.awayTeamPoint.text = match.away.points.toString()
        viewBinding.homeTeamPoint.text = match.home.points.toString()
        if (match.away.winner) {
            viewBinding.awayTeam.typeface = Typeface.DEFAULT_BOLD
            viewBinding.awayTeamPoint.typeface = Typeface.DEFAULT_BOLD
        } else {
            viewBinding.awayTeam.typeface = Typeface.DEFAULT
            viewBinding.awayTeamPoint.typeface = Typeface.DEFAULT
            viewBinding.awayTeam.alpha = 0.8f
            viewBinding.awayTeamPoint.alpha = 0.8f
        }

        if (match.home.winner) {
            viewBinding.homeTeam.typeface = Typeface.DEFAULT_BOLD
            viewBinding.homeTeamPoint.typeface = Typeface.DEFAULT_BOLD
        } else {
            viewBinding.homeTeam.typeface = Typeface.DEFAULT
            viewBinding.homeTeamPoint.typeface = Typeface.DEFAULT
            viewBinding.homeTeam.alpha = 0.8f
            viewBinding.homeTeamPoint.alpha = 0.8f
        }
    }

    companion object {
        const val TAG = "MatchDetail"
    }
}
