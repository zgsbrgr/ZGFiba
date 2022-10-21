@file:Suppress("MagicNumber", "EmptyFunctionBlock")
package com.zgsbrgr.demo.fiba.ui.detail

import android.animation.Animator
import android.graphics.Typeface
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.MatchDetailBinding
import com.zgsbrgr.demo.fiba.domain.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

@AndroidEntryPoint
class MatchDetail : Fragment() {

    private lateinit var viewBinding: MatchDetailBinding
    private lateinit var pagerAdapter: GamePagerAdapter

    private val args by navArgs<MatchDetailArgs>()

    private val viewModel by viewModels<MatchDetailViewModel>(
    )

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

        setupMenu()
        addBackPressCallback()

        viewBinding.preview.apply {
            transitionName = args.imageUri!!
            Glide.with(this)
                .load(args.imageUri)
                .into(this)
        }

        pagerAdapter = GamePagerAdapter(this, args.match)
        viewBinding.matchPager.adapter = pagerAdapter

        viewBinding.result.setOnClickListener {
            findNavController()
                .navigate(
                    MatchDetailDirections
                        .actionMatchDetailToRoster(
                            homeTeam = args.match.home,
                            awayTeam = args.match.away
                        )
                )
        }

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

    /**
     * inflating fragment related menu in the actionbar
     */
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_details, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == R.id.action_settings) {
                        viewBinding.overlapInfo.post {
                            showOverlap()
                        }
                        true
                    } else
                        false
                }

                override fun onPrepareMenu(menu: Menu) {
                    super.onPrepareMenu(menu)
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    /**
     * handling back press in fragment
     * checking here if the overlay is visible, if so pressing back just hides this view
     * else delegate the back press handling to the activity
     */
    private fun addBackPressCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isOpened)
                    showOverlap()
                else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
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

    /**
     * show an overlay on the screen with a circular reveal animation
     */
    private var isOpened: Boolean = false
    private fun showOverlap() {

        if (!isOpened) {

            val x = viewBinding.overlapInfo.right
            val y = viewBinding.overlapInfo.top

            Log.d(TAG, x.toString())

            val startRadius = 0f
            val endRadius = hypot(viewBinding.content.width.toDouble(), viewBinding.content.height.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(viewBinding.overlapInfo, x, y, startRadius, endRadius)
            viewBinding.overlapInfo.visibility = View.VISIBLE
            anim.start()
            isOpened = true
        } else {
            val x = viewBinding.content.right
            val y = viewBinding.content.top

            val startRadius = max(viewBinding.content.width, viewBinding.content.height).toFloat()
            val endRadius = 0f

            val anim = ViewAnimationUtils.createCircularReveal(viewBinding.overlapInfo, x, y, startRadius, endRadius)

            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    // no - op
                }
                override fun onAnimationEnd(animator: Animator) {
                    viewBinding.overlapInfo.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

            anim.start()
            isOpened = false
        }
    }

    companion object {
        const val TAG = "MatchDetail"
    }
}
