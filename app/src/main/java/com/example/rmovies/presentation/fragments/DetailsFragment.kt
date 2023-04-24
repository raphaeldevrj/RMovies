package com.example.rmovies.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.rmovies.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.rmovies.databinding.FragmentDetailsBinding
import com.example.rmovies.presentation.base.BaseFragment
import com.example.rmovies.presentation.viewmodel.DetailsViewModel
import com.example.rmovies.utils.Constants.KEY_BUNDLE_MOVIE_ID
import com.example.rmovies.utils.UiState

class DetailsFragment : BaseFragment() {

    private var binding: FragmentDetailsBinding? = null
    private val movieId: Int by lazy {
        arguments?.getInt(KEY_BUNDLE_MOVIE_ID) ?: -1
    }
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState = uiState
        viewModel.getMovieById(movieId)
        setupObservables()
    }

    private fun setupObservables() {
        viewModel.onSuccessMovieById.observe(viewLifecycleOwner) {
            it?.let { movie ->
                binding?.let { bindingNonNull ->
                    with(bindingNonNull) {
                        contentSucess.isVisible = true
                        contentError.isVisible = false
                        progressBar.isVisible = false
                        activity?.let { activityNonNull ->
                            Glide.with(activityNonNull)
                                .load(movie.backdrop_path)
                                .error(R.drawable.place_holder)
                                .into(ivBackPath)
                        }
                        if (movie.overview.isBlank()) {
                            tvOverview.text = getString(R.string.text_error_overview)
                        } else {
                            tvOverview.text = movie.overview
                        }
                        tvTitleBr.text = movie.title
                        tvTitleOriginal.text = movie.original_title
                    }
                }
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding?.apply {
                        contentError.isVisible = false
                        progressBar.isVisible = true
                    }
                }
                is UiState.Error -> {
                    binding?.apply {
                        progressBar.isVisible = false
                        contentError.isVisible = true
                        btnTryAgain.setOnClickListener {
                            viewModel.getMovieById(movieId)
                        }
                    }
                }
            }
        }
        binding?.ivBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override var uiState: MutableLiveData<UiState> = MutableLiveData()
}