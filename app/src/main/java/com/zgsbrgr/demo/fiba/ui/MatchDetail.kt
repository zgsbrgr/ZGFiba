package com.zgsbrgr.demo.fiba.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zgsbrgr.demo.fiba.databinding.MatchDetailBinding

class MatchDetail : Fragment() {

    private lateinit var viewBinding: MatchDetailBinding

    private val args by navArgs<MatchDetailArgs>()

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

        viewBinding.preview.apply {
            transitionName = args.imageUri!!
            Glide.with(this)
                .load(args.imageUri)
                .into(this)
        }
    }
}
