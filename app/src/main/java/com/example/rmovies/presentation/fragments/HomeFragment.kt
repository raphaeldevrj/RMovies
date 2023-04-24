package com.example.rmovies.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmovies.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.rmovies.databinding.FragmentHomeBinding
import com.example.rmovies.presentation.adapter.PopularAdapter
import com.example.rmovies.presentation.base.BaseFragment
import com.example.rmovies.presentation.viewmodel.HomeViewModel
import com.example.rmovies.utils.Constants.KEY_BUNDLE_MOVIE_ID
import com.example.rmovies.utils.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    override var uiState: MutableLiveData<UiState> = MutableLiveData()
    private val viewModel: HomeViewModel by viewModel()
    private val popularAdapter: PopularAdapter by lazy {
        PopularAdapter { movie ->
            val bundle = Bundle()
            bundle.putInt(KEY_BUNDLE_MOVIE_ID, movie.id)
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState = uiState
        setupObservables()
        setupSearch()
        setupRecyclerView()
        setupObservables()
    }

    private fun setupSearch() {
        binding?.btnSearch?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setupObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getList().collect { pagingData ->
                popularAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
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
                        tvTitle.isVisible = false
                        contentError.isVisible = false
                        btnTryAgain.setOnClickListener {
                            Toast.makeText(context,"TESTE", Toast.LENGTH_SHORT)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.rvPopular?.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = popularAdapter

        }
    }

}